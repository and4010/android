package com.biotech.framework.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.biotech.framework.interfaces.IDialogResultListener
import com.biotech.framework.util.StringUtils

abstract class BasicDialog : DialogFragment() {

    private val DEFULT_TITILE = "Default Title"
    private val DEFULT_MESSAGE = "Default Message"
    protected var BUNDLE_NAME = this.javaClass.simpleName + "Bundle"
    protected var DIALOG_TAG = this.javaClass.simpleName + "_TAG"
    protected var message: String = DEFULT_MESSAGE
    protected var title: String =DEFULT_TITILE
    protected var positiveButton = "PositiveButton"
    protected var negativeButton = "NegativeButton"
    protected var neutralButton = "NeutralButton"
    protected var dialog: AlertDialog? = null
    protected var dialogName = ""

    private var cancelable = false
    private var canceledOnTouchOutside = false
    @set:JvmName("layoutResourse")public var layoutResourse = -1


    protected abstract fun onDialogCreate(argContext: Context?): Dialog?
    protected abstract fun onDialogStop()
    protected abstract fun onDialogCancel(argDialog: DialogInterface?)

    enum class ButtonType {
        NONE, POSITIVE, NEGATIVE, NEUTRAL
    }

    /**************************************************************
     * 自訂 Dialog 事件介面
     **************************************************************/
    interface DialogFactory{
        fun onDialogStop()
        fun onDialogCancel(argDialog: DialogInterface?)
        fun onDialogCreate(argContext: Context?) : Dialog
    }

    private var dialogListener: DialogFactory? = null
    private var dialogResultListener: IDialogResultListener? = null



    /**************************************************************
     * 設定 IDialogResultListener
     * @param dialogResultListener IDialogResultListener
     */
    open fun setDialogResultListener(dialogResultListener: IDialogResultListener?) {
        this.dialogResultListener = dialogResultListener
    }

    /**************************************************************
     * 取得 IDialogResultListener
     */
    open fun getDialogResultListener(): IDialogResultListener {
        return dialogResultListener!!
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        this.dialogName = " (" + (if (StringUtils.isEmpty(this.title) || this.title == this.DEFULT_TITILE) this.message else this.title) + ") "

        if(dialogListener != null )
            return dialogListener!!.onDialogCreate(activity)

        val res  = onDialogCreate(activity) ?: return super.onCreateDialog(savedInstanceState)

        res.setCancelable(this.cancelable)
        res.setCanceledOnTouchOutside(this.canceledOnTouchOutside)
        return res


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setCancelable(this.cancelable)
        super.getDialog()!!.setCancelable(this.cancelable)
        super.getDialog()!!.setCanceledOnTouchOutside(this.canceledOnTouchOutside)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       if(this.layoutResourse > 0 )
           return inflater.inflate(this.layoutResourse,container,false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }



    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        if(dialogListener != null){
            dialogListener!!.onDialogStop()
            return
        }
        this.onDialogStop()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog  )
        if (dialogListener != null) {
            dialogListener!!.onDialogCancel(dialog)
            return
        }
        onDialogCancel(dialog)
    }


    /**************************************************************
     * 保護方法
     */
    protected open fun getCancelButtonClickListener(): View.OnClickListener? {
        return View.OnClickListener {
            if (getDialogResultListener() != null) getDialogResultListener().onDialogResult(true)
            dialog!!.dismiss()
        }
    }

    /**************************************************************
     * 取的按鈕常數對應的枚舉
     * @param argWhich 按鈕常數值
     * @return ButtonType 枚舉
     */
    protected open fun getButtonType(argWhich: Int): ButtonType? {
        if (argWhich == DialogInterface.BUTTON_POSITIVE) return ButtonType.POSITIVE
        if (argWhich == DialogInterface.BUTTON_NEGATIVE) return ButtonType.NEGATIVE
        return if (argWhich == DialogInterface.BUTTON_NEUTRAL) ButtonType.NEUTRAL else ButtonType.NONE
    }


    /**************************************************************
     * 公開方法
     */
    /**************************************************************
     * 取得 Dialog Window
     * @return Window
     */
    open fun getDialogWindow(): Window? {
        return dialog!!.window
    }

    /**************************************************************
     * 設定額外資料
     * @param argContext Context
     */
    open fun showDialog(argContext: Context) {
        this.show((argContext as AppCompatActivity).supportFragmentManager, DIALOG_TAG)
    }

    /**************************************************************
     * 對話視窗消失
     */
    open fun dismissDialog() {
        dismiss()
    }

    /**************************************************************
     * 是否正在顯示
     */
    open fun isShowing(): Boolean {
        return dialog != null && dialog!!.isShowing
    }

    /**************************************************************
     * 設定額外資料
     * @param argExtras 資料集合
     */
    open fun setExtraData(argExtras: Bundle?) {
        arguments?.putBundle(BUNDLE_NAME, argExtras)
    }

    /**************************************************************
     * 設定是否可取消 (false : 觸擊任何地方dialog皆不會消失)
     * @param argEnable 是/否
     */
    override fun setCancelable(argEnable: Boolean) {
        cancelable = argEnable
    }

    /**************************************************************
     * 是否可取消
     */
    override fun isCancelable(): Boolean {
        return cancelable
    }


    /**************************************************************
     * 設定是否可取消 (false : 觸擊物理返回鍵dialog會消失，其它地方皆不會消失)
     * @param argEnable 是/否
     */
    open fun setCanceledOnTouchOutside(argEnable: Boolean) {
        this.canceledOnTouchOutside = argEnable
    }

    /**************************************************************
     * 設定是否可觸及外部區域取消 (觸擊物理返回鍵dialog會消失，其它地方皆不會消失)
     */
    open fun isCanceledOnTouchOutside(): Boolean {
        return canceledOnTouchOutside
    }

    /**************************************************************
     * 設定 Dialog View 資源編號
     * @param argLayoutResourse 資源編號
     */
    open fun setLayoutResourse(argLayoutResourse: Int) {
        this.layoutResourse = argLayoutResourse
    }


    /**************************************************************
     * 設定 DialogListener
     * @param argDialogFactory DialogFactory
     */
    open fun setDialogListener(argDialogFactory: DialogFactory?) {
        dialogListener = argDialogFactory
    }


}