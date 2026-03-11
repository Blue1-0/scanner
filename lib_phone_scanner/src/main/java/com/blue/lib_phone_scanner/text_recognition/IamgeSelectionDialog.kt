package com.blue.lib_phone_scanner.text_recognition

import android.annotation.SuppressLint
import android.view.Window
import com.blankj.utilcode.util.ActivityUtils
import com.blue.lib_phone_scanner.dialog.ImageSelectionDialog
import com.blue.pictureselector.MyPictureSelector

@SuppressLint("StaticFieldLeak")
var imageSelectionDialog: ImageSelectionDialog? = null

fun dismissPictureDialog() {
    if (imageSelectionDialog != null) {
        imageSelectionDialog!!.dismiss()
        imageSelectionDialog = null
    }
}

/**
 * 选择图片
 */
fun imagePicker(max: Int = 6, pictureResult: (List<String>?) -> Unit) {
    val topActivity = ActivityUtils.getTopActivity()
    if (imageSelectionDialog == null) {
        imageSelectionDialog =
            ImageSelectionDialog(
                mContext = topActivity,
                list = listOf("相机", "相册"),
                cancelOnClick = { dismissPictureDialog() },
                onItemClick = { position ->
                    if (position == 0) {
//            MyPictureSelector.openCameraCustomV2(topActivity,MyPictureSelector.REQUEST_CODE)
                        MyPictureSelector.openCameraV2(topActivity) { result, cancel ->
                            if (cancel) {
                                dismissPictureDialog()
                                return@openCameraV2
                            }
                            pictureResult(result)
                        }
                    } else {
                        MyPictureSelector.selectPicture(topActivity, max) { result, cancel ->
                            if (cancel) {
                                dismissPictureDialog()
                                return@selectPicture
                            }
                            pictureResult(result)
                        }
                    }
                    dismissPictureDialog()
                }
            ).apply {
                this.requestWindowFeature(Window.FEATURE_NO_TITLE)
                this.setCancelable(false)
                this.setCanceledOnTouchOutside(false)
            }

    }
    if (true != imageSelectionDialog?.isShowing) {
        imageSelectionDialog?.show()
    }

}

