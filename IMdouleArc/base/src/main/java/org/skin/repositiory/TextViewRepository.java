package org.skin.repositiory;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

import org.skin.utils.TypefaceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File: TextViewRepository.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 9:27 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create TextViewRepository.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class TextViewRepository {


    private static Map<String, List<TextView>> mTextViewMap = new HashMap<>();

    public static void add(Activity activity, TextView textView) {

        String className = activity.getLocalClassName();
        if (mTextViewMap.containsKey(className)) {
            mTextViewMap.get(className).add(textView);
        } else {
            List<TextView> textViews = new ArrayList<>();
            textViews.add(textView);
            mTextViewMap.put(className, textViews);
        }
        textView.setTypeface(TypefaceUtils.CURRENT_TYPEFACE);
    }

    public static void remove(Activity activity) {
        mTextViewMap.remove(activity.getLocalClassName());
    }

    public  static void remove(Activity activity, TextView textView) {
        if (mTextViewMap.containsKey(activity.getLocalClassName())) {
            mTextViewMap.get(activity.getLocalClassName()).remove(textView);
        }
    }

    public  static void applyFont(Typeface tf) {
        for (String className : mTextViewMap.keySet()) {
            for (TextView textView : mTextViewMap.get(className)) {
                textView.setTypeface(tf);
            }
        }
    }
}
