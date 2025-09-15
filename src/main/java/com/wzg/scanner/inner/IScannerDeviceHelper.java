package com.wzg.scanner.inner;

/**
 * 可服用扩展
 */
public interface IScannerDeviceHelper {


    void registerScanReceiver();


    void unregisterScanReceiver();


    void registerScanListener(OnScanListener scanListener);


    interface OnScanListener {

        void onReceivedScanCode(String message);
    }


}
