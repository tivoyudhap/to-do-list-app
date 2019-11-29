package com.mastip.personaltodolist.screen.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity
import com.mastip.personaltodolist.screen.activity.controller.CAddTaskController
import com.mastip.personaltodolist.utilities.Constant

class JAddTaskActivity : MyBaseActivity() {

    lateinit var controller: CAddTaskController

    @BindView(R.id.addtask_lowpriorities_button)
    lateinit var lowPrioritiesButton: Button

    @BindView(R.id.addtask_mediumpriorities_button)
    lateinit var mediumPrioritiesButton: Button

    @BindView(R.id.addtask_highpriorities_button)
    lateinit var highPrioritiesButton: Button

    @BindView(R.id.addtask_submission_relativelayout)
    lateinit var submissionLayout: RelativeLayout

    @BindView(R.id.addtask_submissiondate_textview)
    lateinit var submissionDateTextView: TextView

    @BindView(R.id.addtask_task_edittext)
    lateinit var taskEditText: EditText

    @BindView(R.id.addtask_addtask_button)
    lateinit var addTaskButton: Button

    override val contentViewId: Int
        get() = R.layout.activity_add_task

    override fun initiateItem() {
        controller = CAddTaskController(this)
    }

    override fun onCreateDialog(id: Int, args: Bundle): Dialog? {
        return controller.onCreateDialog(id, args)
    }

    @OnClick(R.id.addtask_submission_relativelayout)
    fun setDate() {
        showDialog(Constant.TAG_CALENDAR)
    }
}