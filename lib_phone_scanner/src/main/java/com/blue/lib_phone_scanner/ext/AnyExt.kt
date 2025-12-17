package com.blue.lib_phone_scanner.ext

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.Utils
//import com.google.gson.reflect.TypeToken
import java.math.BigDecimal

/**
 * @author zhenGui Wen
 */

fun Any?.isNotEmptyExt():Boolean{
    return ObjectUtils.isNotEmpty(this)
}
fun Any?.isNullExt():Boolean{
    return ObjectUtils.isEmpty(this)
}

fun String?.getOrNullToString(): String {
    if (this.isNullOrBlank() || this.equals("null", true)) return ""
    return this
}
//inline fun <reified T> Any?.deepCopy(obj: T?): T? {
//    return try {
//        val toJson = com.blankj.utilcode.util.GsonUtils.toJson(obj)
//        GsonUtils.fromJson(toJson, object : TypeToken<T>() {}.type)
//    } catch (e: Exception) {
//        null
//    }
//
//}
//inline fun <reified T> Any?.deepCopy(): T? {
//    return try {
//        val toJson = com.blankj.utilcode.util.GsonUtils.toJson(this)
//        GsonUtils.fromJson(toJson, object : TypeToken<T>() {}.type)
//    } catch (e: Exception) {
//        null
//    }
//
//}


@androidx.annotation.ColorInt
fun Int.colorExt(): Int {
    return ContextCompat.getColor(Utils.getApp(), this)
}

fun Int.drawableExt(): Drawable? {
    return ContextCompat.getDrawable(Utils.getApp(), this)
}


fun TextView?.setTextBold() {
    this?.setTypeface(null, Typeface.BOLD)
}

fun TextView?.setTextNormal() {
    this?.setTypeface(null, Typeface.NORMAL)
}

fun TextView?.getTextValue(): String {
    return this?.text.toString().trim()
}

fun TextView?.getTextToInt(): Int {
    try {
        return this.getTextValue().toInt()
    } catch (_: Throwable) {
    }
    return 0
}
fun String?.toBigDecimalExt(): BigDecimal {
    val arg0 = if (this.isNullOrEmpty()) "0" else this?.replace(" ", "")
    try {
        return BigDecimal(arg0)
    } catch (_: Throwable) {
    }
    return BigDecimal.ZERO
}


