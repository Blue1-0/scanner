package com.blue.lib_phone_scanner.ext

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import java.util.WeakHashMap

/**
 * @author zhenGui Wen
 */
private val lastClickTimes = WeakHashMap<View, Long>()
fun View.setScaleClickListener(onClick: (View) -> Unit) {
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        val lastClickTime = lastClickTimes[this] ?: 0
        val throttlePeriod = 500 // 节流时间设为500毫秒

        if (currentTime - lastClickTime < throttlePeriod) {
            Log.w("scale","")
            return@setOnClickListener
        }
        lastClickTimes[this] = currentTime
        try {
            // 缩小动画
            val scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 0.9f)
            val scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 0.9f)
            scaleDownX.duration = 100
            scaleDownY.duration = 100

            // 回弹动画
            val scaleUpX = ObjectAnimator.ofFloat(this, "scaleX", 1f)
            val scaleUpY = ObjectAnimator.ofFloat(this, "scaleY", 1f)
            scaleUpX.duration = 100
            scaleUpY.duration = 100

            // 动画集合，按顺序执行
            val scaleDown = AnimatorSet()
            scaleDown.playTogether(scaleDownX, scaleDownY)
            val scaleUp = AnimatorSet()
            scaleUp.playTogether(scaleUpX, scaleUpY)

            // 先缩小再回弹
            scaleDown.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    scaleUp.start()
                }
            })

            // 执行点击事件
            scaleDown.start()
        } catch (_: Throwable) {

        }

        onClick(this)  // 回调点击事件
    }
}


fun View.showKeyBoardExt() {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.requestFocus()
    KeyboardUtils.showSoftInput(this)
}

fun View.hideKeyBoardExt() {
    KeyboardUtils.hideSoftInput(this)
}


/**
 * 扩展函数：连续点击触发事件
 *
 * @param clickCount 需要连续点击的次数
 * @param timeWindow 点击有效时间窗口，单位：毫秒
 * @param onMultiClick 点击满足条件时触发的事件
 */
fun View.setOnMultiClickListener(
    clickCount: Int = 5,
    timeWindow: Long = 1000,
    onMultiClick: () -> Unit
) {
    var currentClickCount = 0
    val handler = Handler(Looper.getMainLooper())
    var resetTask: Runnable? = null

    this.setOnClickListener {
        currentClickCount++

        // 如果是第一次点击，启动计时器
        if (currentClickCount == 1) {
            resetTask = Runnable { currentClickCount = 0 }
            handler.postDelayed(resetTask!!, timeWindow)
        }

        // 达到点击次数时触发事件
        if (currentClickCount == clickCount) {
            onMultiClick()
            currentClickCount = 0
            handler.removeCallbacks(resetTask!!)
        }
    }
}


