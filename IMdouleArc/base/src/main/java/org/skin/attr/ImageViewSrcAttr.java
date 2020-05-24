package org.skin.attr;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import org.skin.utils.SkinResourcesUtils;

/**
 * File: ImageViewSrcAttr.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 10:29 AM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create ImageViewSrcAttr.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ImageViewSrcAttr extends SkinAttr {
    @Override
    protected void applySkin(View view) {
        if (view instanceof ImageView) {
            ImageView iv = (ImageView) view;
            if (isDrawable()) {
                iv.setImageDrawable(SkinResourcesUtils.getDrawable(attrValueRefId));
            } else if (isColor()) {
                iv.setImageDrawable(new ColorDrawable(SkinResourcesUtils.getColor(attrValueRefId)));
            }
        }
    }
}
