###some devices scanner



###use lib
#### Step1: Add the JitPack repository to your build file

```
 maven { url 'https://jitpack.io' }
 
```
#### Step2: Add the dependency

```
// pictrueSelector(被lib-phone-scanner，可以单独使用)
implementation 'com.github.Blue1-0.scanner:pictureselector:1.1.5'
//phone scan
implementation 'com.github.Blue1-0.scanner:lib-phone-scanner:1.1.5'
//pda scan
implementation 'com.github.Blue1-0.scanner:lib-scanner:1.1.5'

```
#### Support the following devices

```
@Parcelize
enum class ScannerType(val product: Array<String>, val brand: String?, val code: Int):Parcelable {
    SUNMI(arrayOf("L2s_PRO"), "SUNMI", 1),
    UROVO(arrayOf("DT50_5G_EEA", "i6310", "i6200", "DT50", "i6200S", "i6300A"), "UROVO", 2),
    SEUIC(arrayOf("full_bird_k62v1_64_bsp"), "SEUIC", 3),
    ZEBRA(arrayOf("TC21", "TC70", "TC72", "TC70X"), "Zebra", 4),
    NEWLAND(arrayOf("NLS-MT95L", "NLS-MT95", "NLS-MT90"), "Newland", 5),

    ALPS(arrayOf("full_k62v1_64_bsp"), "alps", 6),
    Hand_held_Terminal(arrayOf("Hand-held_Terminal"), "Hand-held_Terminal", 7),
    SMARTPHONE(arrayOf("V9100"), "smartphone", 8),

    /**
     * 不确定的设备类型
     */
    OTHER(arrayOf("SD55"), null, 8)

}
```
