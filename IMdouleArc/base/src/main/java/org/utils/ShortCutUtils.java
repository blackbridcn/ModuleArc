package org.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 快捷键三要素
 * 1.创建代码、manifest 清单文件目标activity注册
 * 2.权限
 * 3.版本适配
 */
public class ShortCutUtils {

    /**
     * 创建桌面快捷方式
     * 备注：这里需要权限
     *
     * @param mContext
     * @param mShorCutTitle
     * @param mShorCutIcon
     * @param cls
     * @param action
     * @param extraKey
     * @param extraValue
     */
    public static void creatShorCut(Context mContext, String mShorCutTitle, @DrawableRes int mShorCutIcon,
                                    @NonNull Class<? extends Activity> cls,
                                    String action,
                                    String extraKey,
                                    int extraValue) {
        //8.0 7.1  7.1以下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            creatShortCutVersionO(mContext, cls, mShorCutTitle, mShorCutIcon, action, extraKey, extraValue);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            creatShortCutVer(mContext, cls, mShorCutTitle, mShorCutIcon, action, extraKey, extraValue);
        } else {
            creatOldShorCut(mContext, cls, mShorCutTitle, mShorCutIcon, action, extraKey, extraValue);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void creatShortCutVersionO(@NotNull Context mContext, @NonNull Class<? extends Activity> cls,
                                              @NotNull String mShorCutTitle, @DrawableRes int mShorCutIcon,
                                              String action,
                                              String extraKey,
                                              int extraValue) {
        ShortcutManager shortcutManager = (ShortcutManager) mContext.getSystemService(Context.SHORTCUT_SERVICE);
        boolean requestPinShortcutSupported = shortcutManager.isRequestPinShortcutSupported();
        Log.i("TAG", "启动器是否支持固定快捷方式: " + requestPinShortcutSupported);

        if (requestPinShortcutSupported) {
            Intent mIntent = new Intent(mContext, cls);
            mIntent.putExtra(extraKey, extraValue);
            mIntent.setAction(Intent.ACTION_VIEW);
            ShortcutInfo info = new ShortcutInfo.Builder(mContext, cls.getName())
                    .setIcon(Icon.createWithResource(mContext, mShorCutIcon))
                    .setShortLabel(mShorCutTitle)
                    .setLongLabel(mShorCutTitle)
                    .setIntent(mIntent)
                    .build();

            //当添加快捷方式的确认弹框弹出来时，将被回调CallBackReceiver里面的onReceive方法
            PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, ShortcutReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());
        }
    }

    class ShortcutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG", "onReceive: ----------------->  ");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private static void creatShortCutVer(@NotNull Context mContext, @NonNull Class<? extends Activity> cls,
                                         @NotNull String mShorCutTitle, @DrawableRes int mShorCutIcon,
                                         String action,
                                         String extraKey,
                                         int extraValue) {
        //获取系统服务得到ShortcutManager对象
        ShortcutManager systemService = mContext.getSystemService(ShortcutManager.class);
        //设置Intent跳转逻辑
        Intent intent = new Intent(mContext, cls);
        intent.setAction(Intent.ACTION_VIEW);
        if (StringUtils.isNotEmpty(extraKey))
            intent.putExtra(extraKey, extraValue);
        //设置ID
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(mContext, cls.getName())
                //设置短标题
                .setShortLabel(mShorCutTitle)
                //设置长标题
                .setLongLabel(mShorCutTitle)
                //设置icon
                .setIcon(Icon.createWithResource(mContext, mShorCutIcon))
                //设置Intent
                .setIntent(intent)
                .build();
        //这样就可以通过长按图标显示出快捷方式了
        systemService.setDynamicShortcuts(Arrays.asList(shortcutInfo));
    }

    /**
     * 创建桌面快捷方式
     *
     * @param mContext      Context
     * @param mShorCutTitle 快捷键title
     * @param mShorCutIcon  快捷键图标
     * @param cls           点击快捷键跳转页面
     * @param action        快捷键Action
     * @param extraKey      Intent extraValue Key
     * @param extraValue    Intent extraValue Value
     */
    private static void creatOldShorCut(@NotNull Context mContext, @NonNull Class<? extends Activity> cls,
                                        @NotNull String mShorCutTitle, @DrawableRes int mShorCutIcon,
                                        String action,
                                        String extraKey,
                                        int extraValue) {
        // 加载快捷键桌面图标
        Parcelable icon = Intent.ShortcutIconResource.fromContext(mContext, mShorCutIcon);
        //点击快捷方式后操作Intent,快捷方式建立后，再次启动该程序
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(mContext, cls);
        //下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
        intent.setAction(action);
        intent.putExtra(extraKey, extraValue);//添加快捷开锁标记
        // 创建添加快捷方式的Intent
        Intent addShortCut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //设置不进行重复添加快捷方式EXTRA_SHORTCUT_NAME
        addShortCut.putExtra("duplicate", false);
        //设置快捷方式的标题
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, mShorCutTitle);
        //设置快捷方式的图标
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置快捷方式对应的Intent
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        addShortCut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //发送广播添加快捷方式
        mContext.sendBroadcast(addShortCut);
    }


    /**
     * 判断当前应用在桌面是否有桌面快捷方式
     *
     * @param context
     */
    public static boolean hasShortcut(Context context) {
        boolean result = false;
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {

        }

        final String uriStr;
        if (Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else if (Build.VERSION.SDK_INT < 19) {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher3.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = context.getContentResolver().query(CONTENT_URI, null,
                "title=?", new String[]{title}, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        }
        return result;
    }


    /**
     * 删除当前应用的桌面快捷方式
     * 在android7.0 上测试无效
     *
     * @param context
     */
    public static void delShortcut(Context context) {
        Intent shortcut = new Intent(
                "com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        context.sendBroadcast(shortcut);
    }

}
