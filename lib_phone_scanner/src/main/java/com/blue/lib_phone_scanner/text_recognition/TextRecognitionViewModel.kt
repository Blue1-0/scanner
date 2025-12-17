package com.blue.lib_phone_scanner.text_recognition

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TextRecognitionViewModel : ViewModel() {


    private val _state = MutableStateFlow(TextRecognitionState())
    val state: StateFlow<TextRecognitionState> = _state.asStateFlow()

    private val analyzer =
        TextRecognitionAnalyzer(ChineseTextRecognizerOptions.Builder().build())
    private var mExecutorService: ExecutorService = Executors.newSingleThreadExecutor()


    fun selectImg(callback: (String) -> Unit) {
        imagePicker(1) { imgList ->
            if (ObjectUtils.isNotEmpty(imgList)) {
                _state.update { it.copy(imgPath = imgList[0]) }
                val imgPath = _state.value.imgPath
                if (ObjectUtils.isNotEmpty(imgPath)) {
                    detectInImage(callback)
                }
            }
        }
    }

    private fun showDialog(text: String, callback: (String) -> Unit) {
        AndroidTextSelectionDialog.show(
            context = ActivityUtils.getTopActivity(),
            text = text,
            onCopy = {
                callback(it)
//                Toaster.show(it)
            }
        )
    }

    fun detectInImage(callback: (String) -> Unit) {
        val imgPath = _state.value.imgPath
        if (ObjectUtils.isEmpty(imgPath)) {
            ToastUtils.showLong("请选择图片后操作")
            return
        }
        viewModelScope.launch {
            try {
                val file = FileUtils.getFileByPath(imgPath);
                if (file != null && file.exists()) {
                    val decodeFile = BitmapFactory.decodeFile(file.absolutePath)
                    val inputImage = InputImage.fromBitmap(decodeFile, 0)
                    analyzer.mDetector.process(inputImage)
                        .addOnSuccessListener { text ->
                            Log.d("detectInImage", "==解析结果== ${text.text}")
                            viewModelScope.launch {
                                _state.update { it.copy(analyzerResultText = text.text) }
                            }
                            if (ObjectUtils.isNotEmpty(text.text)) {
                                showDialog(text.text, callback)
                            }
                        }
                        .addOnFailureListener { e ->
                            viewModelScope.launch {
                                _state.update { it.copy(analyzerResultText = "失败：${e.message}") }
                            }
                            Log.d("detectInImage", "==解析结果Failed== ${e.message}")
                        }
                }
            } catch (e: Throwable) {
                Log.d("detectInImage", "==解析结果catch== ${e.message}")
            }
        }


//        val cameraConfig = CameraConfigFactory.createDefaultCameraConfig(
//            Utils.getApp(),
//            CameraSelector.LENS_FACING_UNKNOWN
//        )
//        cameraConfig.options(Preview.Builder()).
//        // 图像分析
//        val imageAnalysis: ImageAnalysis = cameraConfig.options(
//            ImageAnalysis.Builder()
//                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//        )
//
//        ImageAnalysis.Builder().set
//
//        imageAnalysis.setAnalyzer(mExecutorService) { image ->
//            analyzer.analyze(image, object : Analyzer.OnAnalyzeListener<Text> {
//                override fun onSuccess(result: AnalyzeResult<Text>) {
//                    val analyzerResultText = result.result.text
//                    Log.d("detectInImage", "==解析结果== $analyzerResultText")
//                    viewModelScope.launch {
//                        _state.update { it.copy(analyzerResultText = analyzerResultText) }
//                    }
//                }
//
//                override fun onFailure(e: Exception?) {
//                    viewModelScope.launch {
//                        _state.update { it.copy(analyzerResultText = "失败：${e?.message}") }
//                    }
//                    Log.d("detectInImage", "==解析结果== ${e?.message}")
//                    e?.printStackTrace()
//                }
//            })
//            image.close()
//        }

    }


}