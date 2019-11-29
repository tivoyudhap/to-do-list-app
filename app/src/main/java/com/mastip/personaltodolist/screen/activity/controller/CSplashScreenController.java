package com.mastip.personaltodolist.screen.activity.controller;

import android.content.Intent;
import android.support.v7.app.ActionBar;

import com.mastip.personaltodolist.screen.activity.JLoginActivity;
import com.mastip.personaltodolist.screen.activity.JSplashScreenActivity;

/**
 * Created by HateLogcatError on 8/29/2017.
 */

public class CSplashScreenController {

    JSplashScreenActivity activity;

    public CSplashScreenController(JSplashScreenActivity activity) {
        this.activity = activity;

        hideActionBar();
        countdownToNextActivity();
    }

    private void hideActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.hide();
    }

    private void countdownToNextActivity() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(activity, JLoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });
        thread.start();
    }
}
