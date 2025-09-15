package com.blue.lib_scanner.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.blue.lib_scanner.inner.IScannerDeviceHelper;
import com.blue.lib_scanner.ScannerManager;


public class SMDeviceHelper implements IScannerDeviceHelper {


    private static final String TAG = "SMDeviceHelper";
    public static final String PRODUCT_NAME = "L2s_PRO";
    private static final String ACTION_NAME_SUNMI = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";
    private static final String ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE;
    private static final String ACTION_KEY_SUNMI = "data";
    private static final String ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG;
    private static final String ACTION_BYTE_NAME = "source_byte";
    private OnScanListener scanListener;
    private final BroadcastReceiver scanBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String sunmiScanData = intent.getStringExtra(ACTION_KEY_SUNMI);
                String defaultScanData = intent.getStringExtra(ACTION_KEY_DEFAULT);
                String finalScanData = ObjectUtils.isNotEmpty(sunmiScanData) ? sunmiScanData : defaultScanData;
                if (scanListener != null) {
                    scanListener.onReceivedScanCode(finalScanData);
                }
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: " + finalScanData);
            }
        }
    };


    public static SMDeviceHelper getInstance() {
        return Helper.INSTANCE;
    }


    private static boolean isSUNMI() {
        String product = Build.PRODUCT.trim();
        ScannerManager.ScannerType scannerType = ScannerManager.getInstance().initScanner();
        Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null));
        if (scannerType == null) {
            return false;
        }
        boolean sunmi = ScannerManager.ScannerType.SUNMI == scannerType;
        Log.d(TAG, "product: " + product + "\tsunmi: " + sunmi);
        return sunmi;
    }


    @Override
    public void registerScanReceiver() {
        if (!isSUNMI()) return;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_SUNMI);
        intentFilter.addAction(ACTION_NAME_DEFAULT);
        Utils.getApp().registerReceiver(scanBroadcastReceiver, intentFilter);
        Log.d(TAG, "registerScanReceiver===>");


    }

    @Override
    public void unregisterScanReceiver() {
        if (!isSUNMI()) return;
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver);
        Log.d(TAG, "unregisterScanReceiver===>");
    }

    @Override
    public void registerScanListener(OnScanListener scanListener) {
        this.scanListener = scanListener;
        Log.d(TAG, "registerScanListener===>");
    }


    static final class Helper {

        private static final SMDeviceHelper INSTANCE = new SMDeviceHelper();

    }


}
