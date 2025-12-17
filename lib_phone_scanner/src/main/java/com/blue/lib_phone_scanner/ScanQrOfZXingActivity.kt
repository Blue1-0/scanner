package com.blue.lib_phone_scanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.TimeUtils
import com.blue.lib_phone_scanner.ext.colorExt
import com.blue.lib_phone_scanner.ext.setScaleClickListener
import com.google.zxing.Result
import com.king.camera.scan.AnalyzeResult
import com.king.camera.scan.CameraScan
import com.king.camera.scan.analyze.Analyzer
import com.king.zxing.BarcodeCameraScanActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.blue.lib_phone_scanner.text_recognition.TextRecognitionActivity


/**
 * @author Blue
 * @desc  qrCode  scan of zxing
 */
class ScanQrOfZXingActivity : BarcodeCameraScanActivity() {

    private var requestCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCode = intent.getIntExtra("requestCode", -1)
    }

    override fun createAnalyzer(): Analyzer<Result> {
        val decodeConfig = DecodeConfig()
        decodeConfig.setHints(DecodeFormatManager.ALL_HINTS)
            .setFullAreaScan(false)
            .setAreaRectRatio(0.8f)
            .setAreaRectVerticalOffset(0)
            .setAreaRectHorizontalOffset(0)
        return MultiFormatAnalyzer(decodeConfig)
    }


    override fun initCameraScan(cameraScan: CameraScan<Result>) {
        super.initCameraScan(cameraScan)
        cameraScan.setPlayBeep(true)
            .setVibrate(true)
            .bindFlashlightView(ivFlashlight)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_scan_qr
    }


    private fun addBarHeight() {
        try {
            val headLayout = findViewById<ConstraintLayout>(R.id.headerRootView)
            val statusBarHeight = BarUtils.getStatusBarHeight()
            Log.e("statusBarHeight", "statusBarHeight: $statusBarHeight")
            val layoutParams = headLayout.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = statusBarHeight
            headLayout.layoutParams = layoutParams
        } catch (e: Throwable) {
            Log.e("statusBarHeight", "error: ${e.message}")
        }
    }

    override fun initUI() {
        super.initUI()
        BarUtils.setStatusBarColor(this, R.color.text_blue.colorExt())
        addBarHeight()
        findViewById<AppCompatImageView>(R.id.backIv).setOnClickListener {
            finish()
        }
        findViewById<TextView>(R.id.naviTitle).text = "扫码识别"

        findViewById<TextView>(R.id.naviRight).apply {
            text = "文字提取"
            visibility = View.VISIBLE
            setScaleClickListener {
                startTextRecognitionActivity()
            }
        }
    }

    override fun onScanResultCallback(result: AnalyzeResult<Result>) {
        // 停止分析
        cameraScan.setAnalyzeImage(false)


        ScanGlobalEvent.sendEvent(ScanGlobalEvent.GlobalIntent.ReceiveQrCode(
            data = result.result.text,
            timeNow = TimeUtils.getNowString(),
            requestCode = requestCode
        ))


//        val intent = Intent()
//        intent.putExtra(CameraScan.SCAN_RESULT, result.result.text)
//        setResult(RESULT_OK, intent)
        finish()

    }

    private fun startTextRecognitionActivity() {
        val intent = Intent(this@ScanQrOfZXingActivity, TextRecognitionActivity::class.java)
        intent.putExtra("requestCode", requestCode)
        startActivity(intent)
        finish()
    }


}