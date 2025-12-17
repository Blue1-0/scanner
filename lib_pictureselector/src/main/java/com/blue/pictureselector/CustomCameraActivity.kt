package com.blue.pictureselector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.blue.pictureselector.custom.AutoTexturePreviewView
import com.blue.pictureselector.custom.CameraPreviewManager
import com.blue.pictureselector.custom.CustomConfigCenter
import com.blue.pictureselector.custom.CustomRotation

class CustomCameraActivity : AppCompatActivity() {

    companion object {
        private const val PREFER_WIDTH = 1280
        private const val PREFER_HEIGHT = 1024

    }

    private var autoTexturePreview: AutoTexturePreviewView? = null
    private var currentRotation = CustomConfigCenter.CAMERA_ROTATION
    private var requestCode = 0
    private var cameraPreviewManager: CameraPreviewManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        setContentView(R.layout.activity_custom_camera)
        addBarHeight()

        requestCode = intent.getIntExtra("requestCode", 0)
        autoTexturePreview = findViewById(R.id.preview_view)

        // 初始化相机管理器
        cameraPreviewManager = CameraPreviewManager.getInstance()
        startCameraPreview(currentRotation)
        findViewById<View>(R.id.flBack).setOnClickListener { finish() }
        val takePhoto = findViewById<ImageView>(R.id.btn_take_photo)
        takePhoto.setOnClickListener { takePhoto() }

        //只测试，不让用户操作
        val btnRotate = findViewById<ImageView>(R.id.btn_rotate)
        btnRotate.setOnClickListener { changeRotation() }
        btnRotate.visibility = View.INVISIBLE
    }

    private fun addBarHeight() {
        try {
            val headLayout = findViewById<RelativeLayout>(R.id.headerLayout)
            val statusBarHeight = BarUtils.getStatusBarHeight()
            Log.e("statusBarHeight", "statusBarHeight: $statusBarHeight")
            val layoutParams = headLayout.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = statusBarHeight
            headLayout.layoutParams = layoutParams
        } catch (e: Throwable) {
            Log.e("statusBarHeight", "error: ${e.message}")
        }
    }

    override fun onPause() {
        super.onPause()
        cameraPreviewManager?.stopPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraPreviewManager?.stopPreview()
    }

    private fun startCameraPreview(cameraRotation: Int) {
        // 启动预览（根据需求设置宽高）
        cameraPreviewManager?.cameraFacing = CameraPreviewManager.CAMERA_FACING_FRONT
        cameraPreviewManager?.startPreview(
            this,
            autoTexturePreview,
            PREFER_WIDTH,
            PREFER_HEIGHT,
            cameraRotation
        ) { _, _, _, _ -> }
    }

    /** 动态修改旋转角度（例如按钮点击切换横竖屏）
     *  每次 +90
     */

    private fun changeRotation() {
        currentRotation = when (currentRotation) {
            Surface.ROTATION_0 -> Surface.ROTATION_90
            Surface.ROTATION_90 -> Surface.ROTATION_180
            Surface.ROTATION_180 -> Surface.ROTATION_270
            else -> Surface.ROTATION_0
        }
        ToastUtils.showLong("方向：$currentRotation")
        cameraPreviewManager?.stopPreview()
        when (currentRotation) {
            Surface.ROTATION_0 -> startCameraPreview(CustomRotation.ROTATION_0)
            Surface.ROTATION_90 -> startCameraPreview(CustomRotation.ROTATION_90)
            Surface.ROTATION_180 -> startCameraPreview(CustomRotation.ROTATION_180)
            Surface.ROTATION_270 -> startCameraPreview(CustomRotation.ROTATION_270)
        }
    }

    // 拍照逻辑
    private fun takePhoto() {
        cameraPreviewManager?.getmCamera()
            ?.takePicture(null, null) { data, _ ->
                // 保存图片并返回结果
                saveAndReturnResult(data)
            }
    }

    private fun saveAndReturnResult(data: ByteArray) {
        ThreadUtils.executeByIo(object : ThreadUtils.SimpleTask<String?>() {
            override fun doInBackground(): String? {
                // 直接通过 BitmapFactory 解码 byte 数组
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                // 简单判空（防止数据损坏）
                if (bitmap == null) {
                    ToastUtils.showLong("Failed to decode byte array to Bitmap")
                    return null
                }
                val rotation90 = rotateBitmapRight90(bitmap)
                if (rotation90 == null) {
                    ToastUtils.showLong(" rotation90 is empty")
                    return null
                }
                val file = ImageUtils.save2Album(rotation90, Bitmap.CompressFormat.JPEG, 70, true)
                return file?.absolutePath
            }

            override fun onSuccess(result: String?) {
                if (result != null) {
                    val resultIntent = Intent().putExtra("filePath", result)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    ToastUtils.showLong("Failed is empty")
                }
            }
        })

    }

    /**
     * 将 Bitmap 向右旋转90度（顺时针）
     * @param source 原始 Bitmap
     * @return 旋转后的新 Bitmap
     */
    private fun rotateBitmapRight90(source: Bitmap?): Bitmap? {
        if (source == null) return null
        val matrix = Matrix()
        matrix.postRotate(currentRotation.toFloat()) // 设置顺时针旋转90度
        // 创建新 Bitmap（注意交换宽高）
        return Bitmap.createBitmap(
            source,
            0, 0,
            source.width,
            source.height,
            matrix,
            true
        )
    }
}