###some devices scanner



###use lib
#### Step1: Add the JitPack repository to your build file

```
 maven { url 'https://jitpack.io' }
 
```
#### Step2: Add the dependency

```
// pictrueSelector(被lib-phone-scanner，可以单独使用)
implementation 'com.github.Blue1-0.scanner:pictureselector:1.1.2'
//phone scan
implementation 'com.github.Blue1-0.scanner:lib-phone-scanner:1.1.2'
//pda scan
implementation 'com.github.Blue1-0.scanner:lib-scanner:1.1.2'

```
#### 垃圾设备备注：V9100,需要额外配置广播，并且不会持久化

```
 android.intent.ACTION_DECODE_DATA
 
 barcode_string
```
