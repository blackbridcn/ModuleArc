package org.skin.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

import org.skin.SkinConfig;

import java.io.File;

/**
 * File: TypefaceUtils.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 9:28 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create TypefaceUtils.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class TypefaceUtils {

    public static Typeface CURRENT_TYPEFACE;

    public static Typeface createTypeface(Context context, String fontName) {
        Typeface tf;
        if (!TextUtils.isEmpty(fontName)) {
            tf = Typeface.createFromAsset(context.getAssets(), getFontPath(fontName));
            SkinSPUtils.putString(context, SkinConfig.PREF_FONT_PATH, getFontPath(fontName));
        } else {
            tf = Typeface.DEFAULT;
            SkinSPUtils.putString(context, SkinConfig.PREF_FONT_PATH, "");
        }
        TypefaceUtils.CURRENT_TYPEFACE = tf;
        return tf;
    }

    public static Typeface getTypeface(Context context) {
        String fontPath = SkinSPUtils.getString(context, SkinConfig.PREF_FONT_PATH);
        Typeface tf;
        if (!TextUtils.isEmpty(fontPath)) {
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            tf = Typeface.DEFAULT;
            SkinSPUtils.putString(context, SkinConfig.PREF_FONT_PATH, "");
        }
        return tf;
    }

    private static String getFontPath(String fontName) {
        return SkinConfig.FONT_DIR_NAME + File.separator + fontName;
    }
}
