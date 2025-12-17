package com.blue.lib_phone_scanner.compose

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blue.lib_phone_scanner.R
import com.blue.lib_phone_scanner.ext.getOrNullToString


fun String.toColorExt(): Color {
    return Color(android.graphics.Color.parseColor(this))
}


@Composable
fun Int.toColorExt(): Color {
    return colorResource(id = this)
}

//inline val Int.dpExt: Dp
//    get() = Dp(
//        value = AutoSizeUtils.dp2px(
//            ActivityUtils.getTopActivity(),
//            this.toFloat()
//        ).toFloat()
//    )


@Composable
fun getStatusBarHeight(): Dp {
    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.getTop(density)
    return with(LocalDensity.current) { statusBarHeight.toDp() }
}


// 定义全局导航动画
fun AnimatedContentTransitionScope<*>.slideInTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

fun AnimatedContentTransitionScope<*>.slideOutTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

// 淡入淡出动画
fun AnimatedContentTransitionScope<*>.fadeInTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(300))
}

fun AnimatedContentTransitionScope<*>.fadeOutTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(300))
}

@Composable
fun TextLineOfColor(
    title: String,
    value: String? = "",
    fontBold: Boolean = false,
    wrapWidthFlag: Boolean = false,
    fontSize: TextUnit = 12.sp,
    colorRes1: Int =R.color.text_gray,
    colorRes2: Int = R.color.text_black,
    modifier: Modifier = if (wrapWidthFlag) Modifier.wrapContentWidth() else Modifier.fillMaxWidth()
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = LocalTextStyle.current.copy(
                color = colorResource(id = colorRes1),
                fontSize = fontSize,
                fontWeight = if (fontBold) FontWeight.Bold else FontWeight.Normal
            )
        )
//        Spacer(modifier = Modifier.width(1.dp))
        Text(
            text = value.getOrNullToString(),
            maxLines = 1,
            style = LocalTextStyle.current.copy(
                color = colorResource(id = colorRes2),
                fontSize = fontSize,
                fontWeight = if (fontBold) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
fun NotDataComponent(tips: String = "暂无数据") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tips,
            fontSize = 13.sp,
            color = colorResource(id = R.color.text_gray),
            textAlign = TextAlign.Center

        )
    }
}

