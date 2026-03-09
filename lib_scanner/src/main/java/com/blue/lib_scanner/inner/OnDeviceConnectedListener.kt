package com.blue.lib_scanner.inner

interface OnDeviceConnectedListener {
    fun onDeivceStateChanged(deviceName: String?, isConnected: Boolean)
}
