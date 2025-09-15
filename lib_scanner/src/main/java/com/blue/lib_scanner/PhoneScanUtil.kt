package com.blue.lib_scanner

import com.blankj.utilcode.util.ActivityUtils
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions

/**
 * 手机扫描
 */
object PhoneScanUtil {
    const val CAMERA_REQUEST_CODE = 1998
    fun startCameraScan(requestCode: Int) {
        ScanUtil.startScan(
            ActivityUtils.getTopActivity(),
            requestCode,
            HmsScanAnalyzerOptions.Creator()
                .setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).create()
        )
    }
}