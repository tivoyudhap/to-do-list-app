package com.mastip.personaltodolist.screen.activity;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity;
import com.mastip.personaltodolist.screen.activity.controller.CSplashScreenController;

/**
 * Created by HateLogcatError on 8/29/2017.
 */

public class JSplashScreenActivity extends MyBaseActivity{

    CSplashScreenController controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public void initiateItem() {
        this.controller = new CSplashScreenController(this);
    }
}
