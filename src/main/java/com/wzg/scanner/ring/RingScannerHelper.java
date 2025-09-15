//package com.wzg.scanner.ring;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.hyco.sdk.config.HycoConfig;
//import com.wzg.scanner.inner.OnScanedListener;
//
//
//public class RingScannerHelper {
//
//    public final static String KEY_ACTION_STRING_DATA_BLE = HycoConfig.KEY_ACTION_STRING_DATA_BLE;
//    public final static String EXTRA_DATA = HycoConfig.KEY_VALUE_STRING;
//
//    private Context context;
//    public static boolean isRingConnected = false;
//    private OnScanedListener onScanedListener;
//
//    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            Log.i("Alex", "收到广播");
//            if (KEY_ACTION_STRING_DATA_BLE.equals(action)) {
//                String message = intent.getStringExtra(EXTRA_DATA);
//                message = message.replace("\r", "");
//                if (!TextUtils.isEmpty(message)) {
//                    OnReceiveData(message);
//                }
//            }
//        }
//
//    };
//
//    public RingScannerHelper(Context context) {
//        this.context = context;
//    }
//
//    public void register() {
//        registerScanReceiver();
//    }
//
//    private void registerScanReceiver() {
//        Log.i("Alex", "注册广播：" + KEY_ACTION_STRING_DATA_BLE);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(KEY_ACTION_STRING_DATA_BLE);
//        context.registerReceiver(mScanReceiver, filter);
//    }
//
//    public void unregister() {
//        Log.i("Alex", "销毁广播：" + KEY_ACTION_STRING_DATA_BLE);
//        unregisterScanReceiver();
//    }
//
//    private void unregisterScanReceiver() {
//        context.unregisterReceiver(mScanReceiver);
//    }
//
//    public void setOnScanedListener(OnScanedListener onScanedListener) {
//        this.onScanedListener = onScanedListener;
//    }
//
//    private void OnReceiveData(String message) {
//        if (onScanedListener != null) {
//            onScanedListener.onReceivedScanCode(message);
//        }
//    }
//
//}
