package com.mastip.personaltodolist.screen.activity.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import butterknife.ButterKnife
import com.mastip.personaltodolist.utilities.Constant
import com.mastip.personaltodolist.utilities.CustomSharedPreferences

abstract class MyBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewId)
        ButterKnife.bind(this)
        CustomSharedPreferences.sharedPreferences = getSharedPreferences(Constant.TAG_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        initiateItem()
    }

    fun showToast(message: String) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show()
    }

    abstract val contentViewId: Int
    abstract fun initiateItem()
}