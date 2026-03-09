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
 * @desc SmartPhone(ZJ-v9100)
 */
object SmartPhoneDeviceHelper : IScannerDeviceHelper {
    private var scanListener: OnScanListener? = null
    private val scanBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                var finalScanData = intent.getStringExtra(ACTION_KEY_DEFAULT)

                if (ObjectUtils.isEmpty(finalScanData)) {
                    finalScanData = intent.getStringExtra(ACTION_KEY_DEFAULT_ZJ)
                }

                scanListener?.onReceivedScanCode(finalScanData)
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: $finalScanData")
            }
        }
    }

    override fun registerScanReceiver() {
        if (!isSmartphone) return
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_NAME_DEFAULT)
        intentFilter.addAction(ACTION_NAME_DEFAULT_ZJ)
        ContextCompat.registerReceiver(
            Utils.getApp(),
            scanBroadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        Log.d(TAG, "registerScanReceiver===>")
    }

    override fun unregisterScanReceiver() {
        if (!isSmartphone) return
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver)
        Log.d(TAG, "unregisterScanReceiver===>")
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        this.scanListener = scanListener
        Log.d(TAG, "registerScanListener===>")
    }


    //    companion object {
    private const val TAG = "SmartPhoneDeviceHelper"
    private const val ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE

    private const val ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG

    private const val ACTION_NAME_DEFAULT_ZJ = "ACTION_BAR_SCAN"
    private const val ACTION_KEY_DEFAULT_ZJ = "EXTRA_SCAN_DATA"
    private val isSmartphone: Boolean
        get() {
            val product = Build.PRODUCT.trim { it <= ' ' }
            val scannerType = ScannerManager.initScanner()
            Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null))
            if (scannerType == null) {
                return false
            }
            val smartphone = ScannerType.SMARTPHONE == scannerType
            Log.d(TAG, "product: $product\tsmartphone: $smartphone")
            return smartphone
        }
//    }
}
