package com.blue.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.blue.demo.ui.theme.ScannerTheme

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
                                text = "scan code", style = LocalTextStyle.current.copy(
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