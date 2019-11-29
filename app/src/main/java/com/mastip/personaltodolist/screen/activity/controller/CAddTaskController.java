package com.mastip.personaltodolist.screen.activity.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.manager.database.Storage;
import com.mastip.personaltodolist.model.TaskModel;
import com.mastip.personaltodolist.screen.activity.JAddTaskActivity;
import com.mastip.personaltodolist.screen.activity.JMainMenuActivity;
import com.mastip.personaltodolist.utilities.Constant;
import com.mastip.personaltodolist.utilities.CustomSharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by HateLogcatError on 8/28/2017.
 */


public class CAddTaskController {

    private JAddTaskActivity activity;

    private int statusLow = 0;
    private int statusMed = 0;
    private int statusHigh = 0;

    private int priorStatus = 0;

    private int year = 0;
    private int month = 0;
    private int day = 0;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);

    @BindString(R.string.task_must_longer_than_5) String warningTask;
    @BindString(R.string.please_set_the_time) String warningSetTime;
    @BindString(R.string.tap_here_to_set_time) String warningTapToSetTime;
    @BindString(R.string.please_set_priority) String warningSetPriority;

    public CAddTaskController(JAddTaskActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
    }

    private void doingToLowButton() {
        if(statusHigh != 0) {
            activity.getHighPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button));
            statusHigh = 0;
        }
        if(statusMed != 0) {
            activity.getMediumPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button));
            statusMed = 0;
        }

        if(statusLow == 0) {
            activity.getLowPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button_red));
            activity.getLowPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.white));
            statusLow = 1;

            priorStatus = Constant.TAG_LOW_PRIORITY;
        }
    }

    private void doingToMediumButton() {
        if(statusHigh != 0) {
            activity.getHighPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button));
            statusHigh = 0;
        }
        if(statusLow != 0) {
            activity.getLowPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button));
            activity.getLowPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.black));
            statusLow = 0;
        }

        if(statusMed == 0) {
            activity.getMediumPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button_yellow));
            statusMed = 1;

            priorStatus = Constant.TAG_MEDIUM_PRIORITY;
        }
    }

    private void doingToHighButton() {
        if(statusLow != 0) {
            activity.getLowPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button));
            activity.getLowPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.black));
            statusLow = 0;
        }
        if(statusMed != 0) {
            activity.getMediumPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button));
            statusMed = 0;
        }

        if(statusHigh == 0) {
            activity.getHighPrioritiesButton().setBackground(activity.getResources().getDrawable(R.drawable.custom_button_green));
            statusHigh = 1;

            priorStatus = Constant.TAG_HIGH_PRIORITY;
        }
    }

    public Dialog onCreateDialog(int id, Bundle args) {
        if(id == Constant.TAG_CALENDAR) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);

            return new DatePickerDialog(activity, datePickerDialog, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
            String da = "" + i2 + "-" + (i1 + 1) + "-" + i;
            activity.getSubmissionDateTextView().setText(simpleDateFormat.format(new Date(getTimeLong(da))));
        }
    };

    private void addTask() {
        if(activity.getTaskEditText().getText().toString().length() < 5) {
            activity.showToast(warningTask);
        }
        else if(activity.getSubmissionDateTextView().getText().equals(warningTapToSetTime)) {
            activity.showToast(warningSetTime);
        }
        else if(priorStatus == 0) {
            activity.showToast(warningSetPriority);
        }
        else {
            String task = activity.getTaskEditText().getText().toString();
            String submission = activity.getSubmissionDateTextView().getText().toString();
            int priority = priorStatus;

            TaskModel taskModel = setUpTaskModel(task, new Date(submission).getTime(), priority, Constant.TAG_UNFINISH_ADDTASK);

            int id = (int) new Storage(activity).addTransaction(taskModel);

            Intent intent = activity.getIntent();
            intent.putExtra(Constant.TAG_ADD_TASK_RESULT, new TaskModel(String.valueOf(id), taskModel.getTask(), taskModel.getTime(), 0, taskModel.getPriority(), taskModel.getStatus(), taskModel.getSubmission()));
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

    private TaskModel setUpTaskModel(String task, long submission, int priority, int status)  {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long time = calendar.getTime().getTime();

        TaskModel taskModel = new TaskModel();
        taskModel.setTask(task);
        taskModel.setTime(time);
        taskModel.setStatus(status);
        taskModel.setPriority(priority);
        taskModel.setSubmission(submission);

        return taskModel;
    }

    private long getTimeLong(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        try {
            Date d = dateFormat.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @OnClick(R.id.addtask_lowpriorities_button)
    public void lowPrioritiesClicked() {
        doingToLowButton();
    }

    @OnClick(R.id.addtask_mediumpriorities_button)
    public void mediumPrioritiesClicked() {
        doingToMediumButton();
    }

    @OnClick(R.id.addtask_highpriorities_button)
    public void highPrioritiesClicked() {
        doingToHighButton();
    }

    @OnClick(R.id.addtask_addtask_button)
    public void addTaskClicked() {
        addTask();
    }

    @OnTouch(R.id.addtask_lowpriorities_button)
    public boolean lowPrioritiesTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                activity.getLowPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.red));
                break;
            }
            case MotionEvent.ACTION_UP: {
                activity.getLowPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.black));
                break;
            }
        }
        return false;
    }

    @OnTouch(R.id.addtask_mediumpriorities_button)
    public boolean mediumPrioritiesButton(View v, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                activity.getMediumPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.yellow));
                break;
            }
            case MotionEvent.ACTION_UP: {
                activity.getMediumPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.black));
                break;
            }
        }
        return false;
    }

    @OnTouch(R.id.addtask_highpriorities_button)
    public boolean highPrioritiesButton(View v, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                activity.getHighPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.green));
                break;
            }
            case MotionEvent.ACTION_UP: {
                activity.getHighPrioritiesButton().setTextColor(ContextCompat.getColor(activity, R.color.black));
                break;
            }
        }
        return false;
    }
}