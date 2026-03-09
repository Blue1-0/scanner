package com.blue.lib_scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import com.blue.lib_scanner.inner.OnDeviceConnectedListener
import com.blue.lib_scanner.inner.OnDeviceFoundListener

class DeviceConnectedHelper(private val context: Context) {

    private var onDeviceConnectedListener: OnDeviceConnectedListener? = null
    private var onDeviceFoundListener: OnDeviceFoundListener? = null

    private val scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (null == intent?.action) return
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                    when (blueState) {
                        BluetoothAdapter.STATE_ON -> showToast(context, "STATE_ON")
                        BluetoothAdapter.STATE_OFF -> showToast(context, "STATE_OFF")
                        BluetoothAdapter.STATE_TURNING_ON -> showToast(context, "STATE_TURNING_ON")
                        BluetoothAdapter.STATE_TURNING_OFF -> showToast(
                            context,
                            "STATE_TURNING_OFF"
                        )

                        BluetoothAdapter.STATE_CONNECTING -> showToast(context, "STATE_CONNECTING")
                        BluetoothAdapter.STATE_DISCONNECTING -> showToast(
                            context,
                            "STATE_DISCONNECTING"
                        )

                        BluetoothAdapter.STATE_CONNECTED -> showToast(context, "STATE_CONNECTED")
                        BluetoothAdapter.STATE_DISCONNECTED -> showToast(
                            context,
                            "STATE_DISCONNECTED"
                        )

                        else -> showToast(context, "其他状态：$blueState")
                    }
                }

                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    onDeviceConnectedListener?.onDeviceStateChanged(device?.name, true)
                }

                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    onDeviceConnectedListener?.onDeviceStateChanged(device?.name, false)
                }

                BluetoothDevice.ACTION_FOUND -> {
                    onDeviceFoundListener?.onDeviceFound(device)
                }
            }
        }

        private fun showToast(context: Context?, msg: String) {
            context?.let { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
        }
    }

    fun register() {
        registerScanReceiver()
    }

    private fun registerScanReceiver() {
        val filter = IntentFilter()
        //        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(scanReceiver, filter)
    }

    fun unregister() {
        unregisterScanReceiver()
    }

    private fun unregisterScanReceiver() {
        context.unregisterReceiver(scanReceiver)
    }

    fun setOnDeviceConnectedListener(onDeviceConnectedListener: OnDeviceConnectedListener?) {
        this.onDeviceConnectedListener = onDeviceConnectedListener
    }

    fun setOnDeviceFoundListener(onDeviceFoundListener: OnDeviceFoundListener?) {
        this.onDeviceFoundListener = onDeviceFoundListener
    }
}
