package org.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

import org.jetbrains.annotations.NotNull;


/**
 * Author: yuzzha
 * Date: 2019-07-04 14:23
 * Description: IntentUtils
 * Remark:
 */
public class IntentUtils {

    /**
     * 显示跳转到 X5浏览器 页面
     *
     * @param mContext Context
     * @param title    网页标题
     * @param url      网页URL
     */
 /*   public static void goToBrowserPage(Context mContext, String title, String url) {
        Intent intent = new Intent(mContext, X5BrowserActivity.class);
        if (StringUtils.isNotEmpty(title))
            intent.putExtra(BaseContstant.KEY_BROWSER_TITLE, title);
        if (StringUtils.isNotEmpty(url))
            intent.putExtra(BaseContstant.KEY_BROWSER_URL, url);
        mContext.startActivity(intent);
    }*/
    public static void startActivity(Context packageContext, Class<?> mActivity) {
        packageContext.startActivity(new Intent(packageContext, mActivity));
    }

    public static void startActivity(Context packageContext, Intent intent) {
        packageContext.startActivity(intent);
    }

    public static void startActivity(Context packageContext, String className) {
        if (StringUtils.isNotEmpty(className)) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(packageContext, className);
            packageContext.startActivity(intent);
        }
    }

    public static void startActivityExtra(Context packageContext, String mExtraName, int mExtraValue, Class<?> mActivity) {
        if (StringUtils.isNotEmpty(mExtraName)) {
            Intent intent = new Intent(packageContext, mActivity);
            intent.putExtra(mExtraName, mExtraValue);
            packageContext.startActivity(intent);
        }
    }

    public static void startActivityForResult(Activity hostActivity, Class<?> mActivity, int requestCode) {
        startActivityForResult(hostActivity, new Intent(hostActivity, mActivity), requestCode);
    }


    public static void startActivityForResult(Activity hostActivity, Class<?> mActivity, Bundle extra, int requestCode) {
        startActivityForResult(hostActivity, new Intent(hostActivity, mActivity).putExtras(extra), requestCode);
    }

    public static void startActivityForResult(Activity hostActivity, Intent mIntent, int requestCode) {
        if (hostActivity != null)
            hostActivity.startActivityForResult(mIntent, requestCode);
        else new NullPointerException("startActivityForResult: Host Activity is not null");
    }


    /**
     * 隐士Intent 跳转 Activity
     *
     * @param mContext
     * @param action
     */
    public static void startActionActivity(@NotNull Context mContext, String action) {
        if (StringUtils.isNotEmpty(action)) {
            Intent intent = new Intent(action);
            if (mContext.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    LogUtils.e("TAG", "startActionActivity: ------>  ActivityNotFoundException action :" + action);
                }
            }
        }
    }


    public static void startPhoneActivity(@NotNull Context mContext) {
        mContext.startActivity(new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS));
    }

    //跳转系统的辅助功能界面
    public static void SysemHelpPage(@NotNull Context mContext) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        mContext.startActivity(intent);
    }

    //跳转系统的辅助功能界面
    public static void NetWorkHelpPage(@NotNull Context mContext) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        mContext.startActivity(intent);
        // 或者
        // Intent intent =  new Intent(Settings.ACTION_WIFI_SETTINGS);
        // startActivity(intent);
    }

    //系统开发者
    public static void startSysDevActivity(Context mContext) {
        mContext.startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
    }

    //手机系统信息
    public static void startPhoneInfoActivity(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
        mContext.startActivity(intent);
    }

    //NFC设置页面
    public static void startNfcActivity(@NotNull Context mContext) {
        mContext.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
    }

    //NFC Bean
    public static void startNfcSettingActivity(@NotNull Context mContext) {
        //Intent intent =  new Intent(Settings.ACTION_NFCSHARING_SETTINGS);
        mContext.startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
    }

    //系统设置页面
    public static void startSetActivity(@NotNull Context mContext) {
        mContext.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    //系统安全页面设置
    public static void startSetSeft(@NotNull Context mContext) {
        //Intent intent =  new Intent(Settings.ACTION_SECURITY_SETTINGS);
        mContext.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
    }

    //系统语言设置页面
    public static void startLocaleActivity(@NotNull Context mContext){
        //Intent intent =  new Intent(Settings.ACTION_LOCALE_SETTINGS);
        mContext.startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
    }

    //系统位置设置
    public static void startLocationActivity(@NotNull Context mContext){
        // Intent intent =  new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    //显示设置选择网络运营商  或弹选择框
    public static void startNetWorkOperator(@NotNull Context mContext){
        //Intent intent =  new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        mContext.startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
    }
}

