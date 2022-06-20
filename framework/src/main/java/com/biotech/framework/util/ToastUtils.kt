package com.biotech.framework.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.Toast

class ToastUtils {

    companion object{

        private var toast: Toast? = null
        private const val baseGravity = Gravity.CENTER
        private const val baseDuration = Toast.LENGTH_SHORT


        /**************************************************************
         * Toast方法(從Resourse中尋找訊息)
         * @param argContext 目標實體狀態
         * @param argResourceId 資源訊息編號
         * @return Toast 元件
         */
        fun showToast(argContext: Context, argResourceId: Int): Toast? {
            return showToast(argContext, argContext.resources.getString(argResourceId))
        }

        /**************************************************************
         * Toast方法(從Resourse中尋找)
         * @param argContext 目標實體狀態
         * @param argResourceId 資源訊息編號
         * @param argDuration 顯示時間長短 Toast.LENGTH_SHORT(0):短(2s), Toast.LENGTH_LONG(1):長(3.5s)
         * @return Toast 元件
         */
        fun showToast(argContext: Context, argResourceId: Int, argDuration: Int): Toast? {
            return showToast(argContext, argContext.resources.getString(argResourceId), argDuration)
        }

        /**************************************************************
         * Toast方法(從Resourse中尋找)
         * @param argContext 目標實體狀態
         * @param argResourceId 資源訊息編號
         * @param argDuration 顯示時間長短 Toast.LENGTH_SHORT(0):短(2s), Toast.LENGTH_LONG(1):長(3.5s)
         * @param gravity 位置
         * @return Toast 元件
         */
        fun showToast(
            argContext: Context,
            argResourceId: Int,
            argDuration: Int,
            gravity: Int
        ): Toast? {
            return showToast(
                argContext,
                argContext.resources.getString(argResourceId),
                argDuration,
                gravity
            )
        }

        /**************************************************************
         * Toast方法(純文字)
         * @param argContext 目標實體狀態
         * @param argText 內容
         */
        fun showToast(argContext: Context?, argText: CharSequence?): Toast? {
            //預設短時間、位置下方
            return showCreateToast(
                argContext,
                argText,
                baseDuration,
                baseGravity
            )
        }

        /**************************************************************
         * Toast方法(純文字)
         * @param argContext 目標實體狀態
         * @param argText 內容
         * @param argDuration 顯示時間長短 0:短(2s) 1:長(3.5s)
         */
        fun showCenterToast(argContext: Context?, argText: CharSequence?, argDuration: Int): Toast? {
            //預設短時間
            return showCreateToast(
                argContext,
                argText,
                argDuration,
                baseGravity
            )
        }

        /**************************************************************
         * Toast方法(參數轉換)
         * @param argContext 目標實體狀態
         * @param argText 內容
         * @param argDuration 顯示時間長短 0:短(2s) 1:長(3.5s)
         * @param Args 內容參數
         * @return Toast 元件
         */
        fun showToast(
            argContext: Context?,
            argText: String?,
            argDuration: Int,
            vararg Args: Any?
        ): Toast? {
            return showCenterToast(argContext, String.format(argText!!, *Args), argDuration)
        }

        /**************************************************************
         * Toast方法(參數轉換)
         * @param argContext 目標實體狀態
         * @param argText 內容
         * @param argDuration 顯示時間長短 0:短(2s) 1:長(3.5s)
         * @param argGravity 位置
         * @param Args 內容參數
         * @return Toast 元件
         */
        fun showToast(
            argContext: Context?,
            argText: String?,
            argDuration: Int,
            argGravity: Int,
            vararg Args: Any?
        ): Toast? {
            return showCreateToast(
                argContext,
                String.format(argText!!, *Args),
                argDuration,
                argGravity
            )
        }


        /**************************************************************
         * Toast方法
         * @param argContext 目標實體狀態
         * @param argText 內容
         * @param argDuration 顯示時間長短 0:短(2s) 1:長(3.5s)
         * @param argGravity 位置
         * @return Toast 元件
         */
        @SuppressLint("ShowToast")
        fun showCreateToast(
            argContext: Context?,
            argText: CharSequence?,
            argDuration: Int,
            argGravity: Int
        ): Toast? {
            try {
                if (toast == null) {
                    toast = Toast.makeText(argContext, argText, argDuration)
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && toast!!.view!!.isShown)
                {
                    toast!!.cancel()
                }
                toast?.setGravity(argGravity, 0, 0)
                toast?.setText(argText)
                toast?.duration = argDuration
                toast?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return toast
        }




    }



}