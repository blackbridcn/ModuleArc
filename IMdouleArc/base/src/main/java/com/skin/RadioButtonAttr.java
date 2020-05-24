package com.skin;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RadioButton;

import org.skin.attr.SkinAttr;
import org.skin.utils.SkinResourcesUtils;

/**
 * File: RadioButtonAttr.java
 * Author: yuzhuzhang
 * Create: 2020/3/23 10:12 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/23 : Create RadioButtonAttr.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class RadioButtonAttr extends SkinAttr {
    @Override
    protected void applySkin(View view) {
        if (view instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) view;
            Drawable mDrawable = SkinResourcesUtils.getDrawable(attrValueRefId);
            radioButton.setButtonDrawable(mDrawable);
        }
    }
}
