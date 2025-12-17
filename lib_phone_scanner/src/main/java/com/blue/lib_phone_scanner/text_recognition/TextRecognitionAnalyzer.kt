package com.blue.lib_phone_scanner.text_recognition

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.king.mlkit.vision.common.analyze.CommonAnalyzer
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognitionAnalyzer constructor(
    options: TextRecognizerOptionsInterface?
) : CommonAnalyzer<Text>() {

    public var mDetector :TextRecognizer

    init {
        mDetector = if (options!=null){
            TextRecognition.getClient(options)
        }else{
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        }
    }

    override fun detectInImage(inputImage: InputImage): Task<Text> {
        return mDetector.process(inputImage)
    }
}