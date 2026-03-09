package com.blue.lib_scanner.inner;

public interface IScannerManager {


    void registerScanner();

    void unregisterScanner();

    void registerScanListener(IScannerDeviceHelper.OnScanListener scanListener);

}
