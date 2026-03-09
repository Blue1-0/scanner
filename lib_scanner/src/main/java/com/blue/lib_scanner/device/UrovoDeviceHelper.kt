package com.blue.lib_scanner.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils
import com.blue.lib_scanner.ScannerManager
import com.blue.lib_scanner.ScannerType
import com.blue.lib_scanner.inner.IScannerDeviceHelper
import com.blue.lib_scanner.inner.IScannerDeviceHelper.OnScanListener

/**
 * @author Blue
 * @desc Urovo
 */
object UrovoDeviceHelper : IScannerDeviceHelper {
    private var scanListener: OnScanListener? = null
    private val scanBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val finalScanData = intent.getStringExtra(ACTION_KEY_DEFAULT)
                if (scanListener != null) {
                    scanListener!!.onReceivedScanCode(finalScanData)
                }
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: $finalScanData")
            }
        }
    }

    override fun registerScanReceiver() {
        if (!isUrovo) return
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_NAME_DEFAULT)
        ContextCompat.registerReceiver(
            Utils.getApp(),
            scanBroadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        Log.d(TAG, "registerScanReceiver===>")
    }

    override fun unregisterScanReceiver() {
        if (!isUrovo) return
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver)
        Log.d(TAG, "unregisterScanReceiver===>")
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        this.scanListener = scanListener
        Log.d(TAG, "registerScanListener===>")
    }


    //    companion object {
    private const val TAG = "UrovoDeviceHelper"
    private const val PRODUCT_MODEL_CT58 = "CT58"
    private const val PRODUCT_NAME_DT50_5G_EEA = "DT50_5G_EEA"
    private const val ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE
    private const val ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG
    /**
     * 这里面目前仅包含CT58,后续可以将优博讯的其他设备都通过这里适配
     */
    private val isUrovo: Boolean
        get() {
            val product = Build.PRODUCT.trim { it <= ' ' }
            val scannerType = ScannerManager.initScanner()
            Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null))
            if (scannerType == null) {
                return false
            }
            val urovo = ScannerType.UROVO == scannerType
            Log.d(TAG, "product: $product\turovo: $urovo")
            return urovo
        }
//    }
}
