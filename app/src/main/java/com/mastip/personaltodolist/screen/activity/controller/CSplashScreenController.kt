package com.mastip.personaltodolist.screen.activity.controller

import android.content.Intent
import com.mastip.personaltodolist.screen.activity.JLoginActivity
import com.mastip.personaltodolist.screen.activity.JSplashScreenActivity

class CSplashScreenController(var activity: JSplashScreenActivity) {

    private fun hideActionBar() {
        activity.supportActionBar?.hide()
    }

    private fun countdownToNextActivity() {
        val thread = Thread(Runnable {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                val intent = Intent(activity, JLoginActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        })
        thread.start()
    }

    init {
        hideActionBar()
        countdownToNextActivity()
    }
}