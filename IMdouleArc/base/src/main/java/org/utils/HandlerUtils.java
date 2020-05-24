package org.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.jetbrains.annotations.NotNull;


/**
 * Author: yuzzha
 * Date: 2019-07-04 14:23
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class HandlerUtils {

    static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private static Object snyLock = new Object();

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void postTask(@NotNull Runnable task) {
        mHandler.post(task);
    }

    public static void postDelayTask(@NotNull Runnable task, long delayMillis) {
        mHandler.postDelayed(task, delayMillis);
    }

    public static void removeDelayTask(@NotNull Runnable task){
        mHandler.removeCallbacks(task);
    }


    public static void sendMessage(Message msg, int arg1, OnHandleMessageLinsenter linsenter) {
        mHandler.sendMessage(msg);
    }

    interface OnHandleMessageLinsenter {
        void handleMessage(Message msg);
    }

}
