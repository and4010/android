package com.user.example.printer

import android.content.Context
import android.util.Log
import com.biotech.framework.dialog.BaseDialog
import com.biotech.framework.dialog.LoadingDialog
import com.biotech.framework.extension.AsyncTask

import com.example.tscdll.TSCActivity



class TSCPrinter(val context : Context, val address: String) {


    companion object {

        fun print(address : String, quantity : Int, copy : Int, timeout : Int = 700, dialog: BaseDialog? = null, sendCommand : (TSCActivity) -> String) {

            dialog?.show()
            AsyncTask({
                print(address, quantity, copy, timeout, sendCommand)
            }, {
                dialog?.dismiss()
            })
        }

        private fun print(address : String, quantity : Int, copy : Int, timeout : Int = 700, sendCommand : (TSCActivity) -> String) {

            try {
                TSCActivity().run {
                    openport(address)
                    if (sendCommand(this) == "Ready") {
                        printlabel(quantity, copy)
                    }
                    closeport(timeout)
                }
            } catch (e: Exception) {
                Log.e("TSCPrinter", "message: ${e.message} | trace: ${e.stackTrace}")
            }
        }
    }

    private val tag by lazy { this.javaClass.simpleName }
    var status: String = ""
    var statusDescription : String = ""
    var timeout : Int = 700

    //TODO 藍芽測試列印
    fun testprint() {

        val cmd : (tsc : com.example.tscdll.TSCActivity) -> String = { tsc ->
            tsc.sendcommandBig5("SIZE 40mm,0 mm\n")
            tsc.sendcommandBig5("CLS\n")
            tsc.sendcommandBig5("SPEED 5\n")
            tsc.sendcommandBig5("GAP 0,0\n")
            tsc.sendcommandBig5("DIRECTION 0,0\n")
            tsc.sendcommandBig5("SET TEAR ON\n")
            tsc.sendcommandBig5("TEXT 15,40,\"TW01\",0,3,3,\"這是測試列印\"\n")

            status = tsc.status(timeout)
            statusDescription = getDescription(status)

            Log.e(tag,"列印狀態: ${statusDescription}")

            status

        }

        print(address, 1, 1, timeout, LoadingDialog.getInstance(context).setMessage("列印中... 請稍候!!").setCancel(false), cmd)

    }


    private fun getDescription(status: String): String = when(status) {
            "-1" -> "藍芽連線發生錯誤，請檢察藍芽狀態或是檢查藍芽MAC"
            "Ready" -> "準備就緒"
            "Head Open" -> "印字頭開啟"
            "Ribbon Jam" -> "碳帶卡住"
            "Ribbon Empty" -> "碳帶用盡"
            "No Paper" -> "沒裝紙"
            "Paper Jam" -> "紙張卡紙"
            "Paper Empty" -> "紙張用盡"
            "Cutting" -> "正在裁切紙張"
            "Waiting to Press Print Key" -> "等待按下列印紐"
            "Printing Batch" -> "批次列印中"
            "Pause" -> "印標機暫停中"
            else -> "未知錯誤，請重新開啟印標機"
    }

}


