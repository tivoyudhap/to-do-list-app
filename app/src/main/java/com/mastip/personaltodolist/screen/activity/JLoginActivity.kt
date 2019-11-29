package com.mastip.personaltodolist.screen.activity

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import butterknife.BindView
import com.facebook.login.widget.LoginButton
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity
import com.mastip.personaltodolist.screen.activity.controller.CLoginController

class JLoginActivity : MyBaseActivity() {

    lateinit var controller: CLoginController

    @BindView(R.id.main_username_edittext)
    lateinit var usernameEdittext: EditText

    @BindView(R.id.main_password_edittext)
    lateinit var passwordEdittext: EditText

    @BindView(R.id.login_login_button)
    lateinit var buttonLogin: Button

    @BindView(R.id.login_facebooklogin_loginbutton)
    lateinit var buttonFacebookLogin: LoginButton

    override val contentViewId: Int
        get() = R.layout.activity_login

    override fun initiateItem() {
        controller = CLoginController(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.onActivityResult(requestCode, resultCode, data)
    }

}