package com.blue.lib_scanner.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.Utils
import com.blue.lib_scanner.inner.IScannerDeviceHelper
import com.blue.lib_scanner.inner.IScannerDeviceHelper.OnScanListener
import java.io.ByteArrayOutputStream

/**
 * @author blue
 * @desc HandHeldTerminal
 */
object HandHeldTerminalDeviceHelper : IScannerDeviceHelper {
    private var scanListener: OnScanListener? = null

    private val scanBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                try {
                    var byteArrayExtra: ByteArray? = null
                    var scanData3: String? = null
                    if (ACTION_NAME_DEFAULT == intent.action) {
                        byteArrayExtra = intent.getByteArrayExtra(ACTION_KEY_DEFAULT)
                    }
                    if (ACTION_NAME_DEFAULT == intent.action) {
                        scanData3 = intent.getStringExtra(ACTION_KEY_DEFAULT_1)
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
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver)
        Log.d(TAG, "unregisterScanReceiver===>")
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        this.scanListener = scanListener
        Log.d(TAG, "registerScanListener===>")
    }


    private const val TAG = "HandHeldTerminal"
    private const val ACTION_NAME_DEFAULT = "com.uc.scanner.result"
    private const val ACTION_KEY_DEFAULT = "byteArray"
    private const val ACTION_KEY_DEFAULT_1 = "string"
}
