package com.biotech.framework.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.biotech.framework.R
import com.biotech.framework.util.EditTextUtil
import com.biotech.framework.util.OnKeyHelper
import com.biotech.framework.util.StringUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class InputEditDialog : BasicDialog() {

    private val DEFULT = "DEFULT"
    private val INPUTTEXT = "INPUTTEXT"
    private var inputEditTextHashMap: HashMap<String, InputEditText>? = null
    private var defaultTextInputEditText: TextInputEditText? = null
    private var confirmButton: Button? = null
    private var cancelButton: Button? = null
    var textInputLayouts: List<TextInputLayout> = java.util.ArrayList()

    /**************************************************************
     * 公開方法
     */
    /**************************************************************
     * 設定輸入框集合
     * @param inputEditTextHashMap  輸入框集合
     * @return 像素結果
     */
    fun setInputEditTextHashMap(inputEditTextHashMap: HashMap<String, InputEditText>) {
        this.inputEditTextHashMap = inputEditTextHashMap
    }

    /**************************************************************
     * 字串輸入框類別
     **************************************************************/
    class InputEditText {
        private var hintText: String? = null
        private var defaultValue: String? = null
        private var inputType = 0
        private var maxlength = 16
        private var onKeyHelper: OnKeyHelper? = null

        constructor(_hintText: String?) {
            this.InputEditText(_hintText, "", 16)
        }

        constructor(_hintText: String?, _maxLength: Int) {
            this.InputEditText(_hintText, "", _maxLength)
        }

        constructor(_hintText: String?, _defaultValue: String) {
            this.InputEditText(_hintText, _defaultValue, 0)
        }

        constructor(_hintText: String?, _maxLength: Int, _inputType: Int) {
            this.InputEditText(_hintText, "", _maxLength, _inputType)
        }

        constructor(_hintText: String?, _defaultValue: String, _inputType: Int) {
            this.InputEditText(_hintText, _defaultValue, 0, _inputType)
        }

        fun InputEditText(
            _hintText: String?,
            _defaultValue: String?,
            _maxLength: Int,
            _inputType: Int = InputType.TYPE_CLASS_TEXT
        ) {
            hintText = _hintText
            defaultValue = _defaultValue
            inputType = _inputType
            maxlength = if (_maxLength > 0) _maxLength else maxlength
        }

        fun getHintText(): String? {
            return hintText
        }

        fun getDefaultValue(): String? {
            return defaultValue
        }

        fun getInputType(): Int {
            return inputType
        }

        fun getMaxlength(): Int {
            return maxlength
        }

        fun setOnKeyHelper(_onKeyHelper: OnKeyHelper?) {
            this.onKeyHelper = _onKeyHelper
        }

        fun getOnKeyHelper(): OnKeyHelper? {
            return this.onKeyHelper
        }

    }

    /**************************************************************
     * 輸入結果類別
     */
    class InputTextDialogResult {
        private val DEFULT = "DEFULT"
        private var whichButton: BasicDialog.ButtonType? = null
        private var values: HashMap<String, String>? = null

        constructor(whichButton: BasicDialog.ButtonType, values: HashMap<String, String>){
            this.whichButton = whichButton
            this.values = values
        }


        fun getWhichButton(): BasicDialog.ButtonType {
            return whichButton!!
        }

        fun getValues(): HashMap<String, String> {
            return values!!
        }

        fun getValue(): String {
            return if (values!!.containsKey(DEFULT)) values!![DEFULT]!! else ""
        }


    }

    /**************************************************************
     * 取得輸入結果集合
     * @param _textInputLayouts    輸入框Layout集合
     * @return 輸入結果集合
     */
    private fun getEditTextResult(_textInputLayouts: List<TextInputLayout>): java.util.HashMap<String, String>? {
        val textList = HashMap<String, String>()
        if (_textInputLayouts.isEmpty()) return textList
        for (txtlyout in _textInputLayouts) {
            val res =
                if (StringUtils.isEmpty(txtlyout.editText!!.text.toString())) "" else txtlyout.editText!!
                    .text.toString()
            textList[txtlyout.editText!!.tag.toString()] = res
        }
        return textList
    }

    /**************************************************************
     * 輸入結果處理方法
     * @param _textInputLayouts 輸入框集合
     * @param _buttonType        按鈕
     */
    private fun onClickResult(_textInputLayouts: List<TextInputLayout>, _buttonType: ButtonType) {
        val result: HashMap<String, String> = getEditTextResult(_textInputLayouts)!!
        if (getDialogResultListener() != null)
            getDialogResultListener().onDialogResult(InputTextDialogResult(_buttonType, result)
        )
        dialog!!.dismiss()

    }

    /**************************************************************
     * 取得輸入框最大寬度
     * @param argEditNames InputEditText集合
     * @return 最大寬度
     */
    private fun getMaxLength(argEditNames: List<InputEditText>): Int {
        var maxlength = 0
        for (et in argEditNames) {
            if (et.getMaxlength() > maxlength) maxlength = et.getMaxlength()
        }
        return maxlength
    }

    /**************************************************************
     * 取得預設輸入框Layout
     * @param argContext    Context
     * @return 像素結果
     */
    private fun getDefaultTextInputLayout(argContext: Context): TextInputLayout? {
        val textInputLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val textInputLayout = TextInputLayout(argContext)
        textInputLayout.layoutParams = textInputLayoutParams
        return textInputLayout
    }

    /**************************************************************
     * 取得預設輸入框
     * @param argContext    Context
     * @return 像素結果
     */
    private fun getDefaultEditText(argContext: Context): TextInputEditText? {
        val ed = TextInputEditText(argContext)
        ed.setBackgroundResource(R.drawable.underline_edit_text_holo_light)
        ed.gravity = Gravity.CENTER_HORIZONTAL
        ed.isSingleLine = true
        ed.gravity = Gravity.CENTER
        ed.setTextAppearance(R.style.edit_text_value)
        ed.imeOptions = EditorInfo.IME_ACTION_NEXT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            ed.setTextCursorDrawable(R.drawable.cursor_edit_text)
        return ed
    }

    /**************************************************************
     * 私有方法
     */
    /**************************************************************
     * 建立輸入框物件集合
     * @param argContext            Context
     * @param _inputEditTextHashMap InputEditText集合
     * @return 輸入框集合
     */
    private fun createView(
        argContext: Context,
        _inputEditTextHashMap: java.util.HashMap<String, InputEditText>
    ): List<TextInputLayout> {
        val tmp: MutableList<TextInputLayout> = ArrayList()
        if (_inputEditTextHashMap.size <= 0) {
            val input = InputEditText(INPUTTEXT)

            //TextInputLayout
            val textInputLayout: TextInputLayout = this.getDefaultTextInputLayout(argContext)!!

            //TextInputEditText
            defaultTextInputEditText = this.getDefaultEditText(textInputLayout.context)
            defaultTextInputEditText!!.tag = DEFULT
            defaultTextInputEditText!!.inputType = input.getInputType()
            defaultTextInputEditText!!.setHint(input.getHintText())
            defaultTextInputEditText!!.setText(input.getDefaultValue())
            defaultTextInputEditText!!.imeOptions = EditorInfo.IME_ACTION_DONE
            textInputLayout.addView(defaultTextInputEditText)
            tmp.add(textInputLayout)
        } else {
            var count = 0
            for ((key, value) in _inputEditTextHashMap) {
                //TextInputLayout
                val textInputLayout: TextInputLayout = this.getDefaultTextInputLayout(argContext)!!

                //TextInputEditText
                val txtInputEd: TextInputEditText =
                    this.getDefaultEditText(textInputLayout.context)!!
                txtInputEd.tag = key
                txtInputEd.inputType = value.getInputType()
                txtInputEd.hint = value.getHintText()
                txtInputEd.setText(value.getDefaultValue())
                txtInputEd.filters = arrayOf<InputFilter>(
                    InputFilter.LengthFilter(
                        value.getMaxlength()
                    )
                )

                //OnKeyEvent
                if (value.getOnKeyHelper() != null) {
                    txtInputEd.setOnKeyListener(value.getOnKeyHelper())
                    txtInputEd.setOnEditorActionListener(value.getOnKeyHelper())
                    if (count == _inputEditTextHashMap.size - 1) txtInputEd.imeOptions =
                        EditorInfo.IME_ACTION_DONE
                }
                EditTextUtil.initKeyboard(txtInputEd, txtInputEd.context, false)
                textInputLayout.addView(txtInputEd)
                tmp.add(textInputLayout)
                count++
            }
            if (tmp.size >= 1) defaultTextInputEditText = tmp[0].editText as TextInputEditText?
            this.textInputLayouts = tmp
        }
        return tmp
    }


    /**************************************************************
     * 覆寫 TKBasicDialog 抽象方法
     **************************************************************/
    override fun onDialogCreate(argContext: Context?): Dialog? {
        //Layout 綁定

        //Layout 綁定
        val layoutInflater = LayoutInflater.from(argContext)
        val promptView: View = layoutInflater.inflate(R.layout.input_text_dialog, null)

        //UI綁定

        //UI綁定
        val title = promptView.findViewById<View>(R.id.inputtext_dialog_title) as TextView
        confirmButton =
            promptView.findViewById<View>(R.id.inputtext_dialog_confirm_button) as Button
        cancelButton = promptView.findViewById<View>(R.id.inputtext_dialog_cancel_button) as Button

        //title

        //title
        title.text = this.title

        //繪製畫面

        //繪製畫面
        val rootLinearLayout =
            promptView.findViewById<View>(R.id.inputtext_dialog_rootLinearLayout) as LinearLayout
        val scrollView = ScrollView(rootLinearLayout.context)
        val scrollViewLinearLayout = LinearLayout(scrollView.context)

        scrollViewLinearLayout.orientation = LinearLayout.VERTICAL
        scrollView.addView(scrollViewLinearLayout)
        rootLinearLayout.addView(scrollView)

        val textInputLayouts: List<TextInputLayout> = inputEditTextHashMap?.let {
            this.createView(
                rootLinearLayout.context,
                it
            )
        }!!
        for (txtlayout in textInputLayouts) scrollViewLinearLayout.addView(txtlayout)

        //button

        //button
        confirmButton!!.text = positiveButton
        cancelButton!!.text = negativeButton

        //定義正向按鈕事件

        //定義正向按鈕事件
        if (!StringUtils.isEmpty(positiveButton)) confirmButton!!.setOnClickListener {
            onClickResult(
                textInputLayouts,
                ButtonType.POSITIVE
            )
        }

        //定義負向按鈕事件

        //定義負向按鈕事件
        if (!StringUtils.isEmpty(negativeButton)) cancelButton!!.setOnClickListener {
            onClickResult(
                textInputLayouts,
                ButtonType.NEGATIVE
            )
        }

        //建立對話視窗

        //建立對話視窗
        dialog = AlertDialog.Builder(argContext!!).create()
        dialog!!.setView(promptView)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onDialogStop() {
        TODO("Not yet implemented")
    }

    override fun onDialogCancel(argDialog: DialogInterface?) {
        TODO("Not yet implemented")
    }

    /**************************************************************
     * 公開方法
     */
    /**************************************************************
     * 觸發確認按鈕
     */
    fun doConfirmButtonClick() {
        if (confirmButton != null) confirmButton!!.performClick()
    }

    /**************************************************************
     * 觸發取消按鈕
     **************************************************************/
    fun doCancelButtonClick(){
        if(cancelButton != null) cancelButton!!.performClick()
    }

    /**************************************************************
     * 聚焦下個輸入框
     */
    fun doFocusNextInputText() {
        defaultTextInputEditText!!.focusSearch(View.FOCUS_DOWN).requestFocus()
    }

    override fun onResume() {
        super.onResume()
        if (defaultTextInputEditText != null) {
            //EditKeyPad.disableSoftInput(this.defaultTextInputEditText);
            EditTextUtil.initKeyboard(this.defaultTextInputEditText!!, this.requireContext(),  false)
            defaultTextInputEditText!!.isFocusable = true
            defaultTextInputEditText!!.requestFocus()
        }
    }

}
