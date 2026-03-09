package com.blue.lib_scanner.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.Utils
import com.blue.lib_scanner.ScannerManager
import com.blue.lib_scanner.ScannerType
import com.blue.lib_scanner.inner.IScannerDeviceHelper
import com.blue.lib_scanner.inner.IScannerDeviceHelper.OnScanListener
import java.io.ByteArrayOutputStream

/**
 * 目前所知，有两种不同的设备，但是的他们的信息是相同的（如：brand: alps	product: full_k62v1_64_bsp）
 */
object AlpsDeviceHelper : IScannerDeviceHelper {
    private var scanListener: OnScanListener? = null
    private val scanBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                try {
                    var byteArrayExtra: ByteArray? = null
                    var scanData3: String? = null
                    if (intent.action == ACTION_NAME_DEFAULT) {
                        byteArrayExtra = intent.getByteArrayExtra(ACTION_KEY_DEFAULT)
                    }
                    if (intent.action == ACTION_NAME_DEFAULT_2) {
                        byteArrayExtra = intent.getByteArrayExtra(ACTION_KEY_DEFAULT_2)
                    }
                    if (intent.action == ACTION_NAME_DEFAULT_3) {
                        scanData3 = intent.getStringExtra(ACTION_KEY_DEFAULT_3)
                    }
                    if (ObjectUtils.isNotEmpty(scanData3)) {
                        scanListener?.onReceivedScanCode(scanData3)
                        Log.d(
                            TAG,
                            "scanBroadcastReceiver===> finalScanData: " + scanData3 + " length: " + scanData3!!.length + "  trim: " + scanData3.trim { it <= ' ' })
                    } else {
                        byteArrayExtra = removeCarriageReturn(byteArrayExtra)
                        if (ObjectUtils.isNotEmpty(byteArrayExtra)) {
                            val finalScanData = String(byteArrayExtra!!)
                            scanListener?.onReceivedScanCode(finalScanData)
                            Log.d(
                                TAG,
                                "scanBroadcastReceiver===> finalScanData: " + finalScanData + " length: " + finalScanData.length + "  trim: " + finalScanData.trim { it <= ' ' })
                        }
                    }
                } catch (e: Throwable) {
                    Log.e(TAG, "error: " + e.message)
                }
            }
        }
    }

    fun removeCarriageReturn(byteArray: ByteArray?): ByteArray? {
        if (byteArray == null || byteArray.isEmpty()) {
            return byteArray
        }

        // 创建一个ByteArrayOutputStream用于存储过滤后的字节
        val outputStream = ByteArrayOutputStream()
        for (b in byteArray) {
            // 如果不是回车('\r')字节，就写入outputStream
            if (b != '\r'.code.toByte()) {
                outputStream.write(b.toInt())
            } else {
                Log.e(TAG, "包含回车:=====>")
            }
        }

        // 返回过滤后的字节数组
        return outputStream.toByteArray()
    }

    override fun registerScanReceiver() {
        if (!isAlps) return
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_NAME_DEFAULT)
        intentFilter.addAction(ACTION_NAME_DEFAULT_2)
        intentFilter.addAction(ACTION_NAME_DEFAULT_3)
        ContextCompat.registerReceiver(
            Utils.getApp(),
            scanBroadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
        Log.d(TAG, "registerScanReceiver===>")
    }

    override fun unregisterScanReceiver() {
        if (!isAlps) return
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver)
        Log.d(TAG, "unregisterScanReceiver===>")
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        this.scanListener = scanListener
        Log.d(TAG, "registerScanListener===>")
    }


    //    companion object {
    private const val TAG = "AlpsDeviceHelper"
    private const val ACTION_NAME_DEFAULT = "com.action.SCAN_RESULT"
    private const val ACTION_NAME_DEFAULT_2 = "com.rfid.SCAN" //利旧
    private const val ACTION_NAME_DEFAULT_3 = "android.scanservice.action.UPLOAD_BARCODE_DATA"
    private const val ACTION_KEY_DEFAULT = "scanContext"
    private const val ACTION_KEY_DEFAULT_2 = "data"
    private const val ACTION_KEY_DEFAULT_3 = "barcode"

    /**
     * 这里面目前仅包含CT58,后续可以将优博讯的其他设备都通过这里适配
     */
    private val isAlps: Boolean
        get() {
            val product = Build.PRODUCT.trim { it <= ' ' }
            val scannerType: ScannerType? = ScannerManager.initScanner()
            Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null))
            if (scannerType == null) {
                return false
            }
            val urovo = ScannerType.ALPS === scannerType
            Log.d(TAG, "product: $product\talps: $urovo")
            return urovo
        }
//    }
}
