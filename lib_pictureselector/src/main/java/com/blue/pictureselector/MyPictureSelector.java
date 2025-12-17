package com.blue.pictureselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnCameraInterceptListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureSelectorStyle;

import java.util.ArrayList;
import java.util.List;

public class MyPictureSelector {

    private final static String TAG_EXPLAIN_VIEW = "TAG_EXPLAIN_VIEW";
    private final static String TAG = "MyPictureSelector";


    /**
     * 获取返回的路径
     *
     * @param media 本地的媒体资源
     * @return path
     */
    public static ArrayList<String> getPathFromLocalMedia(Context activity,
                                                          List<LocalMedia> media) {
        if (media != null && media.size() > 0) {
            ArrayList<String> paths = new ArrayList<>();
            for (LocalMedia localMedia : media) {
                if (localMedia == null || TextUtils.isEmpty(localMedia.getPath())) {
                    return null;
                }
                String path;
                if (localMedia.isCut() && !localMedia.isCompressed()) {
                    // 裁剪过
                    path = localMedia.getCutPath();
                } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path = localMedia.getCompressPath();
                } else {
                    // 原图地址
                    path = localMedia.getPath();
                }

                /* 部分手机获取到路径是content://开头的 */
                if (path.contains("content:")) {
                    path = PathUtils.getRealPathFromUri(activity, Uri.parse(path));
                }

                paths.add(path);
            }
            return paths;
        }

        return null;
    }


    public static void selectPicture(Context context, int max, OnSelectorListener selectorListener) {
        PictureSelector.create(context)
                .openGallery(SelectMimeType.ofAll())
                .setSelectorUIStyle(new PictureSelectorStyle())
                .setImageEngine(GlideEngine.createGlideEngine())
//                .setCropEngine(new ImageFileCropEngine())
                .setCompressEngine(new ImageFileCompressEngine())
                .setSandboxFileEngine(new MeSandboxFileEngine())
                .isAutoVideoPlay(false)
                .isLoopAutoVideoPlay(false)
                .isUseSystemVideoPlayer(true)
                .isPageSyncAlbumCount(true)
//                .setCustomLoadingListener()
                .setQueryFilterListener(media -> false)
                .setSelectionMode(SelectModeConfig.MULTIPLE)
                .setQuerySortOrder(MediaStore.MediaColumns.DATE_MODIFIED + " ASC")
                .isDisplayTimeAxis(true)
                .isOnlyObtainSandboxDir(false)
                .isPageStrategy(false)
                .isOriginalControl(true)
                .isDisplayCamera(true)
                .isOpenClickSound(false)
//                .setSkipCropMimeType(null)
                .isFastSlidingSelect(false)
                .isWithSelectVideoImage(true)
                .isPreviewFullScreenMode(true)
                .isVideoPauseResumePlay(true)
                .isPreviewZoomEffect(true)
                .isPreviewImage(true)
                .isPreviewVideo(true)
                .isPreviewAudio(true)
                .setLanguage(LanguageConfig.UNKNOWN_LANGUAGE)
                .isMaxSelectEnabledMask(true)
                .isDirectReturnSingle(false)
                .setMaxSelectNum(max)
                .isGif(true)
                .forResult(new OnResultCallbackListener<>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        try {
                            Log.e(TAG, "selectPicture:图片选择结果：  " + GsonUtils.toJson(result));
                            ArrayList<String> absPath = getPathFromLocalMedia(context, result);
                            if (selectorListener != null) {
                                selectorListener.onResultCallback(absPath, false);
                            }

                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "selectPicture: 取消");
                        if (selectorListener != null) {
                            selectorListener.onResultCallback(new ArrayList<>(), true);
                        }
                    }
                });
    }

    public static void openCamera(Context context, OnSelectorListener selectorListener) {

        PictureSelector.create(context)
                .openCamera(SelectMimeType.ofImage())
                .setCameraInterceptListener(new OnCameraInterceptListener() {
                    @Override
                    public void openCamera(Fragment fragment, int cameraMode, int requestCode) {
                        // 拦截默认相机，跳转到自定义相机界面
                        if (cameraMode == SelectMimeType.ofImage()) {
                            boolean checkCamera = PermissionChecker.isCheckCamera(fragment.getActivity());
                            boolean isStorage = PermissionChecker.isCheckWriteExternalStorage(fragment.getActivity());
                            if (checkCamera && isStorage) {
                                Intent intent = new Intent(fragment.requireContext(), CustomCameraActivity.class);
                                // 传递必要参数
                                intent.putExtra("requestCode", requestCode);
                                fragment.startActivityForResult(intent, requestCode);
                            } else {
                                ToastUtils.showLong("没有权限");
                            }
                        }
                    }
                })
                .isCameraAroundState(true)
                .isCameraRotateImage(true)
//                .setCameraInterceptListener(new MeOnCameraInterceptListener())
                .setRecordAudioInterceptListener(new MeOnRecordAudioInterceptListener())
                .setSandboxFileEngine(new MeSandboxFileEngine())
                .setLanguage(LanguageConfig.UNKNOWN_LANGUAGE)
                .isOriginalControl(true)

                .forResult(new OnResultCallbackListener<>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        try {
                            Log.e(TAG, "openCamera:图片选择结果：  " + GsonUtils.toJson(result));
                            ArrayList<String> absPath = getPathFromLocalMedia(context, result);
                            if (selectorListener != null) {
                                selectorListener.onResultCallback(absPath, false);
                            }
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "openCamera: 取消");
                        if (selectorListener != null) {
                            selectorListener.onResultCallback(new ArrayList<>(), true);
                        }
                    }
                });
    }

    public static void openCameraV2(Context context, OnSelectorListener selectorListener) {
        Activity topActivity = ActivityUtils.getTopActivity();
        com.luck.picture.lib.basic.PictureSelector.create(topActivity)
                .openCamera(SelectMimeType.ofImage())
                .isCameraAroundState(false)
                .isCameraRotateImage(true)

//                .setCameraInterceptListener(new MeOnCameraInterceptListener())
                .setRecordAudioInterceptListener(new MeOnRecordAudioInterceptListener())
                .setSandboxFileEngine(new MeSandboxFileEngine())
                .setLanguage(LanguageConfig.UNKNOWN_LANGUAGE)
                .isOriginalControl(true)

                .forResultActivity(new OnResultCallbackListener<>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        try {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            Log.e(TAG, "openCamera:图片选择结果：  " + GsonUtils.toJson(result));
                            ArrayList<String> absPath = getPathFromLocalMedia(topActivity, result);
                            if (selectorListener != null) {
                                selectorListener.onResultCallback(absPath, false);
                            }
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "openCamera: 取消");
                        if (selectorListener != null) {
                            selectorListener.onResultCallback(new ArrayList<>(), true);
                        }
                    }
                });
    }

    public static void openCameraCustom(Activity context,@NonNull OnPermissionCallback onPermissionCallback) {
        // 拦截默认相机，跳转到自定义相机界面
        boolean checkCamera = PermissionChecker.isCheckCamera(context);
//        boolean isStorage = PermissionChecker.isCheckWriteExternalStorage(context);
        if (checkCamera /*&& isStorage*/) {
            onPermissionCallback.onPermission(true);
            Intent intent = new Intent(context, CustomCameraActivity.class);
            // 传递必要参数
            intent.putExtra("requestCode", 1995);
            context.startActivityForResult(intent, 1995);
        } else {
            ToastUtils.showLong("没有权限");
            onPermissionCallback.onPermission(false);
        }

    }

    public interface OnPermissionCallback{
        void onPermission(boolean hasFlag);
    }

    public interface OnSelectorListener {

        void onResultCallback(ArrayList<String> result, boolean cancel);

    }


}
