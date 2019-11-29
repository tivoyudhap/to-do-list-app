package com.mastip.personaltodolist.screen.activity.controller;

import android.content.Intent;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mastip.personaltodolist.R;
import com.mastip.personaltodolist.manager.database.Storage;
import com.mastip.personaltodolist.screen.activity.JLoginActivity;
import com.mastip.personaltodolist.screen.activity.JMainMenuActivity;
import com.mastip.personaltodolist.utilities.Constant;
import com.mastip.personaltodolist.utilities.CustomSharedPreferences;
import com.onesignal.OneSignal;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HateLogcatError on 8/29/2017.
 */

public class CLoginController {

    @BindString(R.string.username_must_longer_than_5) String warningValidateUsername;
    @BindString(R.string.password_must_longer_than_5) String warningValidatePassword;
    @BindString(R.string.user_not_recorded) String userNotRecorded;

    private CallbackManager callbackManager;

    private JLoginActivity activity;

    private String username = "";
    private String password = "";

    public CLoginController(final JLoginActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        checkIfSessionIsAvailable();
        facebookInit();
        oneSignalInit();
    }

    private void oneSignalInit() {
        OneSignal.startInit(activity)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void facebookInit() {
        callbackManager = CallbackManager.Factory.create();
        activity.getButtonFacebookLogin().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                username = loginResult.getAccessToken().getUserId();
                password = "";

                doSearchToDB();
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, "Login Cancelled by User", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(activity, "Facebook error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doLogin() {
        if(validateLogin()) {
            doSearchToDB();
        }
    }

    private void doSearchToDB() {
        if(new Storage(activity).checkIfTheUserIsAvailable(username)) {
            if(new Storage(activity).checkPasswordForTheUser(username, password)) {
                Intent intent = new Intent(activity, JMainMenuActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
            else {
                activity.showToast(userNotRecorded);
            }
        }
        else {
            new Storage(activity).createUser(username, password);

            new CustomSharedPreferences().putDataString(Constant.TAG_USERNAME, username);

            Intent intent = new Intent(activity, JMainMenuActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    @OnClick(R.id.login_login_button)
    public void loginClicked() {
        doLogin();
    }

    private void getLoginData() {
        username = activity.getUsernameEdittext().getText().toString();
        password = activity.getPasswordEdittext().getText().toString();
    }

    private boolean validateLogin() {
        getLoginData();

        if(username.length() < 5) {
            activity.showToast(warningValidateUsername);
            return false;
        }
        else if(password.length() < 5) {
            activity.showToast(warningValidatePassword);
            return false;
        }
        else {
            return true;
        }
    }

    private void checkIfSessionIsAvailable() {
        if(new CustomSharedPreferences().getDataString(Constant.TAG_USERNAME).equals("")) {

        }
        else {
            Intent intent = new Intent(activity, JMainMenuActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
