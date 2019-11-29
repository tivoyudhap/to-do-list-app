package com.mastip.personaltodolist.screen.activity

import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity
import com.mastip.personaltodolist.screen.activity.controller.CSplashScreenController

class JSplashScreenActivity : MyBaseActivity() {

    var controller: CSplashScreenController? = null

    override val contentViewId: Int
        get() = R.layout.activity_splash_screen

    override fun initiateItem() {
        controller = CSplashScreenController(this)
    }
}