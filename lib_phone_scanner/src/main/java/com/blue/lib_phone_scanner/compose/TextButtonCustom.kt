package com.blue.lib_phone_scanner.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blue.lib_phone_scanner.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author Blue
 * @desc 根据内容自适应的，实心的 textButton
 */
@Composable
fun TextButtonCustom(
    text: String,
    textColor: Int = R.color.white,
    fontSize: TextUnit = 12.sp,
    bgColor: Int = R.color.text_blue,
    roundSize: Dp = 6.dp,
    paddingVertical: Dp = 5.dp,
    paddingHorizontal: Dp = 8.dp,
    throttleDuration: Long = 1000,// 默认防抖时间为 500ms
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    val scale = remember { Animatable(1f) }
    val lastClickTime = remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()
    val animJob = remember { mutableStateOf<Job?>(null) }


    // 防抖点击处理函数
    val handleClick: () -> Unit = {
        val currentTime = System.currentTimeMillis()
        // 检查是否在防抖时间内
        if (currentTime - lastClickTime.value >= throttleDuration) {
            lastClickTime.value = currentTime
            onClick()
            animJob.value?.cancel()
            // 启动动画序列
            animJob.value = scope.launch {
                // 缩小到 0.9f
                scale.animateTo(
                    targetValue = 0.9f,
                    animationSpec = tween(durationMillis = 100)
                )
                // 恢复原大小
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 100)
                )
            }
        }


    }


    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
            .background(
                color = bgColor.toColorExt(),
                shape = RoundedCornerShape(roundSize)
            )
            .clip(RoundedCornerShape(roundSize))
            .clickable { handleClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = LocalTextStyle.current.copy(
                color = textColor.toColorExt(),
                fontSize = fontSize,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(
                horizontal = paddingHorizontal,
                vertical = paddingVertical
            ),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}