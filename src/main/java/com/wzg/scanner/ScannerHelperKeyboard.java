package com.wzg.scanner;

import android.util.Log;
import android.view.KeyEvent;


public class ScannerHelperKeyboard {

    private static final int SHITF_KEY_CODE = 59;
    private static final int WAIT_TIME = 200;
    private StringBuffer stringBuffer = new StringBuffer();
    private long lastKeyTime = 0L;
    private Callback callback;
    private boolean isShiftPressed = false;
    private long startTime;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == SHITF_KEY_CODE) {
            isShiftPressed = true;
        } else {
            long currentTime = System.currentTimeMillis();
            String charString;
            if (isShiftPressed) {
                charString = convertToStringUpperCase(keyCode);
            } else {
                charString = convertToStringLowerCase(keyCode);
            }
            // String logString = "code = " + keyCode + ", charString = "
            // + charString + ", margin = " + (currentTime - lastKeyTime);
            // WLog.i("Alex", logString);
            if (currentTime - lastKeyTime > WAIT_TIME) {
                stringBuffer = new StringBuffer();
                startTime = System.currentTimeMillis();
                stringBuffer.append(charString);
            } else {
                if (charString != null && charString.equals("\n")) {
                    // WLog.i("Alex", "扫码数据接收完毕：" + stringBuffer.toString());
                    String barcode = stringBuffer.toString();
                    if (this.callback != null) {
                        Log.i("Alex", "本次识别花费时间：" + (System.currentTimeMillis() - startTime));
                        this.callback.onReceivedScanCode(barcode);
                    }
                } else {
                    stringBuffer.append(charString);
                }
            }
            lastKeyTime = currentTime;
        }
    }

    public void onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == SHITF_KEY_CODE) {
            isShiftPressed = false;
        }
    }

    private String convertToStringLowerCase(int keyCode) {
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            int code = keyCode - KeyEvent.KEYCODE_0 + '0';
            return (char) code + "";
        }
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            int code = keyCode - KeyEvent.KEYCODE_A + 'a';
            return (char) code + "";
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_GRAVE:
                return "`";
            case KeyEvent.KEYCODE_MINUS:
                return "-";
            case KeyEvent.KEYCODE_EQUALS:
                return "=";
            case KeyEvent.KEYCODE_LEFT_BRACKET:
                return "[";
            case KeyEvent.KEYCODE_RIGHT_BRACKET:
                return "]";
            case KeyEvent.KEYCODE_BACKSLASH:
                return "\\";
            case KeyEvent.KEYCODE_SEMICOLON:
                return ";";
            case KeyEvent.KEYCODE_APOSTROPHE:
                return "'";
            case KeyEvent.KEYCODE_COMMA:
                return ",";
            case KeyEvent.KEYCODE_PERIOD:
                return ".";
            case KeyEvent.KEYCODE_SLASH:
                return "/";

            case KeyEvent.KEYCODE_SPACE:
                return " ";
            case KeyEvent.KEYCODE_ENTER:
                return "\n";
            default:
                return "■";
        }
    }

    private String convertToStringUpperCase(int keyCode) {
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            int code = keyCode - KeyEvent.KEYCODE_A + 'A';
            return (char) code + "";
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                return "!";
            case KeyEvent.KEYCODE_2:
                return "@";
            case KeyEvent.KEYCODE_3:
                return "#";
            case KeyEvent.KEYCODE_4:
                return "$";
            case KeyEvent.KEYCODE_5:
                return "%";
            case KeyEvent.KEYCODE_6:
                return "^";
            case KeyEvent.KEYCODE_7:
                return "&";
            case KeyEvent.KEYCODE_8:
                return "*";
            case KeyEvent.KEYCODE_9:
                return "(";
            case KeyEvent.KEYCODE_0:
                return ")";
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_GRAVE:
                return "~";
            case KeyEvent.KEYCODE_MINUS:
                return "_";
            case KeyEvent.KEYCODE_EQUALS:
                return "+";
            case KeyEvent.KEYCODE_LEFT_BRACKET:
                return "{";
            case KeyEvent.KEYCODE_RIGHT_BRACKET:
                return "}";
            case KeyEvent.KEYCODE_BACKSLASH:
                return "|";
            case KeyEvent.KEYCODE_SEMICOLON:
                return ":";
            case KeyEvent.KEYCODE_APOSTROPHE:
                return "\"";
            case KeyEvent.KEYCODE_COMMA:
                return "<";
            case KeyEvent.KEYCODE_PERIOD:
                return ">";
            case KeyEvent.KEYCODE_SLASH:
                return "?";

            case KeyEvent.KEYCODE_SPACE:
                return " ";
            case KeyEvent.KEYCODE_ENTER:
                return "\n";
            default:
                return "■";
        }
    }

    public interface Callback {

        public void onReceivedScanCode(String barcode);

    }

}
