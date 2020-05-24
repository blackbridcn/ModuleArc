package org.skin.action;

import android.view.View;
import android.widget.TextView;

import org.skin.attr.DynamicAttr;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 3/24/2020 2:40 PM
 * Description:
 * Remark:
 */
public interface IDynamicNewView {

    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);


    void dynamicAddView(View view, String attrName, int attrValueResId);

    /**
     * add the textview for font switch
     *
     * @param textView textview
     */
    void dynamicAddFontView(TextView textView);
}
