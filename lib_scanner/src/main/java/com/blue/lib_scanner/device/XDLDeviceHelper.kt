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
 * NLS-MT90/NLS-MT95L
 */
object XDLDeviceHelper : IScannerDeviceHelper {
    private var scanListener: OnScanListener? = null
    private val scanBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                var finalScanData = intent.getStringExtra(ACTION_KEY_DEFAULT)
                try {
                    val nls1 = intent.getStringExtra(ACTION_KEY_DEFAULT_NLS_1)
                    val nls2 = intent.getStringExtra(ACTION_KEY_DEFAULT_NLS_2)
                    if (ObjectUtils.isEmpty(finalScanData)) {
                        finalScanData = nls1
                    }
                    if (ObjectUtils.isEmpty(finalScanData) && ObjectUtils.isNotEmpty(nls2)) {
                        finalScanData = nls2
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                if (scanListener != null) {
                    scanListener!!.onReceivedScanCode(finalScanData)
                }
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: $finalScanData")
            }
        }
    }

    override fun registerScanReceiver() {
        if (!isXDL) return
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_NAME_DEFAULT)
        intentFilter.addAction(ACTION_NAME_DEFAULT_NLS)
        ContextCompat.registerReceiver(
            Utils.getApp(),
            scanBroadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        Log.d(TAG, "registerScanReceiver===>")
    }

    override fun unregisterScanReceiver() {
        if (!isXDL) return
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver)
        Log.d(TAG, "unregisterScanReceiver===>")
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        this.scanListener = scanListener
        Log.d(TAG, "registerScanListener===>")
    }


    //    companion object {
    private const val TAG = "XDLDeviceHelper"
    private const val PRODUCT_NAME_MT95L = "NLS-MT95L"
    private const val PRODUCT_NAME_MT90 = "NLS-MT90"
    private const val ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE //NLS-MT90
    private const val ACTION_NAME_DEFAULT_NLS = "nlscan.action.SCANNER RESULT" //NLS-MT95
    private const val ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG
    private const val ACTION_KEY_DEFAULT_NLS_1 = "SCAN_BARCODE1"
    private const val ACTION_KEY_DEFAULT_NLS_2 = "SCAN_BARCODE2"
    private val isXDL: Boolean
        get() {
            val product = Build.PRODUCT.trim { it <= ' ' }
            val scannerType = ScannerManager.initScanner()
            Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null))
            if (scannerType == null) {
                return false
            }
            val xdl = ScannerType.NEWLAND == scannerType
            Log.d(TAG, "product: $product\txdl: $xdl")
            return xdl
        }
//    }
}
