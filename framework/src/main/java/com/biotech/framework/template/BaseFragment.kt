package com.biotech.framework.template

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.biotech.framework.dialog.LoadingDialog
import com.biotech.framework.dialog.NormalDialog
import com.biotech.framework.interfaces.NormalDialogListener
import com.biotech.framework.util.MessageUtils
import com.biotech.framework.util.ToneUtils
import com.biotech.framework.viewModel.BaseViewModel
//import org.slf4j.LoggerFactory


abstract class BaseFragment(): Fragment(),View.OnClickListener{

    open val TAG by lazy { this.javaClass.simpleName }

//    open val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    var title: String? = null
    var layoutRes : Int = 0
    private lateinit var myView: View
    var normalDialogListener : NormalDialogListener? = null
    var loadingDialog: LoadingDialog? = null

    constructor(@LayoutRes layoutRes: Int) :this()
    {
        this.layoutRes = layoutRes
    }

    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View

    abstract fun initView()

    abstract fun initEvent()

    var flag : Boolean = false //防止重複加載

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        when {
            !::myView.isInitialized && layoutRes > 0 -> {
                myView = inflater.inflate(layoutRes, container, false)
            }
            !::myView.isInitialized -> {
                myView = createView(inflater, container, savedInstanceState)
            }
        }

        return myView
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        if (!flag){
//            loadingDialog = LoadingDialog()
//            loadingDialog!!.setContext(baseActivity!!)
//            loadingDialog!!.isCancelable = false
//
//            initView()
//            initEvent()
//            flag = true
//        }
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!flag){
//            loadingDialog = LoadingDialog()
//            loadingDialog!!.setContext(baseActivity!!)
//            loadingDialog!!.isCancelable = false

            initView()
            initEvent()
            flag = true
        }
    }


    fun initViewModel(baseViewModel: BaseViewModel){

        baseViewModel.progressBar.observe(this, Observer {
            try {
                if (it.isShowing) {
                    if(loadingDialog == null || loadingDialog!!.dialog == null || !loadingDialog!!.dialog!!.isShowing){
                        //LoadingDialog未初始化或沒Show出來時，須重新取得實體並Show出來
                        loadingDialog = LoadingDialog.getInstance(requireContext())
                            .setMessage(it.message)
                            .setCancel(it.isCancelable)
                            .setOnCancelListener(it.onCancelListener)
                        loadingDialog!!.show()
                        requireActivity().supportFragmentManager.executePendingTransactions();
                    }else{
                        //處理變更LoadingDialog的message
                        loadingDialog!!.changeMessage(it.message)
                    }
                }
                else{
                    loadingDialog?.dismiss()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        baseViewModel.messageResultModel.observe(this, Observer {
            if(it == null){
                return@Observer
            }

            when(it.code){
                BaseViewModel.MessageType.Normal.Code ->{
                    MessageUtils.showNormalMessage(this.requireContext(), it.msg, it.turnOnSound, it.turnOnVibration)
                }
                BaseViewModel.MessageType.Warning.Code ->{
                    MessageUtils.showWarningMessage(this.requireContext(), it.msg, it.turnOnSound, it.turnOnVibration)
                }
                BaseViewModel.MessageType.Error.Code ->{
                    MessageUtils.showErrorMessage(this.requireContext(), it.msg, it.turnOnSound, it.turnOnVibration)
                }
            }
        })


        baseViewModel.notifyMessageResultModel.observe(this, Observer {
            if(it == null){
                return@Observer
            }

            when(it.code){
                BaseViewModel.MessageType.Normal.Code ->{
                    try {
                        if(it.turnOnSound){
                            //鈴聲
                            Thread{
                                ToneUtils.playNormal()
                            }.start()
                        }

                        if (it.turnOnVibration){
                            //震動
                            val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                vibrator!!.vibrate(VibrationEffect.createOneShot(100, 50))
                            }
                        }

                        val dialog = NormalDialog.getInstance(this.requireContext())
                        dialog.setTitle(it.title)
                        dialog.setMessage(it.msg)
                        dialog.setNormalDialogListener(normalDialogListener)
                        dialog.show()

                    }catch (E :Throwable){

                    }
                }
                BaseViewModel.MessageType.Warning.Code ->{
                    try {
                        if(it.turnOnSound){
                            //鈴聲
                            Thread{
                                ToneUtils.playWarning()
                            }.start()
                        }

                        if (it.turnOnVibration){
                            //震動
                            val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                vibrator!!.vibrate(VibrationEffect.createOneShot(100, 50))
                            }
                        }

                        val dialog = NormalDialog.getInstance(this.requireContext())
                        dialog.setTitle(it.title)
                        dialog.setMessage(it.msg)
                        dialog.setNormalDialogListener(normalDialogListener)
                        dialog.show()

                    }catch (E :Throwable){

                    }
                }
                BaseViewModel.MessageType.Error.Code ->{
                    try {
                        if(it.turnOnSound){
                            //鈴聲
                            Thread{
                                ToneUtils.playError()
                            }.start()
                        }

                        if (it.turnOnVibration){
                            val timings = longArrayOf(0, 200, 200, 200, 200, 200)
                            //震動
                            MessageUtils.vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                MessageUtils.vibrator!!.vibrate(VibrationEffect.createWaveform(timings, -1))
                            }
                        }

                        val dialog = NormalDialog.getInstance(this.requireContext())
                        dialog.setTitle(it.title)
                        dialog.setMessage(it.msg)
                        dialog.setNormalDialogListener(normalDialogListener)
                        dialog.show()

                    }catch (E :Throwable){

                    }
                }
            }


        })



    }

    override fun onResume() {
        super.onResume()

        baseActivity?.title = title
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        if (myView.parent != null)
            (myView.parent as ViewGroup).removeAllViews()
        super.onDestroyView()
    }

    protected fun <T : View> bindView(@IdRes id: Int, setOnClickListener: Boolean = true): Lazy<T> {
        return lazy {
            val view: T = this.requireView().findViewById(id)
            if (setOnClickListener && view !is AdapterView<*>)
                view.setOnClickListener(this)
            return@lazy view
        }
    }


    var baseActivity: BaseActivity? = null
        get() {
            return if (activity == null || activity !is BaseActivity)
                null
            else
                activity as BaseActivity
        }


    fun runOnUiThread(func: () -> Unit) {
        baseActivity?.runOnUiThread {
            func()
        }
    }

    fun runOnThread(func: () -> Unit) {
        Thread { func() }.start()
    }

}
