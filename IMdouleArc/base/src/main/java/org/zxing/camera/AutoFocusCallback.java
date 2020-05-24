package org.zxing.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

import org.utils.LogUtils;

/**
 * File: AutoFocusCallback.java
 * Author: yuzhuzhang
 * Create: 2020-02-04 11:35
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-04 : Create AutoFocusCallback.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class AutoFocusCallback implements Camera.AutoFocusCallback {

    private static final String TAG = AutoFocusCallback.class.getSimpleName();

    private static final long AUTOFOCUS_INTERVAL_MS = 1500L;

    private Handler autoFocusHandler;
    private int autoFocusMessage;

    void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
        this.autoFocusHandler = autoFocusHandler;
        this.autoFocusMessage = autoFocusMessage;
    }

    public void onAutoFocus(boolean success, Camera camera) {
        if (autoFocusHandler != null) {
            Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
            autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
            autoFocusHandler = null;
        } else {
            LogUtils.d("AutoFocusCallback","Got auto-focus callback, but no handler for it");
        }
    }

}