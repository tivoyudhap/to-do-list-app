package com.mastip.personaltodolist.support.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.model.GroupingTaskModel;
import com.mastip.personaltodolist.model.TaskModel;
import com.mastip.personaltodolist.utilities.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HateLogcatError on 8/29/2017.
 */

public class JHeaderViewHolder extends RecyclerView.ViewHolder {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    @BindView(R.id.mainmenu_taskname_textview)
    TextView textTaskName;

    public JHeaderViewHolder(View v) {
        super(v);

        ButterKnife.bind(this, v);
    }

    public void setUpModelToUI(GroupingTaskModel groupingTaskModel) {
        if(groupingTaskModel.getDate().equals("")) {
            textTaskName.setText(groupingTaskModel.getGroupName());
        }
        else {
            textTaskName.setText(groupingTaskModel.getGroupName() + simpleDateFormat.format(new Date(groupingTaskModel.getDate())));
        }
    }
}
