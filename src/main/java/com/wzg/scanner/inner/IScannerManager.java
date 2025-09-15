package com.wzg.scanner.inner;

public interface IScannerManager {


    void registerScanner();

    void unregisterScanner();

    void registerScanListener(IScannerDeviceHelper.OnScanListener scanListener);

}
