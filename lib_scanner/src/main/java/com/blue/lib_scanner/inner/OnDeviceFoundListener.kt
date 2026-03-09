package com.blue.lib_scanner.inner

import android.bluetooth.BluetoothDevice

interface OnDeviceFoundListener {
    fun onDeviceFound(device: BluetoothDevice?)
}
