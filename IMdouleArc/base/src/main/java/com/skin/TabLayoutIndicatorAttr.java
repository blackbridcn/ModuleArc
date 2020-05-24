package com.skin;

import android.view.View;

import com.google.android.material.tabs.TabLayout;

import org.skin.attr.SkinAttr;
import org.skin.utils.SkinResourcesUtils;

/**
 * File: TabLayoutIndicatorAttr.java
 * Author: yuzhuzhang
 * Create: 2020/3/23 10:07 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/23 : Create TabLayoutIndicatorAttr.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class TabLayoutIndicatorAttr extends SkinAttr {
    @Override
    protected void applySkin(View view) {
        if (view instanceof TabLayout) {
            TabLayout tl = (TabLayout) view;
            if (isColor()) {
                int color = SkinResourcesUtils.getColor(attrValueRefId);
                tl.setSelectedTabIndicatorColor(color);
            }
        }
    }
}
