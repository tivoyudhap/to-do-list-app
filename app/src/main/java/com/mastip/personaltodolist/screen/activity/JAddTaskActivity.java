package com.mastip.personaltodolist.screen.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity;
import com.mastip.personaltodolist.screen.activity.controller.CAddTaskController;
import com.mastip.personaltodolist.utilities.Constant;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by HateLogcatError on 8/28/2017.
 */

public class JAddTaskActivity extends MyBaseActivity {

    CAddTaskController controller;

    @BindView(R.id.addtask_lowpriorities_button)
    Button lowPrioritiesButton;
    @BindView(R.id.addtask_mediumpriorities_button)
    Button mediumPrioritiesButton;
    @BindView(R.id.addtask_highpriorities_button)
    Button highPrioritiesButton;
    @BindView(R.id.addtask_submission_relativelayout)
    RelativeLayout submissionLayout;
    @BindView(R.id.addtask_submissiondate_textview)
    TextView submissionDateTextView;
    @BindView(R.id.addtask_task_edittext)
    EditText taskEditText;
    @BindView(R.id.addtask_addtask_button)
    Button addTaskButton;

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_task;
    }

    @Override
    public void initiateItem() {
        controller = new CAddTaskController(this);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        if(controller != null) {
            return controller.onCreateDialog(id, args);
        }
        return null;
    }

    @OnClick(R.id.addtask_submission_relativelayout)
    public void setDate() {
        showDialog(Constant.TAG_CALENDAR);
    }

    public TextView getSubmissionDateTextView() {
        return submissionDateTextView;
    }

    public EditText getTaskEditText() {
        return taskEditText;
    }

    public Button getLowPrioritiesButton() {
        return lowPrioritiesButton;
    }

    public Button getMediumPrioritiesButton() {
        return mediumPrioritiesButton;
    }

    public Button getHighPrioritiesButton() {
        return highPrioritiesButton;
    }
}
