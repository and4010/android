package com.user.example.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputEditText
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.OnKeyHandler
import com.user.example.NaviActivity
import com.user.example.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment: BaseFragment(R.layout.fragment_login) {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        fun getNewInstance(title: String): LoginFragment {
            return LoginFragment().apply { this.title = title }
        }

    }

     val account by bindView<TextInputEditText>(R.id.editAccount)
     val password by bindView<TextInputEditText>(R.id.editPassword)
     val login_bt by bindView<Button>(R.id.btnLogin)

    override fun initView() {
//        setContentView(R.layout.fragment_login)
        login_bt
//        permissionCheck()
    }

    override fun initEvent() {
        account.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)  {
                Log.e(TAG, "initEvent: " + keyCode + ", " + event.action )
            }

            return@OnKeyListener false
        })
        password.setOnKeyListener(OnKeyHandler(password))

    }

    override fun onClick(v : View?) {
        v?.let {
            when (it.id) {
                login_bt.id -> {
                    inputcheck()
                }
            }
        }
    }

    private fun inputcheck(){
        if (!checkAccount()){
            editAccount.requestFocus()
            return
        }

        if (!checkPassword()){
            editPassword.requestFocus()
            return
        }

        login()
    }

    private fun login(){
        var intent = Intent()
        var bundle = Bundle()
        bundle.putString("account",account.text.toString())
        intent.setClass(requireContext(), NaviActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
//        finish()
//        showFragment(GalleryFargment.getNewInstance("測試"))
    }

    private fun checkAccount() : Boolean {
        var result = false
        when {
            account.text.isNullOrEmpty() -> {
                account.error = "不得空白"
            }
            // TODO: 帳號其它的檢查條件
            else -> {
                result = true
            }
        }
        return result
    }

    private fun checkPassword() : Boolean {
        var result = false
        when {
            password.text.isNullOrEmpty() -> {
                password.error = "不得空白"
            }
            // TODO: 密碼其它的檢查條件
            else -> {
                result = true
            }
        }
        return result
    }




}