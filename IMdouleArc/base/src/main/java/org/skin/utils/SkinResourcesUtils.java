package org.skin.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import org.skin.core.SkinManager;

/**
 * File: SkinResourcesUtils.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 10:21 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create SkinResourcesUtils.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class SkinResourcesUtils {

    public static int getColor(int resId) {
        return SkinManager.getInstance().getColor(resId);
    }

    public static ColorStateList getNightColorStateList(int resId) {
        return SkinManager.getInstance().getNightColorStateList(resId);
    }

    public static int getNightColor(int resId) {
        return SkinManager.getInstance().getNightColor(resId);
    }

    public static Drawable getDrawable(int resId) {
        return SkinManager.getInstance().getDrawable(resId);
    }

    public static Drawable getNightDrawable(String resName) {
        return SkinManager.getInstance().getNightDrawable(resName);
    }

    /**
     * get drawable from specific directory
     *
     * @param resId res id
     * @param dir   res directory
     * @return drawable
     */
    public static Drawable getDrawable(int resId, String dir) {
        return SkinManager.getInstance().getDrawable(resId, dir);
    }

    public static ColorStateList getColorStateList(int resId) {
        return SkinManager.getInstance().getColorStateList(resId);
    }

    public static int getColorPrimaryDark() {
        if (!isNightMode()) {
            Resources resources = SkinManager.getInstance().getResources();
            if (resources != null) {
                int identify = resources.getIdentifier(
                        "colorPrimaryDark",
                        "color",
                        SkinManager.getInstance().getCurSkinPackageName());
                if (identify > 0) {
                    return resources.getColor(identify);
                }
            }
        } else {
            Resources resources = SkinManager.getInstance().getResources();
            if (resources != null) {
                int identify = resources.getIdentifier(
                        "colorPrimaryDark_night",
                        "color",
                        SkinManager.getInstance().getCurSkinPackageName());
                if (identify > 0) {
                    return SkinManager.getInstance().getColor(identify);
                }
            }
        }
        return -1;
    }

    public static boolean isNightMode() {
        return SkinManager.getInstance().isNightMode();
    }

}
