package com.user.example.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.biotech.framework.adapter.RecycleViewSwipeMoveAdapter
import com.biotech.framework.dialog.LoadingDialog
import com.biotech.framework.extension.AsyncTask
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.*
import com.user.example.R
import com.user.example.StartApplication
import java.io.File
import java.net.Socket

class GalleryFargment: BaseFragment(R.layout.fragment_gallery) {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        fun getNewInstance(title : String): GalleryFargment {
            return GalleryFargment().apply { this.title = title }
        }
    }
    private val recyclerview by bindView<RecyclerView>(R.id.recyclerview)
    private val recyaviewadapter by lazy { RecycleViewSwipeMoveAdapter(mutableListOf(), R.layout.recycleview_item_keyvalue_singleline) }
    val button3 by bindView<Button>(R.id.button3)
    val socketUtil by lazy { SocketUtil() }
    val powermanager by lazy { Powermanager(this.requireContext(),requireActivity()) }



    override fun initView() {
        button3
        powermanager
        val layoutManage = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.layoutManager = layoutManage
        val callback = TouchHelperCallback(recyaviewadapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerview)
        recyclerview.addItemDecoration(itemTouchHelper)

        recyclerview.run {
            adapter = recyaviewadapter.also {

            }
        }
    }

    override fun initEvent() {

    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(p0.id){
                button3.id ->{
                    val socket = Socket()
                    val savePath = StartApplication.instance.fileUtil.getExternalFolder(PathUtil.FolderType.Download).absolutePath + File.separator + "wellcome.db"
                    val updatafile = File(savePath)
                    if (updatafile == null){
                        ToastUtils.showToast(requireContext(),"無檔案",R.raw.notify)
                        return
                    }
                    powermanager.openPower()
                    val showDialog = LoadingDialog.getInstance(requireContext()).setMessage("下載中... 請稍候!!").setCancel(true)
                    val bg =  AsyncTask({

                     },
                        {

                            showDialog.dismiss()
                            Log.e("TAG","isDone")
                        })

                    showDialog.setOnCancelListener(DialogInterface.OnCancelListener {

                            powermanager.closePower()
                        bg.cancel(true)
                        }).show()
                }
                else -> {
                }
            }
        }
    }
}