package com.blue.lib_phone_scanner

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * @author Blue
 * @desc 全局事件处理
 */
object ScanGlobalEvent {

    private val _globalEffect = MutableSharedFlow<GlobalIntent>(
        replay = 0,
        extraBufferCapacity = 10
    )
    val globalEvent = _globalEffect.asSharedFlow()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    fun sendEvent(event: GlobalIntent) {
        serviceScope.launch {
            _globalEffect.emit(event)
        }

    }

    sealed class GlobalIntent {


        /**
         * 扫描回调
         */
        data class ReceiveQrCode(
            val data: String,
            val timeNow: String,
            val requestCode: Int = -999
        ) : GlobalIntent()




    }
}

