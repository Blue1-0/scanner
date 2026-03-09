package com.blue.lib_scanner.inner

import com.blue.lib_scanner.inner.IScannerDeviceHelper.OnScanListener

interface IScannerManager {
    fun registerScanner()
    fun unregisterScanner()
    fun registerScanListener(scanListener: OnScanListener?)
}
