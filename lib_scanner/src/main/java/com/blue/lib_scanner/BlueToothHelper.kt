package com.blue.lib_scanner

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import java.lang.reflect.InvocationTargetException

class BlueToothHelper(activity: Activity) {
    private val context: Context
    var bluetoothAdapter: BluetoothAdapter?

    //    @RequiresPermission("")
    init {
        context = activity.applicationContext
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    val bTMAC: String?
        get() {
            try {
                val field = BluetoothAdapter::class.java.getDeclaredField("mService")
                field.isAccessible = true
                val bluetoothManagerService = field[bluetoothAdapter] ?: return null
                val method = bluetoothManagerService.javaClass.getMethod("getAddress")
                if (method != null) {
                    val obj = method.invoke(bluetoothManagerService)
                    if (obj != null) {
                        return obj.toString()
                    }
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            return null
        }

    companion object {
        private const val REQUEST_ENABLE_BT = 1010
    }
}
