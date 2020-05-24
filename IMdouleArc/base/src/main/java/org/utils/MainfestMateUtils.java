package org.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

/**
 * File: MainfestMateUtils.java
 * Author: yuzhuzhang
 * Create: 2020-02-07 20:05
 * Description:  注意：在获取mainfest 中 metadata Value中是不要 0 开头的
 * 类似这样的值如果使用bundle.getString()的话是不起作用的，因为Bundle中使用的是形如：
 * 的代码获取一个StringValue值的，但是在将metadata包装成bundle的时候，"000"被解析成整数0，
 * 形如000的字符串前面放个\0空字符，强迫android按照字符串解析000
 * 获取mainfest Applicitoin、Activity、Service 组件下
 * meta-data 中的Key - Value值
 * [int 、int []、str 、str[] …… Bundle 、byte 、byte[]、Byte 、char、float、float[]、long 、long[] ……]
 * -----------------------------------------------------------------
 * 2020-02-07 : Create MainfestMateUtils.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class MainfestMateUtils {

    public static String getAppStringValue(Context mContext, String key) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
        return appInfo.metaData.getString(key);
    }

    public static int getAppIntValue(Context mContext, String key) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
        return appInfo.metaData.getInt(key);
    }

    public static String getActivityStringValue(Activity mActivity, String key) throws PackageManager.NameNotFoundException {
        ActivityInfo info = mActivity.getPackageManager()
                .getActivityInfo(mActivity.getComponentName(),
                        PackageManager.GET_META_DATA);
        return info.metaData.getString(key);
    }

    public static int getActivityIntValue(Activity mActivity, String key) throws PackageManager.NameNotFoundException {
        ActivityInfo info = mActivity.getPackageManager()
                .getActivityInfo(mActivity.getComponentName(),
                        PackageManager.GET_META_DATA);
        return info.metaData.getInt(key);
    }

    public static String getServiceStringValue(Context mContext, Class serviceClazz, String key) throws PackageManager.NameNotFoundException {
        ComponentName cn = new ComponentName(mContext, serviceClazz);
        ServiceInfo info = mContext.getPackageManager()
                .getServiceInfo(cn, PackageManager.GET_META_DATA);
        return info.metaData.getString(key);
    }

    public static int getServiceIntValue(Context mContext, Class serviceClazz, String key) throws PackageManager.NameNotFoundException {
        ComponentName cn = new ComponentName(mContext, serviceClazz);
        ServiceInfo info = mContext.getPackageManager()
                .getServiceInfo(cn, PackageManager.GET_META_DATA);
        return info.metaData.getInt(key);
    }
}
