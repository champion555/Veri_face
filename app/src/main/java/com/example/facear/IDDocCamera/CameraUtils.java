package com.example.facear.IDDocCamera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by smartown on 2018/2/24 11:46.
 * <br>
 * Desc:
 * <br>
 * 工具类
 */
public class CameraUtils {
    /**
     * Check if this device has a camera
     */
    public static boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera openCamera() {
        Camera c = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    return c = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("cameras", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }

        }
        return null;
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//
//        } catch (Exception e) {
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
    }
    public static boolean hasFlash(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
