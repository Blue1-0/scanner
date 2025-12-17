package com.blue.lib_phone_scanner.text_recognition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.TimeUtils
import com.blue.lib_phone_scanner.ScanGlobalEvent

/**
 * 文字识别界面
 */
class TextRecognitionActivity : ComponentActivity() {
    private var requestCode: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        requestCode = intent.getIntExtra("requestCode", -1)
        setContent {
            TextRecognitionScreen(
                onBack = {
                    finish()
                },
                decodeImageCallback = { result ->
                    if (ObjectUtils.isEmpty(result)) {
                        return@TextRecognitionScreen
                    }
                    //发送全局事情，
                    ScanGlobalEvent.sendEvent(
                        ScanGlobalEvent.GlobalIntent.ReceiveQrCode(
                            data = result,
                            timeNow = TimeUtils.getNowString(),
                            requestCode = requestCode
                        )
                    )
                    finish()


                }
            )
        }
    }
}