package com.user.example.viewmodel

import android.app.Application
import android.content.DialogInterface
import android.util.Log
import com.biotech.framework.viewModel.BaseViewModel

class LoadingDialogDemoViewModel(application: Application) : BaseViewModel(application) {

    fun showLoadingDialogCancelable(){

        val newThread = Thread(Runnable(){
            try {
                Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 123 start")
                Thread.sleep(1000*3)
                if (Thread.interrupted()){
                    closeLoadingDialog()
                    Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 123 close")
                    return@Runnable
                }

                Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 456 start")
                updateLoadingDialog("456")
                Thread.sleep(1000*3)
                if (Thread.interrupted()){
                    closeLoadingDialog()
                    Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 456 close")
                    return@Runnable
                }

                Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 789 start")
                updateLoadingDialog("789")
                Thread.sleep(1000*3)
                if (Thread.interrupted()){
                    closeLoadingDialog()
                    Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 789 close")
                    return@Runnable
                }

            }catch (e : InterruptedException){
                Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " InterruptedException")
            }
            closeLoadingDialog()
        })

        showLoadingDialog("123", true, DialogInterface.OnCancelListener {
            newThread.interrupt()
        })

        newThread.start()
    }

    fun showLoadingDialog(){

        val newThread = Thread(Runnable(){
            Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 123 start")
            Thread.sleep(1000*3)

            Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 456 start")
            updateLoadingDialog("456")
            Thread.sleep(1000*3)

            Log.i("CustomThread","Thread ID: " + Thread.currentThread().id.toString() + " 789 start")
            updateLoadingDialog("789")
            Thread.sleep(1000*3)

            closeLoadingDialog()
        })

        showLoadingDialog("123", false)

        newThread.start()

    }
}