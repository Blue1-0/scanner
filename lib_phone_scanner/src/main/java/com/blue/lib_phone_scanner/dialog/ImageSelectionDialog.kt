package com.blue.lib_phone_scanner.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blue.lib_phone_scanner.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * author:Blue
 * date:2022/5/6
 * desc：相册，相机选择对话框
 */
class ImageSelectionDialog(private val mContext: Context, private val list: List<String>) : Dialog(
    mContext
) {
    private var rvList: RecyclerView? = null
    private var ivCancel: View? = null
    private var cancelOnClick: View.OnClickListener? = null
    private var onItemClickListener: OnItemClickListener? = null
    fun setCancelOnClick(cancelOnClick: View.OnClickListener?): ImageSelectionDialog {
        this.cancelOnClick = cancelOnClick
        return this
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?): ImageSelectionDialog {
        this.onItemClickListener = onItemClickListener
        return this
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dailog_common_picture_or_camera)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        val window = window
        window!!.setWindowAnimations(R.style.DialogBottomAnim)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp
        initView()
    }

    private fun initView() {
        rvList = findViewById(R.id.rvList)
        ivCancel = findViewById(R.id.ivCancel)
        val adapter = ListItemAdapter(list)
        rvList?.layoutManager = LinearLayoutManager(mContext)
        rvList?.adapter = adapter
        ivCancel?.setOnClickListener { v -> cancelOnClick?.onClick(v) }
        adapter.setOnItemClickListener(onItemClickListener)
    }

    private class ListItemAdapter(data: List<String>) :
        BaseQuickAdapter<String?, BaseViewHolder>(
            R.layout.item_common_picture_or_camera,
            data.toMutableList()
        ) {
        override fun convert(holder: BaseViewHolder, item: String?) {
            holder.setText(R.id.tvTitle, item)
                .setGone(R.id.line, getItemPosition(item) == data.size - 1)
        }
    }
}
