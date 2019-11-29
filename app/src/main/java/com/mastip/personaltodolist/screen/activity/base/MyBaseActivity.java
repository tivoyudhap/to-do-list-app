package com.mastip.personaltodolist.screen.activity.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mastip.personaltodolist.utilities.Constant;
import com.mastip.personaltodolist.utilities.CustomSharedPreferences;

import butterknife.ButterKnife;

/**
 * Created by HateLogcatError on 8/28/2017.
 */

public abstract class MyBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        new CustomSharedPreferences().setSharedPreferences(getSharedPreferences(Constant.TAG_SHARED_PREFERENCE, MODE_PRIVATE));
        initiateItem();
    }

    public void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    public abstract int getContentViewId();
    public abstract void initiateItem();
}
