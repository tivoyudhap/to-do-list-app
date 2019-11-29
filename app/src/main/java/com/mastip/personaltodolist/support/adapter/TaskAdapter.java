package com.mastip.personaltodolist.support.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mastip.personaltodolist.model.GroupingTaskModel;
import com.mastip.personaltodolist.model.TaskModel;
import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.support.adapter.viewholder.JFinishViewHolder;
import com.mastip.personaltodolist.support.adapter.viewholder.JHeaderViewHolder;
import com.mastip.personaltodolist.support.adapter.viewholder.JTaskViewHolder;
import com.mastip.personaltodolist.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HateLogcatError on 8/25/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<GroupingTaskModel> groupTask = new ArrayList<>();
    ArrayList<Integer> headerPosition = new ArrayList<>();

    private Context context;

    public TaskAdapter(Context context, ArrayList<GroupingTaskModel> groupTask) {
        this.context = context;
        this.groupTask = groupTask;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if(viewType == Constant.VIEW_HEADER) {
            v = LayoutInflater.from(context).inflate(R.layout.adapter_header_task, null);
            return new JHeaderViewHolder(v);
        }
        else if(viewType == Constant.VIEW_BODY_UNFINISH) {
            v = LayoutInflater.from(context).inflate(R.layout.adapter_task, null);
            return new JTaskViewHolder(v);
        }
        else {
            v = LayoutInflater.from(context).inflate(R.layout.adapter_task, null);
            return new JFinishViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int lastGroupPosition = headerPosition.get(headerPosition.size() - 1);

        int pos = position;
        for(GroupingTaskModel groupingTaskModel: groupTask) {
            if(pos == 0) {
                return Constant.VIEW_HEADER;
            }
            else if(pos <= groupingTaskModel.getTaskModel().size()) {
                if(position > lastGroupPosition && isHaveFinishData()) {
                    return Constant.VIEW_BODY_FINISH;
                }
                else {
                    return Constant.VIEW_BODY_UNFINISH;
                }
            }
            else {
                pos -= (groupingTaskModel.getTaskModel().size() + 1);
            }
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        int pos = position;
        for(GroupingTaskModel groupingTaskModel: groupTask) {
            List<TaskModel> listModel = groupingTaskModel.getTaskModel();

            if(pos == 0) {
                if(holder instanceof JHeaderViewHolder) {
                    ((JHeaderViewHolder) holder).setUpModelToUI(groupingTaskModel);
                }
                break;
            }
            else if(pos <= groupingTaskModel.getTaskModel().size()) {
                pos -= 1;

                TaskModel taskModel = listModel.get(pos);

                if(holder instanceof JTaskViewHolder) {
                    ((JTaskViewHolder) holder).setUpModelToUI(context, taskModel);
                }
                else if(holder instanceof JFinishViewHolder) {
                    ((JFinishViewHolder) holder).setUpModelToUI(context, taskModel);
                }
                break;
            }
            else {
                pos -= (groupingTaskModel.getTaskModel().size() + 1);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getCountGroupData();
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

    private boolean isHaveFinishData() {
        return groupTask.get(groupTask.size() - 1).getGroupName().equals(Constant.TAG_MY_TASK_FINISH);
    }
}
