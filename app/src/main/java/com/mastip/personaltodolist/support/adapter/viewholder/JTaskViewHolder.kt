package com.mastip.personaltodolist.support.adapter.viewholder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.model.TaskModel
import com.mastip.personaltodolist.utilities.Constant
import java.text.SimpleDateFormat
import java.util.*

class JTaskViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    
    @BindView(R.id.adapter_card_cardview)
    lateinit var card: androidx.cardview.widget.CardView
    
    @BindView(R.id.adapter_task_textview)
    lateinit var textTask: TextView
    
    @BindView(R.id.adapter_time_textview)
    lateinit var textTime: TextView
    
    @BindView(R.id.adapter_finish_textview)
    lateinit var textFinish: TextView
    
    var simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
    
    fun setUpModelToUI(context: Context, taskData: TaskModel) {
        textTask.text = taskData.task
        textTime.text = context.getString(R.string.added) + simpleDateFormat.format(Date(taskData.time))
        textFinish.text = context.getString(R.string.submission) + simpleDateFormat.format(Date(taskData.submission))
        when (taskData.priority) {
            Constant.TAG_LOW_PRIORITY -> {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_red))
            }
            Constant.TAG_MEDIUM_PRIORITY -> {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_yellow))
            }
            Constant.TAG_HIGH_PRIORITY -> {
                card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_green))
            }
        }
    }

    init {
        ButterKnife.bind(this, itemView)
    }
}