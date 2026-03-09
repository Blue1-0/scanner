package com.blue.lib_scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;

import com.blankj.utilcode.util.Utils;
import com.blue.lib_scanner.inner.IScannerDeviceHelper;

public class PhoneScannerHelper implements IScannerDeviceHelper {

    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action

    private ScanManager mScanManager;
    private OnScanListener onScanListener;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
            String message = new String(barcode, 0, barcodelen);
            OnReceiveData(message);
        }

    };

    public PhoneScannerHelper() {
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode(0);
    }

    public void register() {
        registerScanReceiver();
    }

    @Override
    public void registerScanReceiver() {
        if (mScanManager != null) {
            IntentFilter filter = new IntentFilter();
            int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
            String[] value_buf = mScanManager.getParameterString(idbuf);
            if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
                filter.addAction(value_buf[0]);
            } else {
                filter.addAction(SCAN_ACTION);
            }
            Utils.getApp().registerReceiver(mScanReceiver, filter);
        }
    }

    public void unregister() {
        unregisterScanReceiver();
    }

    @Override
    public void unregisterScanReceiver() {
        if (mScanManager != null) {
            mScanManager.stopDecode();
        }
        Utils.getApp().unregisterReceiver(mScanReceiver);
    }

    @Override
    public void registerScanListener(OnScanListener scanListener) {
        this.onScanListener = scanListener;
    }


    private void OnReceiveData(String message) {
        if (onScanListener != null) {
            onScanListener.onReceivedScanCode(message);
        }
    }

}
