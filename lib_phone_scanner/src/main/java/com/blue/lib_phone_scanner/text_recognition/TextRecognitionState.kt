package com.blue.lib_phone_scanner.text_recognition

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextRecognitionState(
    val imgPath: String? = null,
    val analyzerResultText: String = ""
) : Parcelable