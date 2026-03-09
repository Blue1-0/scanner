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


public class UrovoDeviceHelper implements IScannerDeviceHelper {

    private static final String TAG = "UrovoDeviceHelper";
    private static final String PRODUCT_MODEL_CT58 = "CT58";
    private static final String PRODUCT_NAME_DT50_5G_EEA = "DT50_5G_EEA";
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

    private UrovoDeviceHelper() {
        //don't create it
    }

    public static UrovoDeviceHelper getInstance() {
        return Helper.INSTANCE;
    }

    /**
     * 这里面目前仅包含CT58,后续可以将优博讯的其他设备都通过这里适配
     */
    private static boolean isUrovo() {
        String product = Build.PRODUCT.trim();
        ScannerManager.ScannerType scannerType = ScannerManager.getInstance().initScanner();
        Log.d(TAG, "product: " + product + "\tscannerType.isNull: " + (scannerType == null));
        if (scannerType == null) {
            return false;
        }
        boolean urovo = ScannerManager.ScannerType.UROVO == scannerType;
        Log.d(TAG, "product: " + product + "\turovo: " + urovo);
        return urovo;
    }

    @Override
    public void registerScanReceiver() {
        if (!isUrovo()) return;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_DEFAULT);
        Utils.getApp().registerReceiver(scanBroadcastReceiver, intentFilter);
        Log.d(TAG, "registerScanReceiver===>");
    }

    @Override
    public void unregisterScanReceiver() {
        if (!isUrovo()) return;
        Utils.getApp().unregisterReceiver(scanBroadcastReceiver);
        Log.d(TAG, "unregisterScanReceiver===>");
    }

    @Override
    public void registerScanListener(OnScanListener scanListener) {
        this.scanListener = scanListener;
        Log.d(TAG, "registerScanListener===>");
    }

    static final class Helper {

        private static final UrovoDeviceHelper INSTANCE = new UrovoDeviceHelper();

    }
}
