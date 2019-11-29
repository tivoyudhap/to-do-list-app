package com.mastip.personaltodolist.screen.activity.controller

import android.content.Intent
import android.os.Parcelable
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.internal.Mutable
import com.facebook.login.LoginManager
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.manager.database.Storage
import com.mastip.personaltodolist.model.GroupingTaskModel
import com.mastip.personaltodolist.model.TaskModel
import com.mastip.personaltodolist.screen.activity.JAddTaskActivity
import com.mastip.personaltodolist.screen.activity.JLoginActivity
import com.mastip.personaltodolist.screen.activity.JMainMenuActivity
import com.mastip.personaltodolist.support.adapter.TaskAdapter
import com.mastip.personaltodolist.utilities.Constant
import com.mastip.personaltodolist.utilities.CustomSharedPreferences
import com.onesignal.OneSignal
import java.text.SimpleDateFormat
import java.util.*

class CMainMenuController(var activity: JMainMenuActivity) {

    private var dataUnfinish: MutableList<TaskModel> = mutableListOf()
    private var dataFinish: MutableList<TaskModel> = mutableListOf()
    var groupTask: MutableList<GroupingTaskModel> = mutableListOf()
    var headerPosition: MutableList<Int> = mutableListOf()

    private var adapter: TaskAdapter? = null
    private var simpleDateFormat1 = SimpleDateFormat("dd MMMM yyyy", Locale.US)

    private fun oneSignalInit() {
        OneSignal.startInit(activity)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
    }

    private fun hideActionBar() {
        activity.supportActionBar?.hide()
    }

    private fun retrieveData() {
        dataUnfinish.clear()
        dataFinish.clear()
        dataUnfinish = Storage(activity).getAllData(Constant.TAG_UNFINISH_ADDTASK)
        dataFinish = Storage(activity).getAllData(Constant.TAG_FINISH_ADDTASK)
        classifyData()
        countGroupData
    }

    private fun setAdapter() {
        adapter = TaskAdapter(activity, groupTask)
        activity.listTask?.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        activity.listTask?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        activity.listTask?.adapter = adapter
    }

    private fun initTask() {
        retrieveData()
        setAdapter()
        val touchHelper: ItemTouchHelper.Callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, target: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val decreator = headerPosition[checkIndexForGroup(position)] + 1
                val groupingTaskModel = groupTask[checkIndexForGroup(position)]
                val taskModelList = groupingTaskModel.taskModel
                val taskModel = taskModelList[position - decreator]
                Storage(activity).disposeTransaction(taskModel.id)
                refreshUI(checkIndexForGroup(position), position - decreator)
            }

            override fun getSwipeDirs(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
                return if (viewHolder.itemViewType == Constant.VIEW_HEADER ||
                        viewHolder.itemViewType == Constant.VIEW_BODY_FINISH) {
                    0
                } else super.getSwipeDirs(recyclerView, viewHolder)
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelper)
        itemTouchHelper.attachToRecyclerView(activity.listTask)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.TAG_ACTIVITY_RESULT_ADDTASK && data != null) {
            val taskModel = data.extras.getParcelable<Parcelable>(Constant.TAG_ADD_TASK_RESULT) as TaskModel
            refreshUI()
        }
    }

    private fun refreshUI() {
        groupTask.clear()
        retrieveData()
        adapter?.notifyDataSetChanged()
    }

    private fun refreshUI(groupPosition: Int, listPosition: Int) {
        groupTask[groupPosition].taskModel[listPosition].finish = Date().time
        val taskModel = groupTask[groupPosition].taskModel[listPosition]
        if (groupTask[groupTask.size - 1].groupName == Constant.TAG_MY_TASK_FINISH) {
            groupTask[groupTask.size - 1].taskModel.add(0, taskModel)
        } else {
            val taskModels: MutableList<TaskModel> = ArrayList()
            taskModels.add(taskModel)
            groupTask.add(GroupingTaskModel(Constant.TAG_MY_TASK_FINISH, taskModels, ""))
        }
        groupTask[groupPosition].taskModel.removeAt(listPosition)
        if (groupTask[groupPosition].taskModel.size == 0) {
            groupTask.removeAt(groupPosition)
        }
        adapter?.notifyDataSetChanged()
    }

    @OnClick(R.id.mainmenu_fab_floatingactionbutton)
    fun onFabClicked() {
        val intent = Intent(activity, JAddTaskActivity::class.java)
        activity.startActivityForResult(intent, Constant.TAG_ACTIVITY_RESULT_ADDTASK)
    }

    @OnClick(R.id.toolbar_logout_imageview)
    fun onLogout() {
        CustomSharedPreferences.deleteDataString(Constant.TAG_USERNAME)
        LoginManager.getInstance().logOut()
        val intent = Intent(activity, JLoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    // form : date will be check - date today = result / interval day
// then, add to the result / interval day to date today
// example :
// 21 - 11 = 10 / 3 = 3 (rounded to down)
// 11 + (3 x 3) = 11 + 9 = 20;
// so 21 is estimated to be after 20th
    private fun classifyData() {
        val calendar1 = Calendar.getInstance()
        for (taskModel in dataUnfinish) {
            calendar1.time = Date()
            calendar1[Calendar.HOUR_OF_DAY] = 0
            calendar1[Calendar.MINUTE] = 0
            calendar1[Calendar.SECOND] = 0
            calendar1[Calendar.MILLISECOND] = 0
            val dateNow = calendar1.time
            val dateSubmission = Date(taskModel.submission)
            val taskModels: MutableList<TaskModel> = ArrayList()
            taskModels.add(taskModel)
            if (dateSubmission == dateNow || dateSubmission.after(dateNow)) { // not expired
                val difference = checkTimeDiffByDay(dateNow.time, dateSubmission.time).toInt()
                val dateFlagIncreaser = difference / Constant.FLAG_DATE_INCREMENT
                val increaser = dateFlagIncreaser * Constant.FLAG_DATE_INCREMENT
                calendar1.add(Calendar.DATE, increaser)
                if (groupTask.size > 0) {
                    for (a in groupTask.indices) {
                        val groupingTaskModel = groupTask[a]
                        // check if group list already exist
                        if (groupingTaskModel.date == simpleDateFormat1.format(calendar1.time)) {
                            groupTask[a].taskModel.add(taskModel)
                            break
                        } else { // if the group loop is last, it will create new list of group
                            if (a == groupTask.size - 1) {
                                groupTask.add(GroupingTaskModel(Constant.TAG_MY_TASK, taskModels, simpleDateFormat1.format(calendar1.time)))
                                break
                            }
                        }
                    }
                } else {
                    groupTask.add(GroupingTaskModel(Constant.TAG_MY_TASK, taskModels, simpleDateFormat1.format(calendar1.time)))
                }
            } else { // expired
                if (groupTask.size > 0) {
                    if (groupTask[0].groupName == Constant.TAG_EXPIRED) {
                        groupTask[0].taskModel.add(taskModel)
                    }
                } else {
                    groupTask.add(GroupingTaskModel(Constant.TAG_EXPIRED, taskModels, ""))
                }
            }
        }
        // also add finish data
        for (taskModel in dataFinish) {
            val taskModels: MutableList<TaskModel> = ArrayList()
            taskModels.add(taskModel)
            if (groupTask.size > 0) {
                val lastGroup = groupTask[groupTask.size - 1]
                if (lastGroup.groupName == Constant.TAG_MY_TASK_FINISH) {
                    lastGroup.taskModel.add(taskModel)
                } else {
                    groupTask.add(GroupingTaskModel(Constant.TAG_MY_TASK_FINISH, taskModels, ""))
                }
            } else {
                groupTask.add(GroupingTaskModel(Constant.TAG_MY_TASK_FINISH, taskModels, ""))
            }
        }
    }

    private fun checkTimeDiffByDay(date1: Long, date2: Long): Long {
        val MSPERDAY = 60 * 60 * 24 * 1000.toLong()
        return (Date(date2).time - Date(date1).time) / MSPERDAY
    }

    private val countGroupData: Int
        private get() {
            headerPosition.clear()
            var positionCount = 0
            for (groupingTaskModel in groupTask) {
                headerPosition.add(positionCount)
                positionCount = positionCount + groupingTaskModel.taskModel.size + 1
            }
            return positionCount
        }

    private fun checkIndexForGroup(position: Int): Int {
        val positionCount = 0
        for (a in groupTask.indices) {
            val zeroIndexed = groupTask[0]
            if (position < zeroIndexed.taskModel.size + 1) {
                return positionCount
            } else if (position > zeroIndexed.taskModel.size && position < beforeLastPosition) {
                for (b in 1 until headerPosition.size - 1) {
                    val headerIndex = headerPosition[b]
                    val headerIndexAfter = headerPosition[b + 1]
                    if (position in headerIndex until headerIndexAfter) {
                        return b
                    }
                }
            } else {
                return groupTask.size - 1
            }
        }
        return positionCount
    }

    //    private void initDate() {
    private val beforeLastPosition: Int
        private get() = headerPosition[groupTask.size - 1]

    init {
        ButterKnife.bind(this, activity)
        oneSignalInit()
        hideActionBar()
        initTask()
    }
}