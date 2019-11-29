package com.mastip.personaltodolist.support.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.model.GroupingTaskModel
import com.mastip.personaltodolist.support.adapter.viewholder.JFinishViewHolder
import com.mastip.personaltodolist.support.adapter.viewholder.JHeaderViewHolder
import com.mastip.personaltodolist.support.adapter.viewholder.JTaskViewHolder
import com.mastip.personaltodolist.utilities.Constant
import java.util.*

class TaskAdapter(private val context: Context, private val groupTask: MutableList<GroupingTaskModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var headerPosition = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val v: View
        return when (viewType) {
            Constant.VIEW_HEADER -> {
                v = LayoutInflater.from(context).inflate(R.layout.adapter_header_task, parent, false)
                JHeaderViewHolder(v)
            }
            Constant.VIEW_BODY_UNFINISH -> {
                v = LayoutInflater.from(context).inflate(R.layout.adapter_task, parent, false)
                JTaskViewHolder(v)
            }
            else -> {
                v = LayoutInflater.from(context).inflate(R.layout.adapter_task, parent, false)
                JFinishViewHolder(v)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val lastGroupPosition = headerPosition[headerPosition.size - 1]
        var pos = position
        for (groupingTaskModel in groupTask) {
            pos -= if (pos == 0) {
                return Constant.VIEW_HEADER
            } else if (pos <= groupingTaskModel.taskModel.size) {
                return if (position > lastGroupPosition && isHaveFinishData) {
                    Constant.VIEW_BODY_FINISH
                } else {
                    Constant.VIEW_BODY_UNFINISH
                }
            } else {
                groupingTaskModel.taskModel.size + 1
            }
        }
        return 0
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        var pos = position
        for (groupingTaskModel in groupTask) {
            val listModel = groupingTaskModel.taskModel
            if (pos == 0) {
                if (holder is JHeaderViewHolder) {
                    holder.setUpModelToUI(groupingTaskModel)
                }
                break
            } else if (pos <= groupingTaskModel.taskModel.size) {
                pos -= 1
                val taskModel = listModel[pos]
                if (holder is JTaskViewHolder) {
                    holder.setUpModelToUI(context, taskModel)
                } else if (holder is JFinishViewHolder) {
                    holder.setUpModelToUI(context, taskModel)
                }
                break
            } else {
                pos -= groupingTaskModel.taskModel.size + 1
            }
        }
    }

    override fun getItemCount(): Int {
        return countGroupData
    }

    private val countGroupData: Int
        get() {
            headerPosition.clear()
            var positionCount = 0
            for (groupingTaskModel in groupTask) {
                headerPosition.add(positionCount)
                positionCount += groupingTaskModel.taskModel.size + 1
            }
            return positionCount
        }

    private val isHaveFinishData: Boolean
        get() = groupTask[groupTask.size - 1].groupName == Constant.TAG_MY_TASK_FINISH
}