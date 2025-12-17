package com.blue.pictureselector.custom;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * @author Cs
 * @Date 2021/10/27 12:38
 * @Description
 **/
public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
    private float mRadius;

    public TextureVideoViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        /*Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);*/
        /*int left = 0;
        int top = (view.getHeight() - view.getWidth()) / 2;
        int right = view.getWidth();
        int bottom = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();*/
        int left = 0;
        int top = (view.getHeight() - view.getWidth()) / 2;
        int right = view.getWidth();
        int bottom = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();
        outline.setOval(left, top, right, bottom);
    }
}
