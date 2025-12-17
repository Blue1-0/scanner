
package com.blue.pictureselector.custom;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.TextureView;
import android.widget.FrameLayout;


/**
 * 基于 系统TextureView实现的预览View。
 *
 * @Time: 2019/1/28
 * @Author: v_chaixiaogang
 */
public class AutoTexturePreviewView extends FrameLayout {

    public TextureView textureView;

    private int videoWidth = 0;
    private int videoHeight = 0;


//    private int previewWidth = 0;
//    private int previewHeight = 0;
//    private static int scale = 2;

    private int rotationDegrees = 0; // 当前旋转角度


    public AutoTexturePreviewView(Context context) {
        super(context);
        init();
    }

    public AutoTexturePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoTexturePreviewView(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final Handler handler = new Handler(Looper.getMainLooper());

    private void init() {
        textureView = new TextureView(getContext());
//        textureView.setOutlineProvider(new TextureVideoViewOutlineProvider(120));
//        textureView.setClipToOutline(true);
        addView(textureView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

            // 判断是否需要交换宽高比
            boolean needSwap = (rotationDegrees / 90) % 2 == 1; // 90/270度时需要交换

            int adjustedWidth = needSwap ? videoHeight : videoWidth;
            int adjustedHeight = needSwap ? videoWidth : videoHeight;

            if (adjustedWidth == 0 || adjustedHeight == 0 || getWidth() == 0 || getHeight() == 0) {
                super.onLayout(changed, left, top, right, bottom);
                return;
            }

            float viewAspect = (float) getWidth() / getHeight();
            float videoAspect = (float) adjustedWidth / adjustedHeight;

            if (videoAspect > viewAspect) {
                // 视频更宽，按宽度适配
                int height = (int) (getWidth() / videoAspect);
                textureView.layout(0, (getHeight() - height) / 2,
                        getWidth(), (getHeight() + height) / 2);
            } else {
                // 视频更高，按高度适配
                int width = (int) (getHeight() * videoAspect);
                textureView.layout((getWidth() - width) / 2, 0,
                        (getWidth() + width) / 2, getHeight());
            }
        ///
//        previewWidth = getWidth();
//        previewHeight = getHeight();
//
//        if (videoWidth == 0 || videoHeight == 0 || previewWidth == 0 || previewHeight == 0) {
//            return;
//        }
//
//        if (previewWidth * videoHeight > previewHeight * videoWidth) {
//            int scaledChildHeight = videoHeight * previewWidth / videoWidth;
//            textureView.layout(0, (previewHeight - scaledChildHeight) / scale,
//                    previewWidth, (previewHeight + scaledChildHeight) / scale);
//        } else {
//            int scaledChildWidth = videoWidth * previewHeight / videoHeight;
//            textureView.layout((previewWidth - scaledChildWidth) / scale, 0,
//                    (previewWidth + scaledChildWidth) / scale, previewHeight);
//        }
    }

    public TextureView getTextureView() {
        return textureView;
    }

//    public int getPreviewWidth() {
//        return previewWidth;
//    }
//
//    public int getPreviewHeight() {
//        return previewHeight;
//    }

    public void setPreviewSize(int width, int height) {
        if (this.videoWidth == width && this.videoHeight == height) {
            return;
        }
        this.videoWidth = width;
        this.videoHeight = height;
        handler.post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    public void setRotation(int degrees) {
        // 规范化角度到[0, 360)范围
        degrees = degrees % 360;
        if (degrees < 0) degrees += 360;

        if (this.rotationDegrees != degrees) {
            this.rotationDegrees = degrees;
            updateRotation();
        }
    }
    public float getRotation() {
        return rotationDegrees;
    }

    private void updateRotation() {
        // 应用矩阵变换
        applyRotation();

        // 请求布局更新
        handler.post(() -> {
            requestLayout();
            invalidate();
        });
    }
    private void applyRotation() {
        Matrix matrix = new Matrix();
        RectF drawRect = new RectF(0, 0, videoWidth, videoHeight);
        RectF viewRect = new RectF(0, 0, getWidth(), getHeight());

        // 计算基础缩放
        matrix.setRectToRect(drawRect, viewRect, Matrix.ScaleToFit.CENTER);

        // 应用旋转（以视图中心为轴心）
        matrix.postRotate(rotationDegrees, viewRect.centerX(), viewRect.centerY());

        textureView.setTransform(matrix);
    }
}