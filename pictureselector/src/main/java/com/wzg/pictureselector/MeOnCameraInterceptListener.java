package com.wzg.pictureselector;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.luck.lib.camerax.SimpleCameraX;
import com.luck.picture.lib.interfaces.OnCameraInterceptListener;

public class MeOnCameraInterceptListener implements OnCameraInterceptListener {

    @Override
    public void openCamera(Fragment fragment, int cameraMode, int requestCode) {
        SimpleCameraX camera = SimpleCameraX.of();
        camera.isAutoRotation(true);
        camera.setCameraMode(cameraMode);
        camera.setVideoFrameRate(25);
        camera.setVideoBitRate(3 * 1024 * 1024);
        camera.isDisplayRecordChangeTime(true);
        camera.isManualFocusCameraPreview(true);
        camera.isZoomCameraPreview(true);
        camera.setOutputPathDir(SelectorUtils.getSandboxCameraOutputPath());
        camera.setPermissionDeniedListener(null);
        camera.setPermissionDescriptionListener(null);
        camera.setImageEngine((context, url, imageView) -> Glide.with(context).load(url).into(imageView));
        camera.start(fragment.requireActivity(), fragment, requestCode);
    }
}
