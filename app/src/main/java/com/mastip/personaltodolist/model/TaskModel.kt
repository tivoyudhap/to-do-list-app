package com.mastip.personaltodolist.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel (
        var id: String = "",
        var task: String = "",
        var time: Long = 0,
        var finish: Long = 0,
        var priority: Int = 0,
        var status: Int = 0,
        var submission: Long = 0
): Parcelable