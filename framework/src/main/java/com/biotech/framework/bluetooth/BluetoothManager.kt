package com.biotech.framework.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.biotech.framework.interfaces.OnBluetoothNotifyListener
import java.util.*
import androidx.core.content.ContextCompat.startActivity




open class BluetoothManager(val context: Context, val lifecycle: Lifecycle) : LifecycleObserver {


    companion object {
        const val Spp_UUID = "00001101-0000-1000-8000-00805F9B34FB"
        val uuid = UUID.fromString(Spp_UUID)
        val serviceName = "RFCOMM_SERVICE"

    }

    private val TAG: String = this.javaClass.simpleName
    private var isRegister : Boolean = false
    var bluetoothAdapter: BluetoothAdapter? = null

    var onBluetoothNotifyListener : OnBluetoothNotifyListener? = null

    init {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        setLifecycle(lifecycle)
    }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device : BluetoothDevice?
            when(action) {
                // 搜尋設備時，取得設備的 MAC 位址
                BluetoothDevice.ACTION_FOUND -> {
                    device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    if(device != null && onBluetoothNotifyListener != null) {
                        onBluetoothNotifyListener!!.onDiscovery(device)
                    }
                }
                // 設備連結狀態
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    if(device != null && onBluetoothNotifyListener != null) {
                        onBluetoothNotifyListener!!.onStateChanged(device)
                    }
                }
                BluetoothDevice.ACTION_PAIRING_REQUEST -> {
                }
                else -> {

                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun resume() {
        registerReceiver(context)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun pause() {
        unregisterReceiver(context)
    }


    fun isEnabled() = bluetoothAdapter?.isEnabled!!

    fun on(context: Context) {
        if(!isEnabled()) {
            val intent1 = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivity(intent1)
        }
    }

    fun off() {
        if(isEnabled()) {
            bluetoothAdapter?.disable()
        }
    }

    @Throws(Exception::class)
    fun getRemoteDeviceByAddress(address : String) : BluetoothDevice? =  try { bluetoothAdapter?.getRemoteDevice(address) } catch (e : IllegalArgumentException) { null }

    fun registerReceiver(context: Context) {
        if(!isRegister) {

            val intent = IntentFilter()
            intent.addAction(BluetoothDevice.ACTION_FOUND)
            intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
            intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            context.registerReceiver(receiver, intent)
        }
    }

    fun unregisterReceiver(context: Context) {
        if(isRegister) {
            context.unregisterReceiver(receiver)
            isRegister = false
        }
    }

    fun enableDiscoverable(timeout : Int) {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, timeout)
        context.startActivity(intent)
    }

    fun startDiscovery() {
        bluetoothAdapter!!.startDiscovery()
    }

    fun cancelDiscovery() {
        bluetoothAdapter!!.cancelDiscovery()
    }

    fun createClient() = BluetoothClient(this)

    fun createServer() = BluetoothServer(this, bluetoothAdapter?.listenUsingRfcommWithServiceRecord(serviceName, uuid))

    /**
     * Get a list of paired device
     *
     * @return
     */
    fun getPairedDevices() = bluetoothAdapter?.bondedDevices!!

    /**
     * Checks if a device is already paired.
     *
     * @param device the device to check.
     * @return true if it is already paired, false otherwise.
     */
    fun isAlreadyPaired(device: BluetoothDevice): Boolean {
        return getPairedDevices().contains(device)
    }

    /**
     * Performs the device pairing.
     *
     * @param device the device to pair with.
     * @return true if the pairing was successful, false otherwise.
     */
    fun pair(device: BluetoothDevice): Boolean {
        // Stops the discovery and then creates the pairing.
        if (bluetoothAdapter?.isDiscovering ?: false) {
            Log.d(TAG, "Bluetooth cancelling discovery.")
            bluetoothAdapter?.cancelDiscovery()
        }

        return if(!isAlreadyPaired(device)) createBond(device) else false
    }

    /**
     * Performs the device unpairing.
     *
     * @param device the device to check.
     * @return true if the unpairing was successful, false otherwise.
     */
    fun unpair(device: BluetoothDevice):Boolean {
        // Stops the discovery and then creates the pairing.
        if (bluetoothAdapter?.isDiscovering ?: false) {
            Log.d(TAG, "Bluetooth cancelling discovery.")
            bluetoothAdapter?.cancelDiscovery()
        }

        return if(isAlreadyPaired(device)) removeBond(device) else false
    }

    fun setLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }


    /**
     * 与设备配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    @Throws(Exception::class)
    private fun createBond(btDevice: BluetoothDevice): Boolean {
        val classBt = Class.forName("android.bluetooth.BluetoothDevice")
        return classBt.getMethod("createBond").invoke(btDevice) as Boolean
    }

    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    @Throws(Exception::class)
    private fun removeBond(btDevice: BluetoothDevice): Boolean {
        val classBt = Class.forName("android.bluetooth.BluetoothDevice")
        return classBt.getMethod("removeBond").invoke(btDevice) as Boolean
    }

    @Throws(Exception ::class)
    private fun setPairingConfirmation(device : BluetoothDevice, isConfirm : Boolean) : Boolean {
        val classBt = Class.forName("android.bluetooth.BluetoothDevice")
        return classBt.getMethod("setPairingConfirmation", Boolean::class.java).invoke(device,isConfirm) as Boolean
    }

    @Throws(Exception::class)
    private fun setPin(btDevice: BluetoothDevice, str: String): Boolean {
        try {
            val classBt = Class.forName("android.bluetooth.BluetoothDevice")
            val removeBondMethod = classBt.getDeclaredMethod(
                "setPin",
                ByteArray::class.java
            )
            return removeBondMethod.invoke(
                btDevice,
                str.toByteArray()
            ) as Boolean

        } catch (e: SecurityException) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return true

    }



}


