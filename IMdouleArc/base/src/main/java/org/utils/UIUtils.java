package org.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.application.BaseApplication;
import com.base.R;

public class UIUtils {


    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static int getDensity(Activity mActivity) {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.densityDpi;// 屏幕密度DPI（120 / 160 / 240）
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 获取上下文
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * pxz转换dip
     */

    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取字符数组
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取颜色id
     */
    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }


    /**
     * 根据id获取尺寸
     */
    public static int getDimens(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 在主线程中执行代码
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (isRunOnMainThread()) {
            // 执行代码
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static void post(Runnable runnable) {
        Handler handler = getHandler();
        handler.post(runnable);
    }

    /**
     * 移除一个执行的对象
     */
    public static void removeCallBacks(Runnable r) {
        getHandler().removeCallbacks(r);
    }

    /**
     * 延迟执行
     */
    public static void postDelay(Runnable runnable, long delay) {
        Handler handler = getHandler();
        handler.postDelayed(runnable, delay);
    }


    private static Handler getHandler() {
        return HandlerUtils.getMainHandler();//BaseApplication.getHandler();
    }

    public static boolean isRunOnMainThread() {
        return android.os.Process.myTid() == getMainThreadTid();
    }

    private static int getMainThreadTid() {
        return BaseApplication.getMainThreadId();
    }

    public static String getString(int id) {
        return getResources().getString(id);
    }

    public static Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    private static final int[] APPCOMPAT_CHECK_ATTRS = {R.attr.colorPrimary};

    public static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        final boolean failed = !a.hasValue(0);
        if (a != null) {
            a.recycle();
        }
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int dp2px(Context context, double dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        //((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void setViewHeight(ViewGroup viewGroup, double percent) {
        int screenHeight = getScreenHeight(getContext());
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewGroup.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (screenHeight * percent);
        viewGroup.setLayoutParams(layoutParams);
    }

    /**
     * 动态设置控件高度和屏幕宽度的比例
     *
     * @param activity
     * @param view     需要设置大小的view
     * @param ratio    比例：宽/高的值
     */
    public static void setViewHeightAboutWindow(Activity activity, View view,
                                                double ratio) {

        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 窗口的宽度
        int screenWidth = dm.widthPixels;
        // 窗口高度
        int screenHeight = dm.heightPixels;
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams(); // 取控件textView当前的布局参数
        linearParams.height = (int) (screenWidth / ratio);// 控件的高强制设成20
        linearParams.width = screenWidth;// 控件的宽强制设成30
        view.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件</pre>
    }
}
