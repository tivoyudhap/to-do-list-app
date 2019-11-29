package com.mastip.personaltodolist.screen.activity.controller;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.manager.database.Storage;
import com.mastip.personaltodolist.model.GroupingTaskModel;
import com.mastip.personaltodolist.model.TaskModel;
import com.mastip.personaltodolist.screen.activity.JAddTaskActivity;
import com.mastip.personaltodolist.screen.activity.JLoginActivity;
import com.mastip.personaltodolist.screen.activity.JMainMenuActivity;
import com.mastip.personaltodolist.support.adapter.TaskAdapter;
import com.mastip.personaltodolist.utilities.Constant;
import com.mastip.personaltodolist.utilities.CustomSharedPreferences;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by HateLogcatError on 8/28/2017.
 */

public class CMainMenuController {

    JMainMenuActivity activity;

    List<TaskModel> dataUnfinish = new ArrayList<>();
    ArrayList<TaskModel> dataFinish = new ArrayList<>();
    ArrayList<GroupingTaskModel> groupTask = new ArrayList<>();
    ArrayList<Integer> headerPosition = new ArrayList<>();

    TaskAdapter adapter;

    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    public CMainMenuController(JMainMenuActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        oneSignalInit();
        hideActionBar();
        initTask();
    }

    private void oneSignalInit() {
        OneSignal.startInit(activity)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void hideActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.hide();
    }

    private void retrieveData() {
        dataUnfinish.clear();
        dataFinish.clear();

        dataUnfinish = new Storage(activity).getAllData(Constant.TAG_UNFINISH_ADDTASK);
        dataFinish = new Storage(activity).getAllData(Constant.TAG_FINISH_ADDTASK);

        classifyData();
        getCountGroupData();
    }

    private void setAdapter() {
        adapter = new TaskAdapter(activity, groupTask);

        activity.getListTask().setItemAnimator(new DefaultItemAnimator());
        activity.getListTask().setLayoutManager(new LinearLayoutManager(activity));
        activity.getListTask().setAdapter(adapter);
    }

    private void initTask() {
        retrieveData();
        setAdapter();

        ItemTouchHelper.Callback touchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                int decreator = headerPosition.get(checkIndexForGroup(position)) + 1;

                GroupingTaskModel groupingTaskModel = groupTask.get(checkIndexForGroup(position));
                List<TaskModel> taskModelList = groupingTaskModel.getTaskModel();
                TaskModel taskModel = taskModelList.get(position - decreator);

                new Storage(activity).disposeTransaction(taskModel.getId());
                refreshUI(checkIndexForGroup(position), position - decreator);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if(viewHolder.getItemViewType() == Constant.VIEW_HEADER ||
                        viewHolder.getItemViewType() == Constant.VIEW_BODY_FINISH) {
                    return 0;
                }

                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
        itemTouchHelper.attachToRecyclerView(activity.getListTask());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constant.TAG_ACTIVITY_RESULT_ADDTASK && data != null) {
            TaskModel taskModel = (TaskModel) data.getExtras().getParcelable(Constant.TAG_ADD_TASK_RESULT);
            refreshUI();
        }
    }

    private void refreshUI() {
        groupTask.clear();

        retrieveData();
        adapter.notifyDataSetChanged();
    }

    private void refreshUI(int groupPosition, int listPosition) {
        groupTask.get(groupPosition).getTaskModel().get(listPosition).setFinish(new Date().getTime());
        TaskModel taskModel = groupTask.get(groupPosition).getTaskModel().get(listPosition);
        if(groupTask.get(groupTask.size() - 1).getGroupName().equals(Constant.TAG_MY_TASK_FINISH)) {
            groupTask.get(groupTask.size() - 1).getTaskModel().add(0, taskModel);
        }
        else {
            List<TaskModel> taskModels = new ArrayList<>();
            taskModels.add(taskModel);
            groupTask.add(new GroupingTaskModel(Constant.TAG_MY_TASK_FINISH, taskModels, ""));
        }
        groupTask.get(groupPosition).getTaskModel().remove(listPosition);

        if(groupTask.get(groupPosition).getTaskModel().size() == 0) {
            groupTask.remove(groupPosition);
        }

        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.mainmenu_fab_floatingactionbutton)
    void onFabClicked() {
        Intent intent = new Intent(activity, JAddTaskActivity.class);
        activity.startActivityForResult(intent, Constant.TAG_ACTIVITY_RESULT_ADDTASK);
    }

    @OnClick(R.id.toolbar_logout_imageview)
    void onLogout() {
        new CustomSharedPreferences().deleteDataString(Constant.TAG_USERNAME);

        LoginManager.getInstance().logOut();

        Intent intent = new Intent(activity, JLoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    // form : date will be check - date today = result / interval day
    // then, add to the result / interval day to date today
    // example :
    // 21 - 11 = 10 / 3 = 3 (rounded to down)
    // 11 + (3 x 3) = 11 + 9 = 20;
    // so 21 is estimated to be after 20th
    private void classifyData() {
        Calendar calendar1 = Calendar.getInstance();

        for(TaskModel taskModel: dataUnfinish) {
            calendar1.setTime(new Date());
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.set(Calendar.MILLISECOND, 0);

            Date dateNow = calendar1.getTime();
            Date dateSubmission = new Date(taskModel.getSubmission());

            List<TaskModel> taskModels = new ArrayList<>();
            taskModels.add(taskModel);

            if(dateSubmission.equals(dateNow) || dateSubmission.after(dateNow)) {
                // not expired
                int difference = (int) checkTimeDiffByDay(dateNow.getTime(), dateSubmission.getTime());

                int dateFlagIncreaser = difference / Constant.FLAG_DATE_INCREMENT;
                int increaser = dateFlagIncreaser * Constant.FLAG_DATE_INCREMENT;

                calendar1.add(Calendar.DATE, increaser);

                if(groupTask.size() > 0) {
                    for (int a = 0; a < groupTask.size(); a++) {
                        GroupingTaskModel groupingTaskModel = groupTask.get(a);
                        // check if group list already exist
                        if(groupingTaskModel.getDate().equals(simpleDateFormat1.format(calendar1.getTime()))) {
                            groupTask.get(a).getTaskModel().add(taskModel);
                            break;
                        }
                        else {
                            // if the group loop is last, it will create new list of group
                            if(a == groupTask.size() - 1) {
                                groupTask.add(new GroupingTaskModel(Constant.TAG_MY_TASK, taskModels, simpleDateFormat1.format(calendar1.getTime())));
                                break;
                            }
                        }
                    }
                }
                else {
                    groupTask.add(new GroupingTaskModel(Constant.TAG_MY_TASK, taskModels, simpleDateFormat1.format(calendar1.getTime())));
                }
            }
            else {
                // expired

                if(groupTask.size() > 0) {
                    if(groupTask.get(0).getGroupName().equals(Constant.TAG_EXPIRED)) {
                        groupTask.get(0).getTaskModel().add(taskModel);
                    }
                }
                else {
                    groupTask.add(new GroupingTaskModel(Constant.TAG_EXPIRED, taskModels, ""));
                }
            }
        }

        // also add finish data
        for (TaskModel taskModel: dataFinish) {
            List<TaskModel> taskModels = new ArrayList<>();
            taskModels.add(taskModel);

            if(groupTask.size() > 0) {
                GroupingTaskModel lastGroup = groupTask.get(groupTask.size() - 1);

                if(lastGroup.getGroupName().equals(Constant.TAG_MY_TASK_FINISH)) {
                    lastGroup.getTaskModel().add(taskModel);
                }
                else {
                    groupTask.add(new GroupingTaskModel(Constant.TAG_MY_TASK_FINISH, taskModels, ""));
                }
            } else {
                groupTask.add(new GroupingTaskModel(Constant.TAG_MY_TASK_FINISH, taskModels, ""));
            }
        }
    }

    private long checkTimeDiffByDay(long date1, long date2) {
        final long MSPERDAY = 60 * 60 * 24 * 1000;
        return (new Date(date2).getTime() - new Date(date1).getTime()) / MSPERDAY;
    }

    private int getCountGroupData() {
        headerPosition.clear();

        int positionCount = 0;

        for (GroupingTaskModel groupingTaskModel: groupTask) {
            headerPosition.add(positionCount);
            positionCount = positionCount + groupingTaskModel.getTaskModel().size() + 1;
        }

        return positionCount;
    }

    private int checkIndexForGroup(int position) {
        int positionCount = 0;
        for(int a = 0; a < groupTask.size(); a++) {
            GroupingTaskModel zeroIndexed = groupTask.get(0);

            if(position < zeroIndexed.getTaskModel().size() + 1) {
                return positionCount;
            }
            else if(position > zeroIndexed.getTaskModel().size() && position < getBeforeLastPosition()) {
                for(int b = 1; b < headerPosition.size() - 1; b++) {
                    int headerIndex = headerPosition.get(b);
                    int headerIndexAfter = headerPosition.get(b + 1);

                    if(position >= headerIndex && position < headerIndexAfter) {
                        return b;
                    }
                }
            }
            else {
                return groupTask.size() - 1;
            }
        }
        return positionCount;
    }

    private int getBeforeLastPosition() {
        return headerPosition.get(groupTask.size() - 1);
    }

//    private void initDate() {
//        calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//    }

//    private void classifyData() {
//        initDate();
//
//        if(dataUnfinish.size() > 0) {
//            int z = 0;
//
//            expiredTaskChecker();
//
//            while(z < dataUnfinish.size()) {
//                Date start = calendar.getTime();
//                calendar.add(Calendar.DATE, 3);
//                Date finish = calendar.getTime();
//
//                for (TaskModel taskModel: dataUnfinish) {
//                    if(checkDate(start, finish, new Date(taskModel.getSubmission()))) {
//                        classifiedData.add(taskModel);
//                        z++;
//                    }
//                }
//
//                if(classifiedData.size() > 0) {
//                    List<TaskModel> listGroup = new ArrayList<>();
//                    listGroup.addAll(classifiedData);
//
//                    groupTask.add(new GroupingTaskModel("My Task - ", listGroup, start.toString()));
//
//                    classifiedData.clear();
//                }
//            }
//            dataUnfinish.clear();
//        }
//
//        if(dataFinish.size() > 0) {
//            List<TaskModel> listGroup = new ArrayList<>();
//            listGroup.addAll(dataFinish);
//
//            groupTask.add(new GroupingTaskModel("Task Finished", listGroup, ""));
//        }
//    }
//
//    private boolean checkDate(Date dateStart, Date dateEnd, Date dateCheck) {
//        return dateCheck.equals(dateStart) || dateCheck.after(dateStart) && dateCheck.before(dateEnd);
//    }
//
//    private void expiredTaskChecker() {
//        List<TaskModel> removeList = new ArrayList<>();
//
//        for(int a = 0; a < dataUnfinish.size(); a++) {
//            TaskModel taskModel = dataUnfinish.get(a);
//            if(isDateExpired(new Date(taskModel.getSubmission()).getTime())) {
//                classifiedData.add(taskModel);
//                removeList.add(taskModel);
//            }
//        }
//
//        dataUnfinish.removeAll(removeList);
//
//        List<TaskModel> list = new ArrayList<>();
//        list.addAll(classifiedData);
//
//        if(classifiedData.size() > 0) {
//            groupTask.add(new GroupingTaskModel("Expired Task", list, ""));
//        }
//
//        classifiedData.clear();
//    }
//
//    private boolean isDateExpired(long checkedDate) {
//        Calendar cals = simpleDateFormat.getCalendar();
//        cals.setTime(new Date());
//        cals.set(Calendar.HOUR, 0);
//        cals.set(Calendar.MINUTE, 0);
//        cals.set(Calendar.SECOND, 0);
//        Date dates = cals.getTime();
//
//        if(checkedDate < new Date(simpleDateFormat.format(dates)).getTime()) {
//            // expired
//            return true;
//        }
//
//        // not expired
//        return false;
//    }

}