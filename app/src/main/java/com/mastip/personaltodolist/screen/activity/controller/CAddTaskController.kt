package com.mastip.personaltodolist.screen.activity.controller

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import butterknife.BindString
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTouch
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.manager.database.Storage
import com.mastip.personaltodolist.model.TaskModel
import com.mastip.personaltodolist.screen.activity.JAddTaskActivity
import com.mastip.personaltodolist.utilities.Constant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CAddTaskController(private val activity: JAddTaskActivity) {

    private var statusLow = 0
    private var statusMed = 0
    private var statusHigh = 0
    private var priorStatus = 0

    @BindString(R.string.task_must_longer_than_5)
    lateinit var warningTask: String

    @BindString(R.string.please_set_the_time)
    lateinit var warningSetTime: String

    @BindString(R.string.tap_here_to_set_time)
    lateinit var warningTapToSetTime: String

    @BindString(R.string.please_set_priority)
    lateinit var warningSetPriority: String

    private fun doingToLowButton() {
        if (statusHigh != 0) {
            activity.highPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button)
            statusHigh = 0
        }
        if (statusMed != 0) {
            activity.mediumPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button)
            statusMed = 0
        }
        if (statusLow == 0) {
            activity.lowPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button_red)
            activity.lowPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.white))
            statusLow = 1
            priorStatus = Constant.TAG_LOW_PRIORITY
        }
    }

    private fun doingToMediumButton() {
        if (statusHigh != 0) {
            activity.highPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button)
            statusHigh = 0
        }
        if (statusLow != 0) {
            activity.lowPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button)
            activity.lowPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.black))
            statusLow = 0
        }
        if (statusMed == 0) {
            activity.mediumPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button_yellow)
            statusMed = 1
            priorStatus = Constant.TAG_MEDIUM_PRIORITY
        }
    }

    private fun doingToHighButton() {
        if (statusLow != 0) {
            activity.lowPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button)
            activity.lowPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.black))
            statusLow = 0
        }
        if (statusMed != 0) {
            activity.mediumPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button)
            statusMed = 0
        }
        if (statusHigh == 0) {
            activity.highPrioritiesButton?.background = activity.resources.getDrawable(R.drawable.custom_button_green)
            statusHigh = 1
            priorStatus = Constant.TAG_HIGH_PRIORITY
        }
    }

    private fun addTask() {
        when {
            activity.taskEditText?.text.toString().length < 5 -> {
                activity.showToast(warningTask)
            }
            activity.submissionDateTextView.text == warningTapToSetTime -> {
                activity.showToast(warningSetTime)
            }
            priorStatus == 0 -> {
                activity.showToast(warningSetPriority)
            }
            else -> {
                val task = activity.taskEditText.text.toString()
                val submission = activity.submissionDateTextView.text.toString()
                val priority = priorStatus
                val taskModel = setUpTaskModel(task, Date(submission).time, priority, Constant.TAG_UNFINISH_ADDTASK)
                val id = Storage(activity).addTransaction(taskModel).toInt()
                val intent = activity.intent
                intent.putExtra(Constant.TAG_ADD_TASK_RESULT, TaskModel(id.toString(), taskModel.task, taskModel.time, 0, taskModel.priority, taskModel.status, taskModel.submission))
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            }
        }
    }

    private fun setUpTaskModel(task: String, submission: Long, priority: Int, status: Int): TaskModel {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar[Calendar.HOUR] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val time = calendar.time.time
        val taskModel = TaskModel()
        taskModel.task = task
        taskModel.time = time
        taskModel.status = status
        taskModel.priority = priority
        taskModel.submission = submission
        return taskModel
    }

    @OnClick(R.id.addtask_lowpriorities_button)
    fun lowPrioritiesClicked() {
        doingToLowButton()
    }

    @OnClick(R.id.addtask_mediumpriorities_button)
    fun mediumPrioritiesClicked() {
        doingToMediumButton()
    }

    @OnClick(R.id.addtask_highpriorities_button)
    fun highPrioritiesClicked() {
        doingToHighButton()
    }

    @OnClick(R.id.addtask_addtask_button)
    fun addTaskClicked() {
        addTask()
    }

    @OnTouch(R.id.addtask_lowpriorities_button)
    fun lowPrioritiesTouch(view: View?, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                activity.lowPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.red))
            }
            MotionEvent.ACTION_UP -> {
                activity.lowPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.black))
            }
        }
        return false
    }

    @OnTouch(R.id.addtask_mediumpriorities_button)
    fun mediumPrioritiesButton(v: View?, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                activity.mediumPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.yellow))
            }
            MotionEvent.ACTION_UP -> {
                activity.mediumPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.black))
            }
        }
        return false
    }

    @OnTouch(R.id.addtask_highpriorities_button)
    fun highPrioritiesButton(v: View?, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                activity.highPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.green))
            }
            MotionEvent.ACTION_UP -> {
                activity.highPrioritiesButton?.setTextColor(ContextCompat.getColor(activity, R.color.black))
            }
        }
        return false
    }

    init {
        ButterKnife.bind(this, activity)
    }
}