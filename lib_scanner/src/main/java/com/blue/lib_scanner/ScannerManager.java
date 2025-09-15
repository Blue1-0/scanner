package com.blue.lib_scanner;


import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.blue.lib_scanner.device.AlpsDeviceHelper;
import com.blue.lib_scanner.device.DJDeviceHelper;
import com.blue.lib_scanner.device.SMDeviceHelper;
//import com.blue.lib_scanner.device.TC21PhoneScannerHelper;
import com.blue.lib_scanner.device.UrovoDeviceHelper;
import com.blue.lib_scanner.device.XDLDeviceHelper;
import com.blue.lib_scanner.inner.IScannerDeviceHelper;
import com.blue.lib_scanner.inner.IScannerManager;

/**
 * 扫码管理类
 */
public class ScannerManager implements IScannerManager {

    private IScannerDeviceHelper deviceHelper;


    public ScannerManager() {
        ScannerType scannerType = initScanner();
        if (null == scannerType) return;
        switch (scannerType) {
            case SUNMI:
                deviceHelper = SMDeviceHelper.getInstance();
                break;
            case UROVO:
                deviceHelper = UrovoDeviceHelper.getInstance();
                break;
            case SEUIC:
                deviceHelper = DJDeviceHelper.getInstance();
                break;
            case NEWLAND:
                deviceHelper = XDLDeviceHelper.getInstance();
                break;
//            case ZEBRA:
//                deviceHelper = new TC21PhoneScannerHelper();
//                break;
            case ALPS:
                deviceHelper = AlpsDeviceHelper.getInstance();
                break;
            case OTHER:
                deviceHelper = new PhoneScannerHelper();
                break;
            default:
                break;
        }
    }

    public static ScannerManager getInstance() {
        return Helper.INSTANCE;
    }

    @Override
    public void registerScanner() {
        if (deviceHelper != null) {
            deviceHelper.registerScanReceiver();
        }
    }

    @Override
    public void unregisterScanner() {
        if (deviceHelper != null) {
            deviceHelper.unregisterScanReceiver();
        }
    }

    @Override
    public void registerScanListener(IScannerDeviceHelper.OnScanListener scanListener) {
        if (deviceHelper != null) {
            deviceHelper.registerScanListener(scanListener);
        }
    }


    private static final class Helper {
        private static final ScannerManager INSTANCE = new ScannerManager();
    }

    /**
     * @return 如果返回空则不是已适配的设备类型或者不是PDA
     */
    public ScannerType initScanner() {
        final String brand = Build.BRAND;
        final String product = Build.PRODUCT;

        Log.d("scannerManager", "brand: " + brand + "\tproduct: " + product);

        for (ScannerType scannerType : ScannerType.values()) {

            if (ObjectUtils.isNotEmpty(product)) {
                for (String itemProduct : scannerType.getProduct()) {
                    if (product.contains(itemProduct)) {
                        return scannerType;
                    }
                }
            }

            if (ObjectUtils.isNotEmpty(brand) && ObjectUtils.isNotEmpty(scannerType.getBrand()) && brand.equalsIgnoreCase(scannerType.getBrand())) {
                return scannerType;
            }
        }

        return null;
    }


    public enum ScannerType {

        SUNMI(new String[]{"L2s_PRO"}, "SUNMI", 1),

        UROVO(new String[]{"DT50_5G_EEA", "i6310", "i6200", "DT50", "i6200S", "i6300A"}, "UROVO", 2),

        SEUIC(new String[]{"full_bird_k62v1_64_bsp"}, "SEUIC", 3),

        ZEBRA(new String[]{"TC21", "TC70", "TC72", "TC70X"}, "Zebra", 4),

        NEWLAND(new String[]{"NLS-MT95L", "NLS-MT90"}, "Newland", 5),

        ALPS(new String[]{"full_k62v1_64_bsp"}, "alps", 6),
        /**
         * 不确定的设备类型
         */
        OTHER(new String[]{"SD55"}, null, 7);

        private final String[] product;
        private final String brand;
        private final int code;

        ScannerType(String[] product, String brand, int code) {
            this.product = product;
            this.brand = brand;
            this.code = code;
        }

        public String[] getProduct() {
            return product;
        }

        public String getBrand() {
            return brand;
        }

        public int getCode() {
            return code;
        }
    }
}
