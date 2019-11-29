package com.mastip.personaltodolist.support.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.model.TaskModel;
import com.mastip.personaltodolist.utilities.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HateLogcatError on 8/29/2017.
 */

public class JTaskViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.adapter_card_cardview)
    CardView card;
    @BindView(R.id.adapter_task_textview)
    TextView textTask;
    @BindView(R.id.adapter_time_textview)
    TextView textTime;
    @BindView(R.id.adapter_finish_textview)
    TextView textFinish;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    public JTaskViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void setUpModelToUI(Context context, TaskModel taskData) {
        textTask.setText(taskData.getTask());
        textTime.setText(context.getString(R.string.added) + simpleDateFormat.format(new Date(taskData.getTime())));
        textFinish.setText(context.getString(R.string.submission) + simpleDateFormat.format(new Date(taskData.getSubmission())));

        if(taskData.getPriority() == Constant.TAG_LOW_PRIORITY) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_red));
        }
        else if(taskData.getPriority() == Constant.TAG_MEDIUM_PRIORITY) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_yellow));
        }
        else if(taskData.getPriority() == Constant.TAG_HIGH_PRIORITY) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_green));
        }
    }


}
