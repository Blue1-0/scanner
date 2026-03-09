package com.blue.lib_scanner.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.Utils
import com.blue.lib_scanner.ScannerManager
import com.blue.lib_scanner.ScannerType
import com.blue.lib_scanner.inner.IScannerDeviceHelper
import com.blue.lib_scanner.inner.IScannerDeviceHelper.OnScanListener

object DJDeviceHelper : IScannerDeviceHelper {
    private var scanListener: OnScanListener? = null
    private val scanBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val scannerDataDefault = intent.getStringExtra(ACTION_KEY_DEFAULT)
                val scannerDataDj = intent.getStringExtra(ACTION_KEY_DJ)
                val finalScanData =
                    if (ObjectUtils.isNotEmpty(scannerDataDefault)) scannerDataDefault else scannerDataDj
                scanListener?.onReceivedScanCode(finalScanData)
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: $finalScanData")
            }
        }
    }

    override fun registerScanReceiver() {
        if (!isDJ) return
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_NAME_DEFAULT)
        intentFilter.addAction(ACTION_NAME_DJ)
        ContextCompat.registerReceiver(
            Utils.getApp(), scanBroadcastReceiver, intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        Log.d(TAG, "registerScanReceiver===>")
    }

    override fun unregisterScanReceiver() {
        if (!isDJ) return
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver)
        Log.d(TAG, "unregisterScanReceiver===>")
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        this.scanListener = scanListener
        Log.d(TAG, "registerScanListener===>")
    }


    private const val TAG = "DJDeviceHelper"
    private const val ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE
    private const val ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG
    private const val ACTION_NAME_DJ = "com.android.server.scannerservice.broadcast"
    private const val ACTION_KEY_DJ = "scannerdata"
    private val isDJ: Boolean
        get() {
            val product = Build.PRODUCT.trim { it <= ' ' }
            val scannerType: ScannerType? = ScannerManager.initScanner()
            Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null))
            if (scannerType == null) {
                return false
            }
            val dj = ScannerType.SEUIC === scannerType
            Log.d(TAG, "product: $product\tdj: $dj")
            return dj
        }
}
