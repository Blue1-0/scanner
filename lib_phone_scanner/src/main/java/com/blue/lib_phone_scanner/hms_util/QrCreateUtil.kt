package com.blue.lib_phone_scanner.hms_util

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.hmsscankit.WriterException
import com.huawei.hms.ml.scan.HmsBuildBitmapOption
import com.huawei.hms.ml.scan.HmsScan

object QrCreateUtil {
    fun generateQRCode(content: String?, width: Int, height: Int): Bitmap? {
        return try {
            // 创建二维码
            val options = HmsBuildBitmapOption.Creator()
                .setBitmapBackgroundColor(Color.WHITE)
                .setBitmapColor(Color.BLACK)
                .setBitmapMargin(1)
                .create()
            ScanUtil.buildBitmap(content, HmsScan.QRCODE_SCAN_TYPE, width, height, options)
        } catch (e: WriterException) {
            Log.e("GenerateQRCode", "Failed to generate QR code: " + e.message)
            null
        }
    }
}
