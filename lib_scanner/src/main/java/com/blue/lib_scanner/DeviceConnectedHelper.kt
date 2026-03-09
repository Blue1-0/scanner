package com.blue.lib_scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.blue.lib_scanner.inner.OnDeviceConnectedListener;
import com.blue.lib_scanner.inner.OnDeviceFoundListener;


public class DeviceConnectedHelper {
    private Context context;
    private OnDeviceConnectedListener onDeviceConnectedListener;
    private OnDeviceFoundListener onDeviceFoundListener;
    private BroadcastReceiver scanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_ON:
                            showToast(context, "STATE_ON");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            showToast(context, "STATE_OFF");
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            showToast(context, "STATE_TURNING_ON");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            showToast(context, "STATE_TURNING_OFF");
                            break;
                        case BluetoothAdapter.STATE_CONNECTING:
                            showToast(context, "STATE_CONNECTING");
                            break;
                        case BluetoothAdapter.STATE_DISCONNECTING:
                            showToast(context, "STATE_DISCONNECTING");
                            break;
                        case BluetoothAdapter.STATE_CONNECTED:
                            showToast(context, "STATE_CONNECTED");
                            break;
                        case BluetoothAdapter.STATE_DISCONNECTED:
                            showToast(context, "STATE_DISCONNECTED");
                            break;
                        default:
                            showToast(context, "其他状态：" + blueState);
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    if (onDeviceConnectedListener != null) {
                        onDeviceConnectedListener.onDeivceStateChanged(device.getName(), true);
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    if (onDeviceConnectedListener != null) {
                        onDeviceConnectedListener.onDeivceStateChanged(device.getName(), false);
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    if (onDeviceFoundListener != null) {
                        onDeviceFoundListener.onDeivceFound(device);
                    }
                    break;
            }
        }

        private void showToast(Context context, String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    };

    public DeviceConnectedHelper(Context context) {
        this.context = context;
    }

    public void register() {
        registerScanReceiver();
    }

    private void registerScanReceiver() {
        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(scanReceiver, filter);
    }

    public void unregister() {
        unregisterScanReceiver();
    }

    private void unregisterScanReceiver() {
        context.unregisterReceiver(scanReceiver);
    }

    public void setOnDeviceConnectedListener(OnDeviceConnectedListener onDeviceConnectedListener) {
        this.onDeviceConnectedListener = onDeviceConnectedListener;
    }

    public void setOnDeviceFoundListener(OnDeviceFoundListener onDeviceFoundListener) {
        this.onDeviceFoundListener = onDeviceFoundListener;
    }
}
