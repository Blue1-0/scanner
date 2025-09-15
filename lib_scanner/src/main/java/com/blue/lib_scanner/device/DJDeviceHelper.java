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


public class DJDeviceHelper implements IScannerDeviceHelper {

    private static final String TAG = "DJDeviceHelper";
    private static final String ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE;
    private static final String ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG;
    private static final String ACTION_NAME_DJ = "com.android.server.scannerservice.broadcast";
    private static final String ACTION_KEY_DJ = "scannerdata";

    private OnScanListener scanListener;
    private final BroadcastReceiver scanBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String scannerDataDefault = intent.getStringExtra(ACTION_KEY_DEFAULT);
                String scannerDataDj = intent.getStringExtra(ACTION_KEY_DJ);
                String finalScanData = ObjectUtils.isNotEmpty(scannerDataDefault) ? scannerDataDefault : scannerDataDj;
                if (scanListener != null) {
                    scanListener.onReceivedScanCode(finalScanData);
                }
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: " + finalScanData);
            }
        }
    };

    private DJDeviceHelper() {
        //don't create it
    }

    public static DJDeviceHelper getInstance() {
        return Helper.INSTANCE;
    }


    private static boolean isDJ() {
        String product = Build.PRODUCT.trim();
        ScannerManager.ScannerType scannerType = ScannerManager.getInstance().initScanner();
        Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null));
        if (scannerType == null) {
            return false;
        }
        boolean dj = ScannerManager.ScannerType.SEUIC == scannerType;
        Log.d(TAG, "product: " + product + "\tdj: " + dj);
        return dj;
    }

    @Override
    public void registerScanReceiver() {
        if (!isDJ()) return;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_DEFAULT);
        intentFilter.addAction(ACTION_NAME_DJ);
        Utils.getApp().registerReceiver(scanBroadcastReceiver, intentFilter);
        Log.d(TAG, "registerScanReceiver===>");
    }

    @Override
    public void unregisterScanReceiver() {
        if (!isDJ()) return;
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver);
        Log.d(TAG, "unregisterScanReceiver===>");
    }

    @Override
    public void registerScanListener(OnScanListener scanListener) {
        this.scanListener = scanListener;
        Log.d(TAG, "registerScanListener===>");
    }

    private static final class Helper {

        private static final DJDeviceHelper INSTANCE = new DJDeviceHelper();

    }
}
