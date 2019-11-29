package com.mastip.personaltodolist.screen.activity

import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import butterknife.BindView
import com.mastip.personaltodolist.R
import com.mastip.personaltodolist.screen.activity.base.MyBaseActivity
import com.mastip.personaltodolist.screen.activity.controller.CMainMenuController

/**
 * Created by HateLogcatError on 8/28/2017.
 */
class JMainMenuActivity : MyBaseActivity() {

    @BindView(R.id.mainmenu_listtask_recyclerview)
    lateinit var listTask: androidx.recyclerview.widget.RecyclerView

    @BindView(R.id.toolbar_logout_imageview)
    lateinit var btnLogout: ImageButton

    @BindView(R.id.mainmenu_fab_floatingactionbutton)
    lateinit var floatingActionButton: FloatingActionButton

    lateinit var controller: CMainMenuController

    override val contentViewId: Int
        get() = R.layout.activity_main_menu

    override fun initiateItem() {
        controller = CMainMenuController(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.onActivityResult(requestCode, resultCode, data)
    }
}