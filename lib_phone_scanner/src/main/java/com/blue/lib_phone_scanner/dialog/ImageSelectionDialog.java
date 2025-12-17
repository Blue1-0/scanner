package com.blue.lib_phone_scanner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blue.lib_phone_scanner.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * author:wzg
 * date:2022/5/6
 * desc：相册，相机选择对话框
 */
public class ImageSelectionDialog extends Dialog {


    private RecyclerView rvList;
    private View ivCancel;
    private List<String> list;
    private Context mContext;
    private View.OnClickListener cancelOnClick;
    private OnItemClickListener onItemClickListener;


    public ImageSelectionDialog setCancelOnClick(View.OnClickListener cancelOnClick) {
        this.cancelOnClick = cancelOnClick;
        return this;
    }

    public ImageSelectionDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public ImageSelectionDialog(Context context, List<String> list) {
        super(context);
        mContext = context;
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dailog_common_picture_or_camera);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogBottomAnim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        rvList = findViewById(R.id.rvList);
        ivCancel = findViewById(R.id.ivCancel);
        ListItemAdapter adapter = new ListItemAdapter(list);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(adapter);
        ivCancel.setOnClickListener(v -> {
            if (cancelOnClick != null) cancelOnClick.onClick(v);
        });
        adapter.setOnItemClickListener(onItemClickListener);

    }

    private static class ListItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public ListItemAdapter(@Nullable List<String> data) {
            super(R.layout.item_common_picture_or_camera, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, String s) {
            holder.setText(R.id.tvTitle, s)
                    .setGone(R.id.line, getItemPosition(s) == getData().size()-1);
        }
    }
}
