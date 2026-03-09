package com.blue.lib_scanner.inner

interface OnDeviceConnectedListener {
    fun onDeviceStateChanged(deviceName: String?, isConnected: Boolean)
}
