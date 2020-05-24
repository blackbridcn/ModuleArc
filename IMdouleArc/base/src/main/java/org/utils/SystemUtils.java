package org.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

/***
 * @author yzzhang
 * TODO  获取Android系统属性的Utils类
 * @2018-12-26
 * */
public class SystemUtils {
    /**
     * 获取设备唯一标示
     * 备注：Mac 地址，如果为空时，则返回系统Setting的android_id
     *
     * @return String
     */
    @SuppressLint("HardwareIds")
    public static String getHardWareId(Context mContext) {
        String macSerial = null;
        String wlanAddCmd = "cat /sys/class/net/wlan0/address ";
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(wlanAddCmd);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        if (macSerial == null) {
            macSerial = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return macSerial;
    }

    /**
     * 判断当前app在前台 还是后台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 判断服务是否运行
     *
     * @param context
     * @param serviceName
     * @return
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
