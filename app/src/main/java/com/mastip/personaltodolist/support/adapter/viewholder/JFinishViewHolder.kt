package com.mastip.personaltodolist.support.adapter.viewholder

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.model.TaskModel
import java.text.SimpleDateFormat
import java.util.*

class JFinishViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    @BindView(R.id.adapter_card_cardview)
    lateinit var card: CardView

    @BindView(R.id.adapter_task_textview)
    lateinit var textTask: TextView

    @BindView(R.id.adapter_time_textview)
    lateinit var textTime: TextView

    @BindView(R.id.adapter_finish_textview)
    lateinit var textFinish: TextView


    fun setUpModelToUI(context: Context, taskData: TaskModel) {
        textTask.text = taskData.task
        textTime.text = "${context.resources.getString(R.string.added)}${SimpleDateFormat("dd MMMM yyyy", Locale.US).format(Date(taskData.time))}"
        textFinish.text = "${context.resources.getString(R.string.finish)}${SimpleDateFormat("dd MMMM yyyy", Locale.US).format(Date(taskData.finish))}"
    }

    init {
        ButterKnife.bind(this, v)
    }
}