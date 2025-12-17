package com.blue.lib_phone_scanner.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blankj.utilcode.util.ObjectUtils
import com.blue.lib_phone_scanner.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderComponent(
    title: String,
    rightText: String? = null,
    rightClick: () -> Unit = {},
    backClick: () -> Unit,
    fontSize: TextUnit = 16.sp,
    showLeftNavIcon: Boolean = true,
    bgColor: Int = R.color.text_blue,
    titleColor: Int = R.color.white,
    fontWeight: FontWeight = FontWeight.Normal,
    contentBarHeight: Dp = 47.dp,
    leftDrawable: Int = R.drawable.icon_back_white
) {
    val interactionSource = remember { MutableInteractionSource() }
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(contentBarHeight + getStatusBarHeight()),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = bgColor)
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    style = TextStyle(color = colorResource(id = titleColor)),
                    fontSize = fontSize,
                    textAlign = TextAlign.Center,
                    fontWeight = fontWeight
                )
            }
        },
        actions = {
            if (ObjectUtils.isNotEmpty(rightText)) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "$rightText",
                        style = TextStyle(color = colorResource(id = titleColor)),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable { rightClick.invoke() }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))

            }
        },
        navigationIcon = {
            if (showLeftNavIcon) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                ) {
                    Image(
                        painter = painterResource(id = leftDrawable),
                        contentDescription = null,
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                            .padding(start = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication =null /*ripple(
                                    bounded = false,
                                    radius = 30.dp
                                )*/,
                                onClick = backClick
                            ),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    )
}