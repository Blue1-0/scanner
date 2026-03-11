package com.blue.lib_phone_scanner.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blue.lib_phone_scanner.R
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup

/**
 * author:Blue
 * date:2022/5/6
 * desc：相册，相机选择对话框
 */
class ImageSelectionDialog(
    mContext: Context,
    private val list: List<String>,
    private val cancelOnClick: () -> Unit,
    private val onItemClick: (Int) -> Unit,
) : Dialog(mContext) {
    private var rvList: RecyclerView? = null
    private var ivCancel: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dailog_common_picture_or_camera)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        val window = window
        window?.let {
            window.setWindowAnimations(R.style.DialogBottomAnim)
            window.setBackgroundDrawableResource(android.R.color.transparent)
            val lp = window.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = lp
        }
        initView()
    }

    private fun initView() {
        rvList = findViewById(R.id.rvList)
        ivCancel = findViewById(R.id.ivCancel)
        ivCancel?.setOnClickListener { cancelOnClick() }
        rvList?.let {
            it.linear()
                .setup {
                    addType<String>(R.layout.item_common_picture_or_camera)
                    onBind {
                        val model = getModel<String>()
                        val tvTitle = this.findView<TextView>(R.id.tvTitle)
                        tvTitle.text = model
                        val line = this.findView<View>(R.id.line)
                        if (modelPosition == list.size - 1) {
                            line.visibility = View.GONE
                        } else {
                            line.visibility = View.VISIBLE
                        }
                    }
                    onClick(R.id.rootView) { onItemClick(modelPosition) }
                }.models = list
        }
    }

}
