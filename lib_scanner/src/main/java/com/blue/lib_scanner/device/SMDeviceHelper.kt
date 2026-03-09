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

/**
 * @author Blue
 * @desc 商米设备
 */
object SMDeviceHelper : IScannerDeviceHelper {
    private var scanListener: OnScanListener? = null
    private val scanBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val sunmiScanData = intent.getStringExtra(ACTION_KEY_SUNMI)
                val defaultScanData = intent.getStringExtra(ACTION_KEY_DEFAULT)
                val finalScanData =
                    if (ObjectUtils.isNotEmpty(sunmiScanData)) sunmiScanData else defaultScanData
                scanListener?.onReceivedScanCode(finalScanData)
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: $finalScanData")
            }
        }
    }

    override fun registerScanReceiver() {
        if (!isSUNMI) return
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_NAME_SUNMI)
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
        if (!isSUNMI) return
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver)
        Log.d(TAG, "unregisterScanReceiver===>")
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        this.scanListener = scanListener
        Log.d(TAG, "registerScanListener===>")
    }


    //    companion object {
    private const val TAG = "SMDeviceHelper"
    const val PRODUCT_NAME = "L2s_PRO"
    private const val ACTION_NAME_SUNMI = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED"
    private const val ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE
    private const val ACTION_KEY_SUNMI = "data"
    private const val ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG
    private const val ACTION_BYTE_NAME = "source_byte"
    private val isSUNMI: Boolean
        get() {
            val product = Build.PRODUCT.trim { it <= ' ' }
            val scannerType = ScannerManager.initScanner()
            Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null))
            if (scannerType == null) {
                return false
            }
            val sunmi = ScannerType.SUNMI == scannerType
            Log.d(TAG, "product: $product\tsunmi: $sunmi")
            return sunmi
        }
//    }
}
