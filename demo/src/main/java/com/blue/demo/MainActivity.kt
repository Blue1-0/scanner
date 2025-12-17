package com.blue.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.blue.demo.ui.theme.ScannerTheme
import com.blue.lib_phone_scanner.ScanGlobalEvent
import com.blue.lib_phone_scanner.ScanQrOfZXingActivity
import kotlinx.coroutines.launch
import java.util.Scanner

class MainActivity : BaseComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(onClick = {
                            startCameraScan() // phone(camera) scan  or pda(Broadcast)
                        }) {
                            Text(
                                text = "scan code of hms", style = LocalTextStyle.current.copy(
                                    fontSize = 20.sp,
                                    color = Color.Black
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            startActivityForResult(
                                Intent(
                                    this@MainActivity,
                                    ScanQrOfZXingActivity::class.java
                                ), 1998
                            )
                        }) {
                            Text(
                                text = "scan code of activity", style = LocalTextStyle.current.copy(
                                    fontSize = 20.sp,
                                    color = Color.Black
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }
            }
        }

        registerScanEvent()
    }

    private fun registerScanEvent() {
        lifecycleScope.launch {
            ScanGlobalEvent.globalEvent.collect { intent ->
                if (intent is ScanGlobalEvent.GlobalIntent.ReceiveQrCode) {
                    ToastUtils.showLong("${intent.requestCode} ${intent.data}")
                }
            }
        }
    }


    override fun receiveDeviceCommonScanResult(result: String, usb: Boolean) {
        super.receiveDeviceCommonScanResult(result, usb)
        ToastUtils.showLong(result)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ScannerTheme {
//        Greeting("Android")
//    }
//}