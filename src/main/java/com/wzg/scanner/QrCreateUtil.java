package com.wzg.scanner;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

public class QrCreateUtil {


    public static Bitmap generateQRCode(String content, int width, int height) {
        try {
            // 创建二维码
            HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator()
                    .setBitmapBackgroundColor(Color.WHITE)
                    .setBitmapColor(Color.BLACK)
                    .setBitmapMargin(1)
                    .create();
            Bitmap qrCode = ScanUtil.buildBitmap(content, HmsScan.QRCODE_SCAN_TYPE, width, height, options);
            return qrCode;
        } catch (WriterException e) {
            Log.e("GenerateQRCode", "Failed to generate QR code: " + e.getMessage());
            return null;
        }
    }

}
