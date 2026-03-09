package com.blue.lib_scanner.inner

/**
 * 可服用扩展
 */
interface IScannerDeviceHelper {
    fun registerScanReceiver()
    fun unregisterScanReceiver()
    fun registerScanListener(scanListener: OnScanListener?)
    interface OnScanListener {
        fun onReceivedScanCode(message: String?)
    }
}
