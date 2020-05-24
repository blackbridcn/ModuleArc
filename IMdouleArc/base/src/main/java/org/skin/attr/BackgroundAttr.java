package org.skin.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import org.skin.utils.SkinResourcesUtils;

/**
 * File: BackgroundAttr.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 10:30 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create BackgroundAttr.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class BackgroundAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (isColor()) {
            int color = SkinResourcesUtils.getColor(attrValueRefId);
            view.setBackgroundColor(color);
        } else if (isDrawable()) {
            Drawable bg = SkinResourcesUtils.getDrawable(attrValueRefId);
            view.setBackgroundDrawable(bg);
        }
    }

    @Override
    protected void applyNightMode(View view) {
        if (isColor()) {
            view.setBackgroundColor(SkinResourcesUtils.getNightColor(attrValueRefId));
        } else if (isDrawable()) {
            view.setBackgroundDrawable(SkinResourcesUtils.getNightDrawable(attrValueRefName));
        }
    }
}
