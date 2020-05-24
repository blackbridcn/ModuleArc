package org.skin.attr;

import android.view.View;
import android.widget.TextView;

import org.skin.utils.SkinResourcesUtils;

/**
 * File: TextColorAttr.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 10:28 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create TextColorAttr.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class TextColorAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (isColor()) {
                tv.setTextColor(SkinResourcesUtils.getColorStateList(attrValueRefId));
            }
        }
    }

    @Override
    protected void applyNightMode(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (isColor()) {
                tv.setTextColor(SkinResourcesUtils.getNightColorStateList(attrValueRefId));
            }
        }
    }
}