package org.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * File: ViewUtils.java
 * Author: yuzhuzhang
 * Create: 2019-12-30 13:56
 * Description: TODO
 * -----------------------------------------------------------------
 * 2019-12-30 : Create ViewUtils.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ViewUtils {

    public static void setViewHeight(ViewGroup viewGroup, double percent) {
        int screenHeight = getScreenHeight(viewGroup.getContext());
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewGroup.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (screenHeight * percent);
        viewGroup.setLayoutParams(layoutParams);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return cs != null && cs.length() > 0;
    }

    public static boolean isNotEmpty(CharSequence... sequences) {
        for (CharSequence mCharSequence : sequences)
            if (mCharSequence == null || mCharSequence.length() == 0) {
                return false;
            }
        return true;
    }



}
