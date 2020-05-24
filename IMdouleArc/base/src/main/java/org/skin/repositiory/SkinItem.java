package org.skin.repositiory;

import android.view.View;

import org.skin.attr.SkinAttr;
import org.skin.utils.SkinListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * File: SkinItem.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 11:05 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create SkinItem.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class SkinItem {
    public View view;

    public List<SkinAttr> attrs;

    public SkinItem() {
        attrs = new ArrayList<>();
    }

    public void apply() {
        if (SkinListUtils.isEmpty(attrs)) {
            return;
        }
        for (SkinAttr at : attrs) {
            at.apply(view);
        }
    }

    public void clean() {
        if (!SkinListUtils.isEmpty(attrs)) {
            for(SkinAttr at : attrs){
                at = null;
            }
        }
    }

    @Override
    public String toString() {
        return "SkinItem [view=" + view.getClass().getSimpleName() + ", attrs=" + attrs + "]";
    }
}

