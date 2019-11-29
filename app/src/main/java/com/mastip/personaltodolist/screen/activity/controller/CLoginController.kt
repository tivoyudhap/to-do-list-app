package com.mastip.personaltodolist.screen.activity.controller

import android.content.Intent
import android.widget.Toast
import butterknife.BindString
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.manager.database.Storage
import com.mastip.personaltodolist.screen.activity.JLoginActivity
import com.mastip.personaltodolist.screen.activity.JMainMenuActivity
import com.mastip.personaltodolist.utilities.Constant
import com.mastip.personaltodolist.utilities.CustomSharedPreferences
import com.onesignal.OneSignal

class CLoginController(private val activity: JLoginActivity) {

    @BindString(R.string.username_must_longer_than_5)
    lateinit var warningValidateUsername: String

    @BindString(R.string.password_must_longer_than_5)
    lateinit var warningValidatePassword: String

    @BindString(R.string.user_not_recorded)
    lateinit var userNotRecorded: String

    private var callbackManager: CallbackManager? = null
    private var username = ""
    private var password = ""

    private fun oneSignalInit() {
        OneSignal.startInit(activity)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
    }

    private fun facebookInit() {
        callbackManager = CallbackManager.Factory.create()
        activity.buttonFacebookLogin!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                username = loginResult.accessToken.userId
                password = ""
                doSearchToDB()
            }

            override fun onCancel() {
                Toast.makeText(activity, "Login Cancelled by User", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(activity, "Facebook error : " + error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun doLogin() {
        if (validateLogin()) {
            doSearchToDB()
        }
    }

    private fun doSearchToDB() {
        if (Storage(activity).checkIfTheUserIsAvailable(username)) {
            if (Storage(activity).checkPasswordForTheUser(username, password)) {
                val intent = Intent(activity, JMainMenuActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            } else {
                activity.showToast(userNotRecorded)
            }
        } else {
            Storage(activity).createUser(username, password)
            CustomSharedPreferences.putDataString(Constant.TAG_USERNAME, username)
            val intent = Intent(activity, JMainMenuActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    @OnClick(R.id.login_login_button)
    fun loginClicked() {
        doLogin()
    }

    private val loginData: Unit
        get() {
            username = activity.usernameEdittext?.text.toString()
            password = activity.passwordEdittext?.text.toString()
        }

    private fun validateLogin(): Boolean {
        loginData
        return if (username.length < 5) {
            activity.showToast(warningValidateUsername)
            false
        } else if (password.length < 5) {
            activity.showToast(warningValidatePassword)
            false
        } else {
            true
        }
    }

    private fun checkIfSessionIsAvailable() {
        if (CustomSharedPreferences.getDataString(Constant.TAG_USERNAME).isNotEmpty()) {
            val intent = Intent(activity, JMainMenuActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    init {
        ButterKnife.bind(this, activity)
        checkIfSessionIsAvailable()
        facebookInit()
        oneSignalInit()
    }
}