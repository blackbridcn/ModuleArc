package org.gradview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Author: yuzzha
 * Date: 12/25/2019 5:51 PM
 * Description:
 * Remark:
 */
public class NoScrollGridView extends GridView {

    Context mContext;
    double nLenStart = 0;

    public NoScrollGridView(Context context) {
        super(context);
        mContext = context;
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);

    }
}
