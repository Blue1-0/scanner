package com.blue.lib_phone_scanner

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blue.lib_phone_scanner.hms_util.HmsPhoneScanUtil

typealias ScanResultCallback = (requestCode: Int, timeNow: String, data: String) -> Unit

/**
 * @author Blue
 * @desc 扫描类
 */
object PhoneScanUtil {

    /**step1:
     * register manifest ScanQrOfZXingActivity
     * step2:
     * register ScanGlobalEvent.GlobalIntent.ReceiveQrCode
     */
    fun startCameraScan(requestCode: Int = -1) = startCameraScanOfZxing(requestCode)

    /**
     * 需要自己处理onActivityResult 【data?.getParcelableExtra<HmsScan>(ScanUtil.RESULT)】
     */
    fun startCameraScanOfHms(requestCode: Int = -1) {
        HmsPhoneScanUtil.startCameraScan(requestCode)
    }

    /**step1:
     * register manifest ScanQrOfZXingActivity
     * step2:
     * register ScanGlobalEvent.GlobalIntent.ReceiveQrCode
     */
    fun startCameraScanOfZxing(requestCode: Int = -1) {
        val topActivity = ActivityUtils.getTopActivity()
        val intent = Intent(topActivity, ScanQrOfZXingActivity::class.java).apply {
            putExtra("requestCode", requestCode)
        }
        topActivity.startActivityForResult(intent, requestCode)
    }

    /**
     * step1:
     * register manifest ScanQrActivity
     * step2:
     * register ScanGlobalEvent.GlobalIntent.ReceiveQrCode
     */
    fun startCameraScanOfMlKit(requestCode: Int = -1) {
        val topActivity = ActivityUtils.getTopActivity()
        val intent = Intent(topActivity, ScanQrActivity::class.java).apply {
            putExtra("requestCode", requestCode)
        }
        topActivity.startActivityForResult(intent, requestCode)
    }

    /**
     * 支持直接监听，也可以使用ScanGlobalEvent自己监听回调，都是一样的
     */
    suspend fun registerScanCallback(scanCallback: ScanResultCallback) {
        ScanGlobalEvent.globalEvent.collect { event ->
            if (event is ScanGlobalEvent.GlobalIntent.ReceiveQrCode) {
                scanCallback(
                    event.requestCode,
                    event.timeNow,
                    event.data
                )
            }
        }
    }
}