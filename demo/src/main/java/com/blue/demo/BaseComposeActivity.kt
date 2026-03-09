package com.blue.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blue.lib_phone_scanner.hms_util.HmsPhoneScanUtil
import com.blue.lib_scanner.ScannerManager
import com.blue.lib_scanner.inner.IScannerDeviceHelper
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan

open class BaseComposeActivity : ComPermissionActivity() {
    private lateinit var scannerManager: ScannerManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initScanner()
    }

    override fun onResume() {
        super.onResume()
        registerScanner()
        if (this::scannerManager.isInitialized) {
            scannerManager.registerScanListener(object :IScannerDeviceHelper.OnScanListener{
                override fun onReceivedScanCode(message: String?) {
                    receiveScanResult(message)
                }
            })

        }
    }

    override fun onPause() {
        super.onPause()
        unregisterScanner()
    }

    protected fun startCameraScan() {
        HmsPhoneScanUtil.startCameraScan(HmsPhoneScanUtil.CAMERA_REQUEST_CODE)
    }

    open fun receiveDeviceCommonScanResult(result: String, usb: Boolean) {
        Log.d("scanResult", "=========$result=========")

    }

    open fun receiveScanResult(qrcode: String?) {
        if (ObjectUtils.isEmpty(qrcode)) return
        this.receiveDeviceCommonScanResult(qrcode!!, false)
    }

    private fun initScanner() {
        if (!this::scannerManager.isInitialized) {
            scannerManager = ScannerManager
        }

    }
    private fun registerScanner() {
        if (this::scannerManager.isInitialized) {
            scannerManager.registerScanner()
        }

    }

    private fun unregisterScanner() {
        if (this::scannerManager.isInitialized) {
            scannerManager.unregisterScanner()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HmsPhoneScanUtil.CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val parcelableExtra = data?.getParcelableExtra<HmsScan>(ScanUtil.RESULT)
            parcelableExtra?.let {
                receiveScanResult(parcelableExtra.originalValue)
            }
        }
    }
    fun ComponentActivity.finishDelayExt() {
        ThreadUtils.getMainHandler().postDelayed({ this.finish() }, 500)
    }


}