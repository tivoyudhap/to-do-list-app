package com.mastip.personaltodolist.screen.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.widget.LoginButton;
import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity;
import com.mastip.personaltodolist.screen.activity.controller.CLoginController;

import butterknife.BindView;

/**
 * Created by HateLogcatError on 8/29/2017.
 */

public class JLoginActivity extends MyBaseActivity {

    CLoginController controller;

    @BindView(R.id.main_username_edittext)
    EditText usernameEdittext;
    @BindView(R.id.main_password_edittext)
    EditText passwordEdittext;
    @BindView(R.id.login_login_button)
    Button buttonLogin;
    @BindView(R.id.login_facebooklogin_loginbutton)
    LoginButton buttonFacebookLogin;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initiateItem() {
        controller = new CLoginController(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controller.onActivityResult(requestCode, resultCode, data);
    }

    public EditText getPasswordEdittext() {
        return passwordEdittext;
    }

    public EditText getUsernameEdittext() {
        return usernameEdittext;
    }

    public LoginButton getButtonFacebookLogin() {
        return buttonFacebookLogin;
    }
}
