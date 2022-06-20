package com.user.example

import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.biotech.framework.dialog.NormalDialog
import com.biotech.framework.interfaces.DialogButtonListener
import com.biotech.framework.template.BaseActivity
import com.biotech.framework.util.CheckPermission
import com.biotech.framework.util.DialogUtil
import com.google.android.material.navigation.NavigationView
import com.user.example.database.AppDataBase
import com.user.example.fragment.*
import kotlinx.android.synthetic.main.activity_navi.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*


class NaviActivity : BaseActivity(R.layout.activity_navi), NavigationView.OnNavigationItemSelectedListener {

    val app = StartApplication.instance
    val header by lazy { nav_view.getHeaderView(0) }
    val nvi_text by lazy { header.findViewById(R.id.nvi_text)as TextView }
    val navi_title by lazy { header.findViewById(R.id.nvi_title) as TextView }

    override fun initView() {
        setContentView(R.layout.activity_navi)
        nav_view.inflateHeaderView(R.layout.nav_header_main)
        setSupportActionBar(toolbar)


        if(CheckPermission().RequestPermission(this)){
            //權限開啟進入下一頁
        }

        if (nav_view.headerCount > 0) {

            nvi_text.text = "狀態 : 未登入"
            this.intent.extras?.let { b ->
                if (b.containsKey("account")) {
                    navi_title.text = b.getString("account")
                    nvi_text.text = "狀態 :  已登入"
                    nav_view.menu.setGroupVisible(R.id.nav_group, true)
                    val nav_login = nav_view.menu.findItem(R.id.nav_login)
                    nav_login.title = "登出"
                }
                navi_title.text = if (b.containsKey("account")) b.getString("account") else ""
            }
        }

    }

    override fun initEvent() {

        ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ).run {
            drawer_layout.addDrawerListener(this)
            syncState()
        }

        nav_view.setNavigationItemSelectedListener(this)


    }


    override fun onClick(p0: View?) {

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_login -> {
//                showFragment(LoginFragment.getNewInstance("登入"))
//            }
//            R.id.action_settings -> {
//
//
//            }
//        }
            return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
//        this.intent.extras?.let { b ->
//           v nvi_text.text == b.getString("account")
//        }
        when (item.itemId) {
            R.id.nav_login -> {
                if (nvi_text.text == "狀態 :  已登入") {

                        DialogUtil(this).show("登出", "是否確認要登出?", true, object : DialogButtonListener {
                            override val showPositiveButton: Boolean = true

                            override val positiveTitle: String? = "是"

                            override fun positiveClick(dialog: AlertDialog) {
                                showFragment(LoginFragment.getNewInstance("登入"))
                                val nav_login = nav_view.menu.findItem(R.id.nav_login)
                                navi_title.text = ""
                                nav_login.title = "登入"
                                nvi_text.text = "狀態 : 未登入"

                                nav_view.menu.setGroupVisible(R.id.nav_group, false)
                            }

                            override val showNegativeButton: Boolean = true
                            override val negativeTitle: String? = "否"
                            override fun negativeClick(dialog: AlertDialog) {

                            }

                            override val showNeutralButton: Boolean = false
                            override val neutralTitle: String? = null
                            override fun neutralClick(dialog: AlertDialog) {
                            }

                        })
                } else {
                    showFragment(LoginFragment.getNewInstance("登入"))
//                    DialogUtil(this).show("登入", "是否確認要登入?", true, object : DialogButtonListener {
//                        override val showPositiveButton: Boolean = true
//
//                        override val positiveTitle: String? = "是"
//
//                        override fun positiveClick(dialog: AlertDialog) {
//                            showFragment(LoginFragment.getNewInstance("登入"))
//                        }
//
//                        override val showNegativeButton: Boolean = true
//                        override val negativeTitle: String? = "否"
//                        override fun negativeClick(dialog: AlertDialog) {
//
//                        }
//
//                        override val showNeutralButton: Boolean = false
//                        override val neutralTitle: String? = null
//                        override fun neutralClick(dialog: AlertDialog) {
//                        }
//                    })
                }
            }

            R.id.nav_gallery -> {
                showFragment(GalleryFargment.getNewInstance("測試"))
            }
            R.id.nav_template_input_field -> {
                showFragment(TemplateInputFieldFragment.getNewInstance("文字欄位樣式"))
            }
            R.id.nav_template_button -> {
                showFragment(TemplateButtonFragment.getNewInstance("按鈕樣式"))
            }
            R.id.nav_usb_transfer -> {
                showFragment(UsbTransferFragment.getNewInstance("USB傳輸"))
            }
            R.id.nav_meat_carton_demo -> {
                startActivity(
                    Intent().apply {
                        setClass(this@NaviActivity, MeatCartonDemoActivity::class.java)
                    }
                )
            }
            R.id.nav_bluetooth -> {
                showFragment(BluetoothSettingFragment.getNewInstance("藍芽設備設定"))
            }
            R.id.nav_tone_button -> {
                showFragment(ToneFragment.getNewInstance("聲音樣式"))
            }
            R.id.nav_loading_dialog_button -> {
                showFragment(LoadingDialogDemoFragment.getNewInstance("LoadingDialog"))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    private val dbHandlerThread by lazy {
        val ht = HandlerThread("DB")
        ht.start()
        return@lazy ht
    }


    fun runOnDbThread(inTransaction: Boolean = false, func: AppDataBase.() -> Unit) {
        Handler(dbHandlerThread.looper).post {
            if (inTransaction)
                app.db.runInTransaction {
                    app.db.func()
                }
            else
                app.db.func()
        }
    }

    //TODO 沒權限下次再通知
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1002){
            if (grantResults.isNotEmpty()) {
                val permissionList: MutableList<String> = ArrayList()
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionList.add(permissions[i])
                    }
                }


                if(permissionList.size < 0){
                    try {
                        val dialog = NormalDialog()
                        dialog.setContext(this)
                        dialog.setTitle("錯誤")
                        dialog.setMessage("必須允許所有權限!!")
                        dialog.setOnCloseClickListener(View.OnClickListener {
                            this.finish()
                        })
                        dialog.show()
                    }catch (E: Throwable){

                    }
                }
                else
                {
                    //開始
                }


            }
        }





    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && supportFragmentManager.backStackEntryCount == 0)
            return true
        return super.onKeyDown(keyCode, event)
    }

}
