package com.blue.lib_scanner

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ScannerType(
    val product: Array<String>,
    val brand: String?,
    val model: String? = null,
//    val code: Int
) : Parcelable {
    SUNMI(arrayOf("L2s_PRO"), "SUNMI"),
    UROVO(arrayOf("DT50_5G_EEA", "i6310", "i6200", "DT50", "i6200S", "i6300A"), "UROVO",),
    SEUIC(arrayOf("full_bird_k62v1_64_bsp"), "SEUIC"),
    ZEBRA(arrayOf("TC21", "TC70", "TC72", "TC70X"), "Zebra"),

    //nls-mt95
    NEWLAND(arrayOf("NLS-MT95L", "NLS-MT95", "NLS-MT90"), "Newland"),

    ALPS(arrayOf("full_k62v1_64_bsp"), "alps"),
    Hand_held_Terminal(arrayOf("Hand-held_Terminal"), "Hand-held_Terminal"),
    SMARTPHONE(arrayOf("V9100"), "smartphone"),

    /**
     * 不确定的设备类型
     */
    OTHER(arrayOf("SD55"), null)

}