package com.blue.lib_phone_scanner.text_recognition

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.blue.lib_phone_scanner.R
import com.blue.lib_phone_scanner.compose.HeaderComponent
import com.blue.lib_phone_scanner.compose.TextButtonCustom
import com.blue.lib_phone_scanner.compose.toColorExt

@Composable
fun TextRecognitionScreen(
    onBack: () -> Unit,
    decodeImageCallback: (String) -> Unit,
    viewModel: TextRecognitionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            HeaderComponent(
                title = "文字识别",
                backClick = { /*navController.popBackStack()*/onBack() })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(R.color.CE7E7E7.toColorExt()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
            ) {

                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(2.dp))
                        .clip(RoundedCornerShape(2.dp)),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.imgPath)
                            .scale(Scale.FILL)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.mipmap.default_material_bg),
                        error = painterResource(id = R.mipmap.default_material_bg)
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )


            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButtonCustom(
                    text = "选择图片",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                        .height(45.dp),
                    onClick = {
                        viewModel.selectImg(callback = decodeImageCallback)
                    }
                )
//                TextButtonCustom(
//                    text = "解析",
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(horizontal = 15.dp)
//                        .height(45.dp),
//                    onClick = {
//                        viewModel.detectInImage()
//                    }
//                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(text = "解析结果:", color = R.color.text_gray.toColorExt())
                Text(
                    text = state.analyzerResultText,
                    color = R.color.text_black.toColorExt(),
                    fontSize = 14.sp,
                    maxLines = 10,
                    minLines = 2
                )
            }
        }
    }
}