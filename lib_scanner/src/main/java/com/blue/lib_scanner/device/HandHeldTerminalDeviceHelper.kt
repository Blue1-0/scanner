package com.blue.lib_scanner.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.blue.lib_scanner.inner.IScannerDeviceHelper;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * @author blue
 * @desc HandHeldTerminal
 */
public class HandHeldTerminalDeviceHelper implements IScannerDeviceHelper {
    private static final String TAG = "HandHeldTerminal";
    private static final String ACTION_NAME_DEFAULT = "com.uc.scanner.result";
    private static final String ACTION_KEY_DEFAULT = "byteArray";
    private static final String ACTION_KEY_DEFAULT_1 = "string";
    private OnScanListener scanListener;
    private final BroadcastReceiver scanBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                try {
                    byte[] byteArrayExtra = null;
                    String scanData3 = null;
                    if (Objects.equals(intent.getAction(), ACTION_NAME_DEFAULT)) {
                        byteArrayExtra = intent.getByteArrayExtra(ACTION_KEY_DEFAULT);
                    }
                    if (Objects.equals(intent.getAction(), ACTION_NAME_DEFAULT)) {
                        scanData3 = intent.getStringExtra(ACTION_KEY_DEFAULT_1);
                    }
                    if (ObjectUtils.isNotEmpty(scanData3)) {
                        if (scanListener != null) {
                            scanListener.onReceivedScanCode(scanData3);
                        }
                        Log.d(TAG, "scanBroadcastReceiver===> finalScanData: " + scanData3 + " length: " + (scanData3.length()) + "  trim: " + (scanData3.trim()));
                    } else {
                        byteArrayExtra = removeCarriageReturn(byteArrayExtra);
                        String finalScanData = new String(byteArrayExtra);
                        if (scanListener != null) {
                            scanListener.onReceivedScanCode(finalScanData);
                        }
                        Log.d(TAG, "scanBroadcastReceiver===> finalScanData: " + finalScanData + " length: " + (finalScanData.length()) + "  trim: " + (finalScanData.trim()));
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "error: " + e.getMessage());
                }


            }
        }
    };

    public byte[] removeCarriageReturn(byte[] byteArray) {
        if (byteArray == null || byteArray.length == 0) {
            return byteArray;
        }

        // 创建一个ByteArrayOutputStream用于存储过滤后的字节
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (byte b : byteArray) {
            // 如果不是回车('\r')字节，就写入outputStream
            if (b != '\r') {
                outputStream.write(b);
            } else {
                Log.e(TAG, "包含回车:=====>");
            }
        }

        // 返回过滤后的字节数组
        return outputStream.toByteArray();
    }


    private HandHeldTerminalDeviceHelper() {
        //don't create it
    }

    public static HandHeldTerminalDeviceHelper getInstance() {
        return HandHeldTerminalDeviceHelper.Helper.INSTANCE;
    }


    @Override
    public void registerScanReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_DEFAULT);
        Utils.getApp().registerReceiver(scanBroadcastReceiver, intentFilter);
        Log.d(TAG, "registerScanReceiver===>");
    }

    @Override
    public void unregisterScanReceiver() {
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver);
        Log.d(TAG, "unregisterScanReceiver===>");
    }

    @Override
    public void registerScanListener(OnScanListener scanListener) {
        this.scanListener = scanListener;
        Log.d(TAG, "registerScanListener===>");
    }

    static final class Helper {

        private static final HandHeldTerminalDeviceHelper INSTANCE = new HandHeldTerminalDeviceHelper();

    }
}
