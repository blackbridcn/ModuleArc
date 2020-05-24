package org.skin;

import android.content.Context;

import org.skin.attr.AttrFactory;
import org.skin.attr.SkinAttr;
import org.skin.utils.SkinSPUtils;

/**
 * File: Config.java
 * Author: yuzhuzhang
 * Create: 2020/3/20 11:04 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/20 : Create Config.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class SkinConfig {

    public static final String NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String ATTR_SKIN_ENABLE = "enable";

    public static final String PREF_FONT_PATH = "skin_font_path";

    public static final String DEFAULT_SKIN = "skin_default";

    public static final String PREF_NIGHT_MODE = "night_mode";

    public static final String SKIN_DIR_NAME = "skin";
    public static final String FONT_DIR_NAME = "fonts";

    public static final String PREF_CUSTOM_SKIN_PATH = "skin_custom_path";

    //是否支持全局的皮肤主题的切换
    private static boolean isGlobalSkinApply = false;
    //是否修改ttf 字体
    private static boolean isCanChangeFont = false;
    //
    private static boolean isCanChangeStatusColor = false;

    private static boolean isDebug = false;


    public static boolean isCanChangeFont() {
        return isCanChangeFont;
    }

    public static boolean isGlobalSkinApply() {
        return isGlobalSkinApply;
    }

    public static boolean isInNightMode(Context context) {
        return SkinSPUtils.getBoolean(context, PREF_NIGHT_MODE, false);
    }

    public static void setNightMode(Context context, boolean isEnableNightMode) {
        SkinSPUtils.putBoolean(context, PREF_NIGHT_MODE, isEnableNightMode);
    }

    public static String getCustomSkinPath(Context context) {
        return SkinSPUtils.getString(context, PREF_CUSTOM_SKIN_PATH, DEFAULT_SKIN);
    }

    public static boolean isDefaultSkin(Context context) {
        return DEFAULT_SKIN.equals(getCustomSkinPath(context));
    }

    public static void saveSkinPath(Context context, String path) {
        SkinSPUtils.putString(context, PREF_CUSTOM_SKIN_PATH, path);
    }

    public static void setCanChangeStatusColor(boolean isCan) {
        isCanChangeStatusColor = isCan;
    }


    public static void setCanChangeFont(boolean isCan) {
        isCanChangeFont = isCan;
    }

    public static void setDebug(boolean enable) {
        isDebug = enable;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void addSupportAttr(String attrName, SkinAttr skinAttr) {
        AttrFactory.addSupportAttr(attrName, skinAttr);
    }


    /**
     * apply skin for global and you don't to set  skin:enable="true"  in layout
     */
    public static void enableGlobalSkinApply() {
        isGlobalSkinApply = true;
    }

    public static boolean isCanChangeStatusColor() {
        return isCanChangeStatusColor;
    }
}
