package com.blue.lib_scanner.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.blue.lib_scanner.inner.IScannerDeviceHelper;
import com.blue.lib_scanner.ScannerManager;


public class XDLDeviceHelper implements IScannerDeviceHelper {

    private static final String TAG = "XDLDeviceHelper";
    private static final String PRODUCT_NAME_MT95L = "NLS-MT95L";
    private static final String PRODUCT_NAME_MT90 = "NLS-MT90";
    private static final String ACTION_NAME_DEFAULT = ScanManager.ACTION_DECODE;
    private static final String ACTION_KEY_DEFAULT = ScanManager.BARCODE_STRING_TAG;
    private OnScanListener scanListener;
    private final BroadcastReceiver scanBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String finalScanData = intent.getStringExtra(ACTION_KEY_DEFAULT);
                if (scanListener != null) {
                    scanListener.onReceivedScanCode(finalScanData);
                }
                Log.d(TAG, "scanBroadcastReceiver===> finalScanData: " + finalScanData);
            }
        }
    };

    private XDLDeviceHelper() {
        //don't create it
    }

    public static XDLDeviceHelper getInstance() {
        return Helper.INSTANCE;
    }

    private static boolean isXDL() {
        String product = Build.PRODUCT.trim();
        ScannerManager.ScannerType scannerType = ScannerManager.getInstance().initScanner();
        Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null));
        if (scannerType == null) {
            return false;
        }
        boolean xdl = ScannerManager.ScannerType.NEWLAND == scannerType;
        Log.d(TAG, "product: " + product + "\txdl: " + xdl);
        return xdl;
    }

    @Override
    public void registerScanReceiver() {
        if (!isXDL()) return;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_DEFAULT);
        Utils.getApp().registerReceiver(scanBroadcastReceiver, intentFilter);
        Log.d(TAG, "registerScanReceiver===>");
    }

    @Override
    public void unregisterScanReceiver() {
        if (!isXDL()) return;
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver);
        Log.d(TAG, "unregisterScanReceiver===>");
    }

    @Override
    public void registerScanListener(OnScanListener scanListener) {
        this.scanListener = scanListener;
        Log.d(TAG, "registerScanListener===>");
    }

    static final class Helper {

        private static final XDLDeviceHelper INSTANCE = new XDLDeviceHelper();

    }
}
