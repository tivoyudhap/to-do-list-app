package com.mastip.personaltodolist.screen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity;
import com.mastip.personaltodolist.screen.activity.controller.CMainMenuController;
import com.mastip.personaltodolist.utilities.Constant;

import butterknife.BindView;

/**
 * Created by HateLogcatError on 8/28/2017.
 */

public class JMainMenuActivity extends MyBaseActivity {

    @BindView(R.id.mainmenu_listtask_recyclerview)
    RecyclerView listTask;
    @BindView(R.id.toolbar_logout_imageview)
    ImageButton btnLogout;
    @BindView(R.id.mainmenu_fab_floatingactionbutton)
    FloatingActionButton floatingActionButton;

    CMainMenuController controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main_menu;
    }

    @Override
    public void initiateItem() {
        controller = new CMainMenuController(this);
    }

    public RecyclerView getListTask() {
        return listTask;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.onActivityResult(requestCode, resultCode, data);
    }
}
