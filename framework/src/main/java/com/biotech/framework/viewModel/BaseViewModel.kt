package com.biotech.framework.viewModel

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.biotech.framework.model.NotifyModel
import com.biotech.framework.model.LoadingDialogModel
import com.biotech.framework.model.ResultModel
import java.util.*

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var messageResultModel: MutableLiveData<NotifyModel<Int>> =  MutableLiveData()

    var notifyMessageResultModel: MutableLiveData<NotifyModel<Int>> =  MutableLiveData()

    var progressBar: MutableLiveData<LoadingDialogModel> = MutableLiveData()


    enum class MessageType(val Code: Int) {
        Normal(1),
        Success(0),
        Error(-1),
        Warning(-2),
    }

    @Synchronized
    open fun showErrorMessage(message: String? = "", turnOnSound: Boolean = true, turnOnVibration: Boolean = true) {
        val notifyModel: NotifyModel<Int> = NotifyModel()
        notifyModel.code = MessageType.Error.Code
        notifyModel.msg = message
        notifyModel.data = Random(Calendar.getInstance().timeInMillis).nextInt()
        notifyModel.turnOnSound = turnOnSound
        notifyModel.turnOnVibration = turnOnVibration
        messageResultModel.postValue(notifyModel)
    }

    @Synchronized
    open fun showNormalMessage(message: String? = "", turnOnSound: Boolean = true, turnOnVibration: Boolean = true) {
        val notifyModel: NotifyModel<Int> = NotifyModel()
        notifyModel.code = MessageType.Normal.Code
        notifyModel.data = Random(Calendar.getInstance().timeInMillis).nextInt()
        notifyModel.msg = message
        notifyModel.turnOnSound = turnOnSound
        notifyModel.turnOnVibration = turnOnVibration
        messageResultModel.postValue(notifyModel)
    }

    @Synchronized
    open fun showWarningMessage(message: String? = "", turnOnSound: Boolean = true, turnOnVibration: Boolean = true) {
        val notifyModel: NotifyModel<Int> = NotifyModel()
        notifyModel.code = MessageType.Warning.Code
        notifyModel.data = Random(Calendar.getInstance().timeInMillis).nextInt()
        notifyModel.msg = message
        notifyModel.turnOnSound = turnOnSound
        notifyModel.turnOnVibration = turnOnVibration
        messageResultModel.postValue(notifyModel)
    }

    @Synchronized
    open fun showLoadingDialog(message: String = "", isCancelable: Boolean = false, onCancelListener : DialogInterface.OnCancelListener? = null) {
        progressBar.postValue(LoadingDialogModel(message, isCancelable, true, onCancelListener))
    }

    @Synchronized
    open fun showNormalDialog(message: String? = "", title: String? = "訊息", turnOnSound: Boolean = true, turnOnVibration: Boolean = true) {
        val notifyModel: NotifyModel<Int> = NotifyModel()
        notifyModel.msg = message
        notifyModel.title = title
        notifyModel.code = MessageType.Normal.Code
        notifyModel.data = Random(Calendar.getInstance().timeInMillis).nextInt()
        notifyModel.turnOnSound = turnOnSound
        notifyModel.turnOnVibration = turnOnVibration
        notifyMessageResultModel.postValue(notifyModel)
    }

    @Synchronized
    open fun showWarningDialog(message: String? = "", title: String? = "訊息", turnOnSound: Boolean = true, turnOnVibration: Boolean = true) {
        val notifyModel: NotifyModel<Int> = NotifyModel()
        notifyModel.msg = message
        notifyModel.title = title
        notifyModel.code = MessageType.Warning.Code
        notifyModel.data = Random(Calendar.getInstance().timeInMillis).nextInt()
        notifyModel.turnOnSound = turnOnSound
        notifyModel.turnOnVibration = turnOnVibration
        notifyMessageResultModel.postValue(notifyModel)
    }

    @Synchronized
    open fun showErrorDialog(message: String? = "", title: String? = "訊息", turnOnSound: Boolean = true, turnOnVibration: Boolean = true) {
        val notifyModel: NotifyModel<Int> = NotifyModel()
        notifyModel.msg = message
        notifyModel.title = title
        notifyModel.code = MessageType.Error.Code
        notifyModel.data = Random(Calendar.getInstance().timeInMillis).nextInt()
        notifyModel.turnOnSound = turnOnSound
        notifyModel.turnOnVibration = turnOnVibration
        notifyMessageResultModel.postValue(notifyModel)
    }

    @Synchronized
    open fun closeLoadingDialog() {
        progressBar.postValue(LoadingDialogModel(isShowing = false))
    }


    @Synchronized
    open fun updateLoadingDialog(message: String) {
        progressBar.postValue(LoadingDialogModel(message, isShowing = true))
    }


}