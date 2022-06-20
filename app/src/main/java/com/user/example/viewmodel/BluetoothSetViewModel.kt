package com.user.example.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.biotech.framework.adapter.SpinnerKeyValueAdapter
import com.biotech.framework.bluetooth.BluetoothManager
import com.biotech.framework.model.KeyValueModel
import com.user.example.database.entity.BluetoothDevices
import com.user.example.repository.BluetoothDeviceRepository
import android.widget.AdapterView
import com.biotech.framework.adapter.RecycleViewKeyValueAdapter
import com.biotech.framework.bluetooth.BluetoothClient
import com.biotech.framework.bluetooth.BluetoothServer
import com.biotech.framework.extension.AsyncTask
import com.biotech.framework.interfaces.OnBluetoothClientListener
import com.biotech.framework.interfaces.OnBluetoothServerListener
import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.nio.charset.Charset


class BluetoothSetViewModel(application: Application) :AndroidViewModel(application) {

    val deviceName : MutableLiveData<String> = MutableLiveData()
    var deviceAddress : MutableLiveData<String> = MutableLiveData()
    val saveDevices : LiveData<List<KeyValueModel>> = Transformations.switchMap(deviceName) { repository.getDevices() }
    var searchDevices : LiveData<List<KeyValueModel>> = MutableLiveData()
    var spinnerSelectedItem : KeyValueModel? = null
    val state : State by lazy { State(MutableLiveData(), MutableLiveData(), MutableLiveData(), MutableLiveData())}
    var bluetoothClient : BluetoothClient? = null
    var bluetoothServer : BluetoothServer? = null

    lateinit var bluetoothSpinnerAdapter : SpinnerKeyValueAdapter

    lateinit var bluetoothRecycleViewKeyValueAdapter: RecycleViewKeyValueAdapter

    lateinit var bluetoothManager : BluetoothManager

    lateinit var repository: BluetoothDeviceRepository

    init {

    }

    fun discoveryDevices() {

        bluetoothRecycleViewKeyValueAdapter.clear()
        bluetoothManager.cancelDiscovery()
        bluetoothManager.startDiscovery()
        AsyncTask({

            Thread.sleep(20000)
        }, {
            bluetoothManager.cancelDiscovery()
        }, {
            bluetoothManager.cancelDiscovery()
        })
    }

    fun pairDevice() {
        if(deviceAddress.value != null) {
            bluetoothManager.getRemoteDeviceByAddress(deviceAddress.value!!)?.let { device ->
                bluetoothManager.pair(device)
            }
        }
    }

    fun askForSave() {
        if(deviceAddress.value != null && deviceName.value != null) {
            state.askSaveDialog.value = "是否確定要儲存 ${deviceName.value} ${deviceAddress.value} ?"
        }
    }

    fun saveDevice() {
        repository.saveDevice(BluetoothDevices(deviceAddress.value!!, deviceName.value!!, 0))
    }

    fun askForDelete() {
        if(spinnerSelectedItem != null) {
            state.askDeleteDialog.value = "是否確定要刪除 ${spinnerSelectedItem!!.key} ${spinnerSelectedItem!!.value} ?"
        }
    }

    fun deleteDevice() {
        if(spinnerSelectedItem != null) {
            repository.deleteDevice(spinnerSelectedItem!!.value)
        }
    }

    //TODO 解碼器
    private fun parseAddress(mac: String): String {
        var temp = mac.toUpperCase()
        when {
            temp.length == 12 -> {
                temp = (temp.substring(0, 2) + ":"
                        + temp.substring(2, 4) + ":"
                        + temp.substring(4, 6) + ":"
                        + temp.substring(6, 8) + ":"
                        + temp.substring(8, 10) + ":"
                        + temp.substring(10))
            }
            temp.length == 16 -> {

            }
            else -> {
                temp = ""
            }

        }

        return temp
    }


    fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if(parent != null) {
            spinnerSelectedItem = if (pos >= 0) parent.adapter.getItem(pos) as KeyValueModel else null
            spinnerSelectedItem?.let {
                deviceName.value = it.key
                deviceAddress.value = it.value
            }
        }
    }

    fun connectAndSend() {
        bluetoothManager.getRemoteDeviceByAddress(deviceAddress.value!!)?.let { device ->

            if(!bluetoothManager.isAlreadyPaired(device)) {
                bluetoothManager.pair(device)
                return
            }


            bluetoothManager.createClient().let {client ->

                client.onBluetoothClientListener = object : OnBluetoothClientListener {
                    override fun onConnect(client: BluetoothClient) {
                        val data = PrintKerryTJLabel(10, "12345678901", "12", "XX", "99", 1, 1, 1, 10, "9999")
                        val callback = { client.disconnect() }
                        client.send(data.toByteArray(Charset.forName("BIG5")), callback)
                    }

                    override fun onDisconnect(client: BluetoothClient) {

                    }
                }
                client.connect(device.address)
                bluetoothClient = client
            }
        }


    }

    fun receive() {
        bluetoothManager.run {

            createServer().let { server ->
                server.onBluetoothServerListener = object : OnBluetoothServerListener {
                    override fun onClientConnect(client: BluetoothClient) {
                        client.receive {byteArray ->
                            state.received.value = byteArray.toString()
                        }
                    }

                    override fun onClientDisconnect(client: BluetoothClient) {

                    }
                }
                server.accept()
                bluetoothServer = server
            }
        }
    }

    fun testPrint(): ByteArray {
        val cmd = ByteBuffer.allocate(1000)
        val big5charset = Charset.forName("BIG5")
        val engCharset = Charsets.US_ASCII
        cmd.put(("! 0 200 200 464 1\r\n").toByteArray(engCharset))
        cmd.put(("ON-FEED IGNORE\r\n").toByteArray(engCharset))
        cmd.put(("NO-PACE\r\n").toByteArray(engCharset))
        cmd.put(("JOURNAL\r\n").toByteArray(engCharset))
        cmd.put(("ENCODING BIG5\r\n").toByteArray(engCharset))
        cmd.put(("COUNTRY BIG5\r\n").toByteArray(engCharset))
        cmd.put(("CENTER\r\n").toByteArray(engCharset))
        cmd.put(("TEXT 10,10,\"3\",0,1,1,\"").toByteArray(engCharset))
        cmd.put(("HELLO WORLD").toByteArray(big5charset))
        cmd.put(("\"\r\n").toByteArray(engCharset))
        cmd.put(("PRINT\r\n").toByteArray(engCharset))

        return cmd.array()
    }

    //標籤列印
    fun PrintKerryTJLabel(
        x_offset: Int, itemno: String, dest_station: String,
        dest_station_name: String, fwd_station: String, paytype: Int, total: Int,
        pcs: Int, start_pcs: Int, date: String
    ): String {
        var pcs = pcs
        val cmd = StringBuilder()
        val fixchar = "A"
        if (pcs + start_pcs - 1 > total)
            pcs = total - start_pcs + 1

        cmd.append("! $x_offset 200 200 350 $pcs\r\n")
        cmd.append("SPEED 5\r\n")
        cmd.append("GAP-SENSE\r\n")
        cmd.append("CONTRAST 1\r\n")
        cmd.append("COUNTRY BIG5\r\n")
        cmd.append("TEXT 4 0 $x_offset 0 $date\r\n") //日期
        cmd.append("SETMAG 1.4 1 \r\n")
        cmd.append("TEXT 4 0 $x_offset 50 K\r\n") //T
        cmd.append("SETMAG 3 4 \r\n")
        cmd.append("TEXT 5 1 " + (x_offset + 60) + " 40 " + dest_station_name + "\r\n")//到著站中文
        cmd.append("SETMAG 1.5 4 \r\n")
        cmd.append("TEXT 4 10 " + (x_offset + 210) + " 5 " + dest_station + ".\r\n") //到著站簡碼
        cmd.append("SETMAG 1 2\r\n")

        if (paytype == 1) { //到付
            cmd.append("TEXT 4 3 " + (x_offset + 100) + " 130 $\r\n")
            cmd.append("INVERSE-LINE " + (x_offset + 98) + " 130 " + (x_offset + 145) + " 130 88\r\n")
        }

        cmd.append("SETMAG 0 0\r\n")
        if (!fwd_station.isEmpty())
            cmd.append("TEXT 4 0 " + (x_offset + 10) + " 170 " + fwd_station + ".\r\n")//所屬站簡碼

        var xPos = (4 - pcs.toString().length) * 20 + 130 + x_offset
        xPos = if (xPos > 0) xPos else 130 + x_offset

        if (total >= 100) { //件數-頁次
            if (start_pcs >= 1 && start_pcs < 10) {
                val pageNo = "00$start_pcs"
                cmd.append("TEXT 4 0 $xPos 170 $total-$pageNo\r\n")
            } else if (start_pcs >= 10 && start_pcs < 100) {
                val pageNo = "0$start_pcs"
                cmd.append("TEXT 4 0 $xPos 170 $total-$pageNo\r\n")
            } else if (start_pcs >= 100) {
                val pageNo = start_pcs.toString()
                cmd.append("TEXT 4 0 $xPos 170 $total-$pageNo\r\n")
            }
        } else if (total < 100) {
            if (start_pcs >= 1 && start_pcs < 10) {
                val pageNo = "0$start_pcs"
                cmd.append("TEXT 4 0 $xPos 170 $total-$pageNo\r\n")
            } else if (start_pcs >= 10) {
                val pageNo = start_pcs.toString()
                cmd.append("TEXT 4 0 $xPos 170 $total-$pageNo\r\n")
            }
        }

        //start_pcs = String.valueOf(total).length();
        //String pageNo = String.valueOf(start_pcs).toString();

        if (pcs > 1)
        //頁次累加1
            cmd.append("COUNT 1\r\n")
        cmd.append("BARCODE CODABAR 1 20 85 " + (x_offset + 50) + " 220 " + fixchar + itemno + fixchar + "\r\n") //託運單號 codabar 條碼

        val itemNo1 = Integer.parseInt(itemno.substring(0, 7))
        val itemNo2 = Integer.parseInt(itemno.substring(7))

        cmd.append("TEXT 4 0 " + (x_offset + 10) + " 300 " + fixchar + " " + itemNo1 + "-" + itemNo2 + " " + fixchar + "\r\n")//託運單號文字

        cmd.append("FORM\r\n")
        cmd.append("PRINT\r\n")

        return cmd.toString()
    }


    data class State(
        val askDeleteDialog : MutableLiveData<String>,
        val askSaveDialog : MutableLiveData<String>,
        val received : MutableLiveData<String>,
        val sent : MutableLiveData<String>
    )


}