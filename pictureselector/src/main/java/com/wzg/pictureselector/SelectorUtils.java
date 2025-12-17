package com.wzg.pictureselector;

import com.blankj.utilcode.util.Utils;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class SelectorUtils {


    public static String getSandboxPath() {
        File externalFilesDir = Utils.getApp().getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    public static UCrop.Options buildOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(true);
        options.setShowCropFrame(true);
        options.setShowCropGrid(true);
        options.setCircleDimmedLayer(true);
        options.withAspectRatio(3, 4);
//        options.setCropOutputPathDir(getSandboxPath());
        options.isCropDragSmoothToCenter(false);
//        options.setSkipCropMimeType(getNotSupportCrop());
        options.isForbidCropGifWebp(true);
        options.isForbidSkipMultipleCrop(true);
        options.setMaxScaleMultiplier(100);
//        if (selectorStyle != null && selectorStyle.getSelectMainStyle().getStatusBarColor() != 0) {
//            SelectMainStyle mainStyle = selectorStyle.getSelectMainStyle();
//            boolean isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack();
//            int statusBarColor = mainStyle.getStatusBarColor();
//            options.isDarkStatusBarBlack(isDarkStatusBarBlack);
//            if (StyleUtils.checkStyleValidity(statusBarColor)) {
//                options.setStatusBarColor(statusBarColor);
//                options.setToolbarColor(statusBarColor);
//            } else {
//                options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey));
//                options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey));
//            }
//            TitleBarStyle titleBarStyle = selectorStyle.getTitleBarStyle();
//            if (StyleUtils.checkStyleValidity(titleBarStyle.getTitleTextColor())) {
//                options.setToolbarWidgetColor(titleBarStyle.getTitleTextColor());
//            } else {
//                options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.ps_color_white));
//            }
//        } else {
//            options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey));
//            options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey));
//            options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.ps_color_white));
//        }
        return options;
    }


    /**
     * 创建相机自定义输出目录
     *
     * @return
     */
    public static String getSandboxCameraOutputPath() {
        File externalFilesDir = Utils.getApp().getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;

    }

    /**
     * 创建音频自定义输出目录
     *
     * @return
     */
    public static String getSandboxAudioOutputPath() {
        File externalFilesDir = Utils.getApp().getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sound");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;

    }

}
