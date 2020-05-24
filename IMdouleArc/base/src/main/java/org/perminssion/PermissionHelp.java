package org.perminssion;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 2019/5/16 10:57
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class PermissionHelp {

    private PermissionHelp() {
    }

    private static final class PermissionHelpHolder {
        private static final PermissionHelp Instance = new PermissionHelp();
    }

    public static PermissionHelp getInstance() {
        return PermissionHelpHolder.Instance;
    }

    public void goToAppPermission(Context mContext) {
        //String model = android.os.Build.MODEL; // 手机型号
        //String release = android.os.Build.VERSION.RELEASE; // android系统版本号
        String brand = Build.BRAND.toLowerCase();//手机厂商
        switch (brand) {
            case "redmi":
                gotoMiuiPermission(mContext);//小米
                break;
            case "xiaomi":
                gotoMiuiPermission(mContext);//小米
                break;
            case "meizu":
                gotoMeizuPermission(mContext);
                break;
            case "huawei":
                gotoHuaweiPermission(mContext);
                break;
            case "honor":
                gotoHuaweiPermission(mContext);
                break;
            case "vivo":
                gotoVivoPermission(mContext);
                break;
            case "oppo":
                gotoOppoPermission(mContext);
                break;
            case "samsung":
                goToAppDetailSetting(mContext);
                break;
            case "lg":
                gotoLGPermission(mContext);
                break;
            case "sony":
                gotoSonyPermission(mContext);
                break;
            case "coolpad":
                gotoCoolpadPermission(mContext);
                break;
            case "letv":
                gotoLetvPermission(mContext);
                break;
            default:
                goToAppDetailSetting(mContext);
                break;
        }

    }

    /**
     * 跳转到miui的权限管理页面
     */
    private void gotoMiuiPermission(Context mContext) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", mContext.getPackageName());
            localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", mContext.getPackageName());
                mContext.startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情
                goToAppDetailSetting(mContext);
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private void gotoMeizuPermission(Context mContext) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", mContext.getPackageName() /*BuildConfig.APPLICATION_ID*/);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goToAppDetailSetting(mContext);
        }
    }

    /**
     * 华为的权限管理页面
     */
    private void gotoHuaweiPermission(Context mContext) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goToAppDetailSetting(mContext);
        }

    }

    private void gotoLGPermission(Context mContext) {
        try {
            Intent intent = new Intent(mContext.getPackageName());
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goToAppDetailSetting(mContext);
        }
    }

    private void gotoLetvPermission(Context mContext) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", mContext.getPackageName());
            ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            goToAppDetailSetting(mContext);
        }
    }

    private void goto360Permission(Context mContext) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", mContext.getPackageName());
        ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        intent.setComponent(comp);
        mContext.startActivity(intent);
    }

    private void gotoSonyPermission(Context mContext) {
        try {
            Intent intent = new Intent(mContext.getPackageName());
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goToAppDetailSetting(mContext);
        }
    }

    private void gotoOppoPermission(Context mContext) {
        doStartApplicationWithPackageName(mContext, "com.coloros.safecenter");
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     * 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    private void gotoCoolpadPermission(Context mContext) {
        doStartApplicationWithPackageName(mContext, "com.yulong.android.security:remote");
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    private void gotoVivoPermission(Context mContext) {
        doStartApplicationWithPackageName(mContext, "com.bairenkeji.icaller");
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }


    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     * 应用信息界面
     *
     * @return
     */
    private void goToAppDetailSetting(Context mContext) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        mContext.startActivity(localIntent);
    }

    private void doStartApplicationWithPackageName(Context mContext, String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            goToAppDetailSetting(mContext);
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            resolveIntent.setPackage(packageinfo.packageName);
        }
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mContext.getPackageManager().queryIntentActivities(resolveIntent, 0);
        //  Log.e("PermissionPageManager", "resolveinfoList" + resolveinfoList.size());
        /*for (int i = 0; i < resolveinfoList.size(); i++) {
            Log.e("PermissionPageManager", resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }*/
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                goToAppDetailSetting(mContext);
                e.printStackTrace();
            }
        }
    }


}
