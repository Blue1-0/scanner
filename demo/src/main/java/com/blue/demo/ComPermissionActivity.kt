package com.blue.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 *
 * 权限类
 */
open class ComPermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!XXPermissions.isGranted(this, Permission.CAMERA)) {
            XXPermissions.with(this)
                .permission(Permission.CAMERA)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String>, allGranted: Boolean) {}
                    override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                        super.onDenied(permissions, doNotAskAgain)
                        ToastUtils.showLong("拒绝权限将可能导致某些功能不可用")
                    }
                })
        }

        val camera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        Log.e("permission", "camera has permission: $camera")


    }




}