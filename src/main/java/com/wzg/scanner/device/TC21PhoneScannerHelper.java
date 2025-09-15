//package com.wzg.scanner.device;
//
//import android.view.View;
//import android.widget.CompoundButton;
//
//import com.blankj.utilcode.util.ThreadUtils;
//import com.symbol.emdk.EMDKManager;
//import com.symbol.emdk.EMDKManager.EMDKListener;
//import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
//import com.symbol.emdk.EMDKResults;
//import com.symbol.emdk.barcode.BarcodeManager;
//import com.symbol.emdk.barcode.BarcodeManager.ConnectionState;
//import com.symbol.emdk.barcode.BarcodeManager.ScannerConnectionListener;
//import com.symbol.emdk.barcode.ScanDataCollection;
//import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
//import com.symbol.emdk.barcode.Scanner;
//import com.symbol.emdk.barcode.Scanner.TriggerType;
//import com.symbol.emdk.barcode.ScannerConfig;
//import com.symbol.emdk.barcode.ScannerException;
//import com.symbol.emdk.barcode.ScannerInfo;
//import com.symbol.emdk.barcode.ScannerResults;
//import com.symbol.emdk.barcode.StatusData;
//import com.symbol.emdk.barcode.StatusData.ScannerStates;
//import com.wzg.scanner.inner.IScannerDeviceHelper;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//
//
//
//public class TC21PhoneScannerHelper implements
//        EMDKListener,
//        Scanner.DataListener,
//        Scanner.StatusListener,
//        ScannerConnectionListener,
//        CompoundButton.OnCheckedChangeListener,
//        IScannerDeviceHelper {
//
//    private final static String TAG = "TC21";
//
//    private EMDKManager emdkManager = null;
//    private BarcodeManager barcodeManager = null;
//    private Scanner scanner = null;
//    private List<ScannerInfo> deviceList = null;
//
//    //当前使用的scanner类型 信息
//    private ScannerInfo mScannerInfo;
//    private String statusString = "";
//
//    //是否选择 点击 button view
//    private boolean bSoftTriggerSelected = false;
//    //编码格式设置是否更了
//    private boolean bDecoderSettingsChanged = true;
//    private boolean bExtScannerDisconnected = false;
//    //对象锁
//    private final Object lock = new Object();
//
//
//    private OnScanListener onScanListener;
//
//    //步骤2
//    @Override
//    public void onOpened(EMDKManager emdkManager) {
//        WLog.i(TAG, "EMDK open success!");
//        this.emdkManager = emdkManager;
//        // Acquire the barcode manager resources
//        initBarcodeManager();
//
//        //init
//        bSoftTriggerSelected = false;
//        bExtScannerDisconnected = false;
//        deInitScanner();
//        initScanner();
//    }
//
//    //步骤 最后关闭
//    @Override
//    public void onClosed() {
//        // Release all the resources
//        if (emdkManager != null) {
//            emdkManager.release();
//            emdkManager = null;
//        }
//        WLog.i(TAG, "EMDK closed unexpectedly! Please close and restart the application.");
//    }
//
//    //步骤3 接收到code信息
//    @Override
//    public void onData(ScanDataCollection scanDataCollection) {
//        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
//            ArrayList<ScanData> scanData = scanDataCollection.getScanData();
//            for (ScanDataCollection.ScanData data : scanData) {
//                WLog.i(TAG, data.getLabelType() + "=: " + data.getData());
//                onReceiveData(data.getData());
//            }
//        }
//    }
//
//    @Override
//    public void onStatus(StatusData statusData) {
//        ScannerStates state = statusData.getState();
//        switch (state) {
//            case IDLE:
//                statusString = statusData.getFriendlyName() + " is enabled and idle...";
//                WLog.i(TAG, statusString);
//                // set trigger type
//                if (bSoftTriggerSelected) {
//                    scanner.triggerType = TriggerType.SOFT_ONCE;
//                    bSoftTriggerSelected = false;
//                } else {
//                    scanner.triggerType = TriggerType.HARD;
//                }
//                // set decoders
//                if (bDecoderSettingsChanged) {
//                    setDecoders();
//                    bDecoderSettingsChanged = false;
//                }
//                // submit read
//                if (!scanner.isReadPending() && !bExtScannerDisconnected) {
//                    try {
//                        scanner.read();
//                    } catch (ScannerException e) {
//                        WLog.i(TAG, e.getMessage());
//                    }
//                }
//                break;
//            case WAITING:
//                statusString = "Scanner is waiting for trigger press...";
//                WLog.i(TAG, statusString);
//                break;
//            case SCANNING:
//                statusString = "Scanning...";
//                WLog.i(TAG, statusString);
//                break;
//            case DISABLED:
//                statusString = statusData.getFriendlyName() + " is disabled.";
//                WLog.i(TAG, statusString);
//                break;
//            case ERROR:
//                statusString = "An error has occurred.";
//                WLog.i(TAG, statusString);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onConnectionChange(ScannerInfo scannerInfo, ConnectionState connectionState) {
//        String status;
//        String scannerName = "";
//        String statusExtScanner = connectionState.toString();
//        String scannerNameExtScanner = scannerInfo.getFriendlyName();
////        if (deviceList.size() != 0) {
////            scannerName = deviceList.get(scannerIndex).getFriendlyName();
////        }
//        if (mScannerInfo != null) {
//            scannerName = mScannerInfo.getFriendlyName();
//        }
//
//        if (scannerName.equalsIgnoreCase(scannerNameExtScanner)) {
//            switch (connectionState) {
//                case CONNECTED:
//                    bSoftTriggerSelected = false;
//                    synchronized (lock) {
//                        initScanner();
//                        bExtScannerDisconnected = false;
//                    }
//                    break;
//                case DISCONNECTED:
//                    bExtScannerDisconnected = true;
//                    synchronized (lock) {
//                        deInitScanner();
//                    }
//                    break;
//            }
//            status = scannerNameExtScanner + ":" + statusExtScanner;
//            WLog.i(TAG, status);
//        } else {
//            bExtScannerDisconnected = false;
//            status = statusString + " " + scannerNameExtScanner + ":" + statusExtScanner;
//            WLog.i(TAG, status);
//        }
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        bDecoderSettingsChanged = true;
//        cancelRead();
//    }
//
//    private void setDecoders() {
//        if (scanner != null) {
//            try {
//                ScannerConfig config = scanner.getConfig();
//                config.decoderParams.qrCode.enabled = true;
//                config.decoderParams.upca.enabled = true;
//                // Set EAN8
//                config.decoderParams.ean8.enabled = true;
//                // Set EAN13
//                config.decoderParams.ean13.enabled = true;
//                // Set Code39
//                config.decoderParams.code39.enabled = true;
//                // Set Code39
//                config.decoderParams.code93.enabled = true;
//                //Set Code128
//                config.decoderParams.code128.enabled = true;
//                scanner.setConfig(config);
//            } catch (ScannerException e) {
//                WLog.i(TAG, e.getMessage());
//            }
//        }
//    }
//
//    //点击 Button View
//    public void softScan(View view) {
//        bSoftTriggerSelected = true;
//        cancelRead();
//    }
//
//    private void cancelRead() {
//        if (scanner != null) {
//            if (scanner.isReadPending()) {
//                try {
//                    scanner.cancelRead();
//                } catch (ScannerException e) {
//                    WLog.i(TAG, e.getMessage());
//                }
//            }
//        }
//    }
//
//    //获取支持scan方式列表
//    private ScannerInfo enumerateScannerDevices() {
//        if (barcodeManager != null) {
//            deviceList = barcodeManager.getSupportedDevicesInfo();
//            if ((deviceList != null) && (deviceList.size() != 0)) {
//                Iterator<ScannerInfo> it = deviceList.iterator();
//                while (it.hasNext()) {
//                    ScannerInfo scnInfo = it.next();
//                    //scnInfo.getFriendlyName() scan方式名称
//                    //TWO_DIMENSIONAL  2D Barcode Imager
//                    if ("2D Barcode Imager".equalsIgnoreCase(scnInfo.getFriendlyName())) {
//                        return scnInfo;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    private void initScanner() {
//        if (scanner == null) {
//            mScannerInfo = enumerateScannerDevices();
//            if (mScannerInfo != null && barcodeManager != null) {
//                scanner = barcodeManager.getDevice(mScannerInfo);
//                setDecoders();
//            } else {
//                WLog.e(TAG, "Failed to get the specified scanner device! Please close and restart the application.");
//                CommonTools.showCenterToast("扫描口调用失败，请重试~");
//                return;
//            }
//            if (scanner != null) {
//                scanner.addDataListener(this);
//                scanner.addStatusListener(this);
//                try {
//                    scanner.enable();
//                } catch (ScannerException e) {
//                    WLog.e(TAG, e.getMessage());
//                    deInitScanner();
//                }
//            } else {
//                WLog.e(TAG, "Failed to initialize the scanner device.");
//            }
//        }
//    }
//
//    private void deInitScanner() {
//        if (scanner != null) {
//            try {
//                scanner.disable();
//            } catch (Exception e) {
//                WLog.e(TAG, e.getMessage());
//            }
//
//            try {
//                scanner.removeDataListener(this);
//                scanner.removeStatusListener(this);
//            } catch (Exception e) {
//                WLog.e(TAG, e.getMessage());
//            }
//
//            try {
//                scanner.release();
//            } catch (Exception e) {
//                WLog.e(TAG, e.getMessage());
//            }
//            scanner = null;
//        }
//    }
//
//    private void initBarcodeManager() {
//        barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);
//        // Add connection listener
//        if (barcodeManager != null) {
//            barcodeManager.addConnectionListener(this);
//        }
//    }
//
//    private void deInitBarcodeManager() {
//        if (emdkManager != null) {
//            emdkManager.release(FEATURE_TYPE.BARCODE);
//        }
//    }
//
//    public void onResume() {
//        // The application is in foreground
//        if (emdkManager != null) {
//            // Acquire the barcode manager resources
//            initBarcodeManager();
//
//            // Initialize scanner
//            initScanner();
//        }
//    }
//
//    public void onPause() {
//        // The application is in background
//        // Release the barcode manager resources
//        deInitScanner();
//        deInitBarcodeManager();
//    }
//
//    public void onDestroy() {
//        // Release all the resources
//        if (emdkManager != null) {
//            emdkManager.release();
//            emdkManager = null;
//        }
//    }
//
//    public TC21PhoneScannerHelper() {
//        //步骤1
//        EMDKResults results = EMDKManager.getEMDKManager(WalmartApplication.getContext(), this);
//        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
//            WLog.e("TC21", "EMDKManager object request failed!");
//        }
//    }
//
//
//
//
//    private void onReceiveData(String message) {
//        if (onScanListener != null) {
//            ThreadUtils.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    onScanListener.onReceivedScanCode(message);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void registerScanReceiver() {
//        onResume();
//    }
//
//    @Override
//    public void unregisterScanReceiver() {
//        onPause();
//    }
//
//    @Override
//    public void registerScanListener(OnScanListener scanListener) {
//        this.onScanListener = scanListener;
//    }
//}
