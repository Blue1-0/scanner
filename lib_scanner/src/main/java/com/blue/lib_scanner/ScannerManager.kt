package com.blue.lib_scanner

import android.os.Build
import android.util.Log
import com.blankj.utilcode.util.ObjectUtils
import com.blue.lib_scanner.device.AlpsDeviceHelper
import com.blue.lib_scanner.device.DJDeviceHelper
import com.blue.lib_scanner.device.HandHeldTerminalDeviceHelper
import com.blue.lib_scanner.device.SMDeviceHelper
import com.blue.lib_scanner.device.SmartPhoneDeviceHelper
import com.blue.lib_scanner.device.UrovoDeviceHelper
import com.blue.lib_scanner.device.XDLDeviceHelper
import com.blue.lib_scanner.inner.IScannerDeviceHelper
import com.blue.lib_scanner.inner.IScannerDeviceHelper.OnScanListener
import com.blue.lib_scanner.inner.IScannerManager

/**
 * @author Blue
 * @desc 扫码管理类
 *
 */
object ScannerManager : IScannerManager {
    private var deviceHelper: IScannerDeviceHelper? = null

    init {
        //如果为空，即意味着未适配的设备类型
        val scannerType = initScanner()
        if (null != scannerType) {
            deviceHelper = when (scannerType) {
                ScannerType.SUNMI -> SMDeviceHelper
                ScannerType.UROVO -> UrovoDeviceHelper
                ScannerType.SEUIC -> DJDeviceHelper
                ScannerType.NEWLAND -> XDLDeviceHelper
                ScannerType.ALPS -> AlpsDeviceHelper
                ScannerType.Hand_held_Terminal -> HandHeldTerminalDeviceHelper
                ScannerType.SMARTPHONE -> SmartPhoneDeviceHelper
                ScannerType.OTHER -> PhoneScannerHelper
                else -> null
            }
        }
    }

    override fun registerScanner() {
        deviceHelper?.registerScanReceiver()
    }

    override fun unregisterScanner() {
        deviceHelper?.unregisterScanReceiver()
    }

    override fun registerScanListener(scanListener: OnScanListener?) {
        deviceHelper?.registerScanListener(scanListener)
    }


    /**
     * @return 如果返回空则不是已适配的设备类型或者不是PDA
     */
    fun initScanner(): ScannerType? {
        val brand = Build.BRAND
        val product = Build.PRODUCT
        Log.d("scannerManager", "brand: $brand\tproduct: $product")
        for (scannerType in ScannerType.entries) {
            if (ObjectUtils.isNotEmpty(product)) {
                for (itemProduct in scannerType.product) {
                    if (product.contains(itemProduct)) {
                        return scannerType
                    }
                }
            }
            if (ObjectUtils.isNotEmpty(brand)
                && ObjectUtils.isNotEmpty(scannerType.brand)
                && brand.equals(scannerType.brand, ignoreCase = true)
            ) {
                return scannerType
            }
        }
        return null
    }


}
