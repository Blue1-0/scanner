package com.blue.lib_scanner.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.blue.lib_scanner.ScannerManager;
import com.blue.lib_scanner.inner.IScannerDeviceHelper;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * 目前所知，有两种不同的设备，但是的他们的信息是相同的（如：brand: alps	product: full_k62v1_64_bsp）
 */
public class AlpsDeviceHelper implements IScannerDeviceHelper {
    private static final String TAG = "AlpsDeviceHelper";
    private static final String ACTION_NAME_DEFAULT = "com.action.SCAN_RESULT";
    private static final String ACTION_NAME_DEFAULT_2 = "com.rfid.SCAN";//利旧
    private static final String ACTION_NAME_DEFAULT_3 = "android.scanservice.action.UPLOAD_BARCODE_DATA";
    private static final String ACTION_KEY_DEFAULT = "scanContext";
    private static final String ACTION_KEY_DEFAULT_2 = "data";
    private static final String ACTION_KEY_DEFAULT_3 = "barcode";
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
                    if (Objects.equals(intent.getAction(), ACTION_NAME_DEFAULT_2)) {
                        byteArrayExtra = intent.getByteArrayExtra(ACTION_KEY_DEFAULT_2);
                    }
                    if (Objects.equals(intent.getAction(), ACTION_NAME_DEFAULT_3)) {
                        scanData3 = intent.getStringExtra(ACTION_KEY_DEFAULT_3);
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


    private AlpsDeviceHelper() {
        //don't create it
    }

    public static AlpsDeviceHelper getInstance() {
        return AlpsDeviceHelper.Helper.INSTANCE;
    }

    /**
     * 这里面目前仅包含CT58,后续可以将优博讯的其他设备都通过这里适配
     */
    private static boolean isAlps() {
        String product = Build.PRODUCT.trim();
        ScannerManager.ScannerType scannerType = ScannerManager.getInstance().initScanner();
        Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null));
        if (scannerType == null) {
            return false;
        }
        boolean urovo = ScannerManager.ScannerType.ALPS == scannerType;
        Log.d(TAG, "product: " + product + "\talps: " + urovo);
        return urovo;
    }

    @Override
    public void registerScanReceiver() {
        if (!isAlps()) return;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_DEFAULT);
        intentFilter.addAction(ACTION_NAME_DEFAULT_2);
        intentFilter.addAction(ACTION_NAME_DEFAULT_3);
        Utils.getApp().registerReceiver(scanBroadcastReceiver, intentFilter);
        Log.d(TAG, "registerScanReceiver===>");
    }

    @Override
    public void unregisterScanReceiver() {
        if (!isAlps()) return;
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver);
        Log.d(TAG, "unregisterScanReceiver===>");
    }

    @Override
    public void registerScanListener(OnScanListener scanListener) {
        this.scanListener = scanListener;
        Log.d(TAG, "registerScanListener===>");
    }

    static final class Helper {

        private static final AlpsDeviceHelper INSTANCE = new AlpsDeviceHelper();

    }
}
