package com.mastip.personaltodolist.support.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.model.GroupingTaskModel
import java.text.SimpleDateFormat
import java.util.*

class JHeaderViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

    var simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)

    @BindView(R.id.mainmenu_taskname_textview)
    lateinit var textTaskName: TextView

    fun setUpModelToUI(groupingTaskModel: GroupingTaskModel) {
        if (groupingTaskModel.date == "") {
            textTaskName.text = groupingTaskModel.groupName
        } else {
            textTaskName.text = groupingTaskModel.groupName + simpleDateFormat.format(Date(groupingTaskModel.date))
        }
    }

    init {
        ButterKnife.bind(this, v)
    }
}