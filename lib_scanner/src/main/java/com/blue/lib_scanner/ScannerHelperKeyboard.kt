package com.blue.lib_scanner

import android.util.Log
import android.view.KeyEvent
import com.blankj.utilcode.util.ObjectUtils

class ScannerHelperKeyboard {
    private var stringBuffer = StringBuffer()
    private var lastKeyTime = 0L
    private var callback: Callback? = null
    private var isShiftPressed = false
    private var startTime: Long = 0
    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent?) {
        if (keyCode == SHITF_KEY_CODE) {
            isShiftPressed = true
        } else {
            val currentTime = System.currentTimeMillis()
            val charString = if (isShiftPressed) {
                convertToStringUpperCase(keyCode)
            } else {
                convertToStringLowerCase(keyCode)
            }
            // String logString = "code = " + keyCode + ", charString = "
            // + charString + ", margin = " + (currentTime - lastKeyTime);
            // WLog.i("Alex", logString);
            if (currentTime - lastKeyTime > WAIT_TIME) {
                stringBuffer = StringBuffer()
                startTime = System.currentTimeMillis()
                stringBuffer.append(charString)
            } else {
                if (ObjectUtils.isNotEmpty(charString) && charString == "\n") {
                    // WLog.i("Alex", "扫码数据接收完毕：" + stringBuffer.toString());
                    val barcode = stringBuffer.toString()
                    if (callback != null) {
                        Log.i(
                            "Alex",
                            "本次识别花费时间：" + (System.currentTimeMillis() - startTime)
                        )
                        callback!!.onReceivedScanCode(barcode)
                    }
                } else {
                    stringBuffer.append(charString)
                }
            }
            lastKeyTime = currentTime
        }
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent?) {
        if (keyCode == SHITF_KEY_CODE) {
            isShiftPressed = false
        }
    }

    private fun convertToStringLowerCase(keyCode: Int): String {
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            val code = keyCode - KeyEvent.KEYCODE_0 + '0'.code
            return code.toChar().toString() + ""
        }
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            val code = keyCode - KeyEvent.KEYCODE_A + 'a'.code
            return code.toChar().toString() + ""
        }
        return when (keyCode) {
            KeyEvent.KEYCODE_GRAVE -> "`"
            KeyEvent.KEYCODE_MINUS -> "-"
            KeyEvent.KEYCODE_EQUALS -> "="
            KeyEvent.KEYCODE_LEFT_BRACKET -> "["
            KeyEvent.KEYCODE_RIGHT_BRACKET -> "]"
            KeyEvent.KEYCODE_BACKSLASH -> "\\"
            KeyEvent.KEYCODE_SEMICOLON -> ";"
            KeyEvent.KEYCODE_APOSTROPHE -> "'"
            KeyEvent.KEYCODE_COMMA -> ","
            KeyEvent.KEYCODE_PERIOD -> "."
            KeyEvent.KEYCODE_SLASH -> "/"
            KeyEvent.KEYCODE_SPACE -> " "
            KeyEvent.KEYCODE_ENTER -> "\n"
            else -> "■"
        }
    }

    private fun convertToStringUpperCase(keyCode: Int): String {
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            val code = keyCode - KeyEvent.KEYCODE_A + 'A'.code
            return code.toChar().toString() + ""
        }
        when (keyCode) {
            KeyEvent.KEYCODE_1 -> return "!"
            KeyEvent.KEYCODE_2 -> return "@"
            KeyEvent.KEYCODE_3 -> return "#"
            KeyEvent.KEYCODE_4 -> return "$"
            KeyEvent.KEYCODE_5 -> return "%"
            KeyEvent.KEYCODE_6 -> return "^"
            KeyEvent.KEYCODE_7 -> return "&"
            KeyEvent.KEYCODE_8 -> return "*"
            KeyEvent.KEYCODE_9 -> return "("
            KeyEvent.KEYCODE_0 -> return ")"
        }
        return when (keyCode) {
            KeyEvent.KEYCODE_GRAVE -> "~"
            KeyEvent.KEYCODE_MINUS -> "_"
            KeyEvent.KEYCODE_EQUALS -> "+"
            KeyEvent.KEYCODE_LEFT_BRACKET -> "{"
            KeyEvent.KEYCODE_RIGHT_BRACKET -> "}"
            KeyEvent.KEYCODE_BACKSLASH -> "|"
            KeyEvent.KEYCODE_SEMICOLON -> ":"
            KeyEvent.KEYCODE_APOSTROPHE -> "\""
            KeyEvent.KEYCODE_COMMA -> "<"
            KeyEvent.KEYCODE_PERIOD -> ">"
            KeyEvent.KEYCODE_SLASH -> "?"
            KeyEvent.KEYCODE_SPACE -> " "
            KeyEvent.KEYCODE_ENTER -> "\n"
            else -> "■"
        }
    }

    interface Callback {
        fun onReceivedScanCode(barcode: String?)
    }

    companion object {
        private const val SHITF_KEY_CODE = 59
        private const val WAIT_TIME = 200
    }
}
