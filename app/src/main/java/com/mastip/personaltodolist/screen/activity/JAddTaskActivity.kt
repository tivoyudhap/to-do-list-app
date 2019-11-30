package com.mastip.personaltodolist.screen.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity
import com.mastip.personaltodolist.screen.activity.controller.CAddTaskController
import com.mastip.personaltodolist.utilities.Constant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class JAddTaskActivity : MyBaseActivity(), View.OnClickListener {

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

    var simpleDateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.US)

    override val contentViewId: Int
        get() = R.layout.activity_add_task

    override fun initiateItem() {
        controller = CAddTaskController(this)
        submissionLayout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addtask_submission_relativelayout -> DatePickerDialog(this, 0, DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth ->

                val da = "" + dayOfMonth + "-" + (month + 1) + "-" + year
                submissionDateTextView.text = simpleDateFormat.format(Date(getTimeLong(da)))
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE)).show()
        }
    }

    private fun getTimeLong(date: String): Long {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        try {
            val d = dateFormat.parse(date)
            return d.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    @OnClick(R.id.addtask_submission_relativelayout)
    fun setDate() {
        DatePickerDialog(this)
        showDialog(Constant.TAG_CALENDAR)
    }
}