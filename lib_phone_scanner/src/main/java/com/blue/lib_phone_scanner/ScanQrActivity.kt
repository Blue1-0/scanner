package com.blue.lib_phone_scanner

import android.content.Intent
import android.graphics.Point
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.TimeUtils
import com.blue.lib_phone_scanner.ext.colorExt
import com.blue.lib_phone_scanner.ext.setScaleClickListener
import com.google.mlkit.vision.barcode.common.Barcode
import com.king.camera.scan.AnalyzeResult
import com.king.camera.scan.CameraScan
import com.king.camera.scan.analyze.Analyzer
import com.king.camera.scan.util.PointUtils
import com.king.mlkit.vision.barcode.QRCodeCameraScanActivity
import com.king.mlkit.vision.barcode.analyze.BarcodeScanningAnalyzer
import com.blue.lib_phone_scanner.text_recognition.TextRecognitionActivity

/**
 * @author Blue
 * @desc  qrCode  scan
 */
class ScanQrActivity : QRCodeCameraScanActivity() {
    private var requestCode: Int = -1
    override fun createAnalyzer(): Analyzer<MutableList<Barcode>> {
        return BarcodeScanningAnalyzer(Barcode.FORMAT_ALL_FORMATS)
    }


    override fun initCameraScan(cameraScan: CameraScan<MutableList<Barcode>>) {
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
        requestCode = intent.getIntExtra("requestCode", -1)
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

    private fun startTextRecognitionActivity() {
        val intent = Intent(this@ScanQrActivity, TextRecognitionActivity::class.java)
        intent.putExtra("requestCode", requestCode)
        startActivity(intent)
        finish()
    }


    override fun onScanResultCallback(result: AnalyzeResult<MutableList<Barcode>>) {
        // 停止分析
        cameraScan.setAnalyzeImage(false)
        val results = result.result

        val points = ArrayList<Point>()
        val width = result.imageWidth
        val height = result.imageHeight
        for (barcode in results) {
            barcode.boundingBox?.let { box ->
                //将实际的结果中心点坐标转换成界面预览的坐标
                val point = PointUtils.transform(
                    box.centerX(),
                    box.centerY(),
                    width,
                    height,
                    viewfinderView.width,
                    viewfinderView.height
                )
                points.add(point)
            }
        }
        //设置Item点击监听
        viewfinderView.setOnItemClickListener {
            //显示点击Item将所在位置扫码识别的结果返回
            val displayValue = results[it].displayValue
            processScanResult(displayValue)
            /*
                显示结果后，如果需要继续扫码，则可以继续分析图像
             */
//            ivResult.setImageResource(0)
//            viewfinderView.showScanner()
//            cameraScan.setAnalyzeImage(true)
        }
        //显示结果点信息
        viewfinderView.showResultPoints(points)

        if (results.size == 1) {//只有一个结果直接返回
            val displayValue = results[0].displayValue
            processScanResult(displayValue)
        }

    }

    private fun processScanResult(displayValue: String?) {
        if (ObjectUtils.isNotEmpty(displayValue)) {
            ScanGlobalEvent.sendEvent(
                ScanGlobalEvent.GlobalIntent.ReceiveQrCode(
                    data = displayValue!!,
                    timeNow = TimeUtils.getNowString(),
                    requestCode = requestCode
                )
            )
//            val intent = Intent()
//            intent.putExtra(CameraScan.SCAN_RESULT, results[0].displayValue)
//            setResult(RESULT_OK, intent)
            finish()
        }
    }


}