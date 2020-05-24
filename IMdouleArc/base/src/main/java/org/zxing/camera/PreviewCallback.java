package org.zxing.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * File: PreviewCallback.java
 * Author: yuzhuzhang
 * Create: 2020-02-07 17:56
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-07 : Create PreviewCallback.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class PreviewCallback implements Camera.PreviewCallback {

    private static final String TAG = PreviewCallback.class.getSimpleName();

    private final CameraConfig configManager;
    private final boolean useOneShotPreviewCallback;
    private Handler previewHandler;
    private int previewMessage;

    PreviewCallback(CameraConfig configManager, boolean useOneShotPreviewCallback) {
        this.configManager = configManager;
        this.useOneShotPreviewCallback = useOneShotPreviewCallback;
    }

    void setHandler(Handler previewHandler, int previewMessage) {
        this.previewHandler = previewHandler;
        this.previewMessage = previewMessage;
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Point cameraResolution = configManager.getCameraResolution();
        if (!useOneShotPreviewCallback) {
            camera.setPreviewCallback(null);
        }
        if (previewHandler != null) {
            Message message = previewHandler.obtainMessage(previewMessage, cameraResolution.x,
                    cameraResolution.y, data);
            message.sendToTarget();
            previewHandler = null;
        } else {
            Log.d(TAG, "Got preview callback, but no handler for it");
        }
    }

}

