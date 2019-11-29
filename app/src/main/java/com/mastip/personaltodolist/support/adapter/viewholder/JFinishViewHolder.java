package com.mastip.personaltodolist.support.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HateLogcatError on 8/28/2017.
 */

public class JFinishViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.adapter_card_cardview)
    CardView card;
    @BindView(R.id.adapter_task_textview)
    TextView textTask;
    @BindView(R.id.adapter_time_textview)
    TextView textTime;
    @BindView(R.id.adapter_finish_textview)
    TextView textFinish;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    public JFinishViewHolder(View v) {
        super(v);

        ButterKnife.bind(this, v);
    }

    public void setUpModelToUI(Context context, TaskModel taskData) {
        textTask.setText(taskData.getTask());
        textTime.setText(context.getResources().getString(R.string.added) + simpleDateFormat.format(new Date(taskData.getTime())));
        textFinish.setText(context.getResources().getString(R.string.finish) + simpleDateFormat.format(new Date(taskData.getFinish())));
    }
}
