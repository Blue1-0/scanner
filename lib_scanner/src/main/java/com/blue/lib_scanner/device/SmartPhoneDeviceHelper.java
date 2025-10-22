package com.blue.lib_scanner.device;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.blue.lib_scanner.ScannerManager;
import com.blue.lib_scanner.inner.IScannerDeviceHelper;



public class SmartPhoneDeviceHelper implements IScannerDeviceHelper {

    private static final String TAG = "SmartPhoneDeviceHelper";
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

    private SmartPhoneDeviceHelper() {
        //don't create it
    }

    public static SmartPhoneDeviceHelper getInstance() {
        return Helper.INSTANCE;
    }

    /**
     * 这里面目前仅包含CT58,后续可以将优博讯的其他设备都通过这里适配
     */
    private static boolean isSmartphone() {
        String product = Build.PRODUCT.trim();
        ScannerManager.ScannerType scannerType = ScannerManager.getInstance().initScanner();
        Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null));
        if (scannerType == null) {
            return false;
        }
        boolean smartphone = ScannerManager.ScannerType.SMARTPHONE == scannerType;
        Log.d(TAG, "product: " + product + "\tsmartphone: " + smartphone);
        return smartphone;
    }

    @Override
    public void registerScanReceiver() {
        if (!isSmartphone()) return;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_DEFAULT);
        Utils.getApp().registerReceiver(scanBroadcastReceiver, intentFilter);
        Log.d(TAG, "registerScanReceiver===>");
    }

    @Override
    public void unregisterScanReceiver() {
        if (!isSmartphone()) return;
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver);
        Log.d(TAG, "unregisterScanReceiver===>");
    }

    @Override
    public void registerScanListener(IScannerDeviceHelper.OnScanListener scanListener) {
        this.scanListener = scanListener;
        Log.d(TAG, "registerScanListener===>");
    }

    static final class Helper {

        private static final SmartPhoneDeviceHelper INSTANCE = new SmartPhoneDeviceHelper();

    }
}
