package com.blue.lib_scanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import com.blankj.utilcode.util.Utils
import com.blue.lib_scanner.inner.IScannerDeviceHelper
import com.blue.lib_scanner.inner.IScannerDeviceHelper.OnScanListener

/**
 * @author Blue
 * @desc default device
 */
object PhoneScannerHelper : IScannerDeviceHelper {
    private const val SCAN_ACTION = ScanManager.ACTION_DECODE //default action

    private val mScanManager: ScanManager?
    private var onScanListener: OnScanListener? = null
    private val mScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG)
            val barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0)
            val temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, 0.toByte())
            val message = String(barcode!!, 0, barcodelen)
            OnReceiveData(message)
        }
    }

    init {
        mScanManager = ScanManager()
        mScanManager.openScanner()
        mScanManager.switchOutputMode(0)
    }

    fun register() {
        registerScanReceiver()
    }

    override fun registerScanReceiver() {
        if (mScanManager != null) {
            val filter = IntentFilter()
            val idbuf = intArrayOf(
                PropertyID.WEDGE_INTENT_ACTION_NAME,
                PropertyID.WEDGE_INTENT_DATA_STRING_TAG
            )
            val value_buf = mScanManager.getParameterString(idbuf)
            if (value_buf != null && value_buf[0] != null && value_buf[0] != "") {
                filter.addAction(value_buf[0])
            } else {
                filter.addAction(SCAN_ACTION)
            }
            Utils.getApp().registerReceiver(mScanReceiver, filter)
        }
    }

    fun unregister() {
        unregisterScanReceiver()
    }

    override fun unregisterScanReceiver() {
        mScanManager?.stopDecode()
        Utils.getApp().unregisterReceiver(mScanReceiver)
    }



    override fun registerScanListener(scanListener: OnScanListener?) {
        onScanListener = scanListener
    }

    private fun OnReceiveData(message: String) {
        if (onScanListener != null) {
            onScanListener!!.onReceivedScanCode(message)
        }
    }


}
