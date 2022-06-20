package com.user.example.fragment

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biotech.framework.adapter.RecycleViewKeyValueAdapter
import com.biotech.framework.adapter.SpinnerKeyValueAdapter
import com.biotech.framework.bluetooth.BluetoothManager
import com.biotech.framework.interfaces.*
import com.biotech.framework.model.KeyValueModel
import com.biotech.framework.template.BaseFragment
import com.biotech.framework.util.DialogUtil
import com.biotech.framework.util.OnRecyclerItemClickListener
import com.biotech.framework.util.ToastUtils
import com.user.example.R
import com.user.example.StartApplication
import com.user.example.databinding.FragmentBlueSetBinding
import com.user.example.repository.BluetoothDeviceRepository
import com.user.example.viewmodel.BluetoothSetViewModel


class BluetoothSettingFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun getNewInstance(title: String): BluetoothSettingFragment {
            return BluetoothSettingFragment().apply { this.title = title }
        }

    }

    private val bluetoothManager by lazy { BluetoothManager(requireContext(), this.lifecycle) }

    private val recycleViewKeyValueAdapter by lazy { RecycleViewKeyValueAdapter(mutableListOf()) }

    private val bluetoothSpinnerAdapter by lazy { SpinnerKeyValueAdapter(requireContext(), "請選擇藍芽印表機") }

    private val repository : BluetoothDeviceRepository by lazy {
        BluetoothDeviceRepository(
            StartApplication.instance,
            StartApplication.instance.preferencePath
        )
    }

//    private val viewModel : BluetoothSetViewModel by lazy { ViewModelProviders.of(this).get(BluetoothSetViewModel::class.java) }

    private val viewModel by viewModels<BluetoothSetViewModel>()

    private lateinit var dataBinding : FragmentBlueSetBinding

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blue_set, container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun initView() {

        bluetoothSpinnerAdapter
        bluetoothManager
        repository

        dataBinding.bluetoothSpinner.adapter = bluetoothSpinnerAdapter

            
        dataBinding.blueRecyclerView.run {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            )
            adapter = recycleViewKeyValueAdapter
        }

        viewModel.bluetoothSpinnerAdapter = bluetoothSpinnerAdapter
        viewModel.bluetoothRecycleViewKeyValueAdapter = recycleViewKeyValueAdapter
        viewModel.bluetoothManager = bluetoothManager
        viewModel.repository = repository

        dataBinding.viewModel = viewModel

    }

    override fun initEvent() {

        viewModel.let {
            it.saveDevices.observe(this, Observer { spinnerData ->
                bluetoothSpinnerAdapter.updateData(spinnerData)
            })
//            it.deviceName.observe(this, Observer { t -> dataBinding.blueName.setText(t) })
//            it.deviceAddress.observe(this, Observer { t -> dataBinding.blueMac.setText(t) })
            it.state.askSaveDialog.observe(this, Observer {
                DialogUtil(requireContext()).show("儲存", it, false, object : DialogButtonListener {
                    override val showNeutralButton: Boolean
                        get() = false
                    override val neutralTitle: String?
                        get() = null

                    override fun neutralClick(dialog: AlertDialog) {}

                    override val showPositiveButton: Boolean
                        get() = true
                    override val showNegativeButton: Boolean
                        get() = true
                    override val positiveTitle: String?
                        get() = "確定"
                    override val negativeTitle: String?
                        get() = "取消"

                    override fun positiveClick(dialog: AlertDialog) {
                        viewModel.saveDevice()
                    }

                    override fun negativeClick(dialog: AlertDialog) {}

                })
            })
            it.state.askDeleteDialog.observe(this, Observer {
                DialogUtil(requireContext()).show("刪除", it, false, object : DialogButtonListener {
                    override val showNeutralButton: Boolean
                        get() = false
                    override val neutralTitle: String?
                        get() = null

                    override fun neutralClick(dialog: AlertDialog) {}

                    override val showPositiveButton: Boolean
                        get() = true
                    override val showNegativeButton: Boolean
                        get() = true
                    override val positiveTitle: String?
                        get() = "確定"
                    override val negativeTitle: String?
                        get() = "取消"

                    override fun positiveClick(dialog: AlertDialog) {
                        viewModel.deleteDevice()
                    }

                    override fun negativeClick(dialog: AlertDialog) {}

                })
            })
            it.state.received.observe(this, Observer {message ->
                ToastUtils.showToast(this.requireContext(), message)
            })
            it.state.sent.observe(this, Observer { message ->
                ToastUtils.showToast(this.requireContext(), message)
            })
            it.bluetoothManager.onBluetoothNotifyListener = object : OnBluetoothNotifyListener {


                override fun onDiscovery(device: BluetoothDevice) {
                    val model = KeyValueModel(device.name ?: "", device.address)

                    if (!recycleViewKeyValueAdapter.list.contains(model)) {
                        recycleViewKeyValueAdapter.add(KeyValueModel(device.name ?: "", device.address))
                    }
                }

                override fun onStateChanged(device: BluetoothDevice) {

                }

            }
        }
        //TODO 點擊事件
        dataBinding.blueRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(dataBinding.blueRecyclerView) {
            override fun onLongClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

            }

            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                viewModel.deviceAddress.value = recycleViewKeyValueAdapter.list[position].value
                viewModel.deviceName.value = recycleViewKeyValueAdapter.list[position].key
            }
        })

        dataBinding.bluetoothSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onItemSelected(parent, view, position, id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun onClick(v: View?) {

    }
}



