package com.wzg.pictureselector;

import android.Manifest;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.luck.picture.lib.interfaces.OnRecordAudioInterceptListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.permissions.PermissionResultCallback;
import com.luck.picture.lib.utils.ToastUtils;

public class MeOnRecordAudioInterceptListener implements OnRecordAudioInterceptListener {

    @Override
    public void onRecordAudio(Fragment fragment, int requestCode) {
//        String[] recordAudio = {Manifest.permission.RECORD_AUDIO};
//        if (PermissionChecker.isCheckSelfPermission(fragment.getContext(), recordAudio)) {
//            startRecordSoundAction(fragment, requestCode);
//        } else {
//            addPermissionDescription(false, (ViewGroup) fragment.requireView(), recordAudio);
//            PermissionChecker.getInstance().requestPermissions(fragment,
//                    new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionResultCallback() {
//                        @Override
//                        public void onGranted() {
//                            removePermissionDescription((ViewGroup) fragment.requireView());
//                            startRecordSoundAction(fragment, requestCode);
//                        }
//
//                        @Override
//                        public void onDenied() {
//                            removePermissionDescription((ViewGroup) fragment.requireView());
//                        }
//                    });
//        }
    }

    /**
     * 启动录音意图
     *
     * @param fragment
     * @param requestCode
     */
    private static void startRecordSoundAction(Fragment fragment, int requestCode) {
        Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (recordAudioIntent.resolveActivity(fragment.requireActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(recordAudioIntent, requestCode);
        } else {
            ToastUtils.showToast(fragment.getContext(), "The system is missing a recording component");
        }
    }
}
