package org.group;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.commview.R;

/**
 * File: IconTextNextLayout.java
 * Author: yuzhuzhang
 * Create: 2020/3/6 9:23 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/6 : Create IconTextNextLayout.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class IconTextNextLayout extends ConstraintLayout {


    public IconTextNextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconTextNextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    String itemText;
    Drawable icon;
    ImageView iconHolder;
    TextView itemTextView;

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommIconTextNextLayout);
        icon = ta.getDrawable(R.styleable.CommIconTextNextLayout_icon);
        itemText = ta.getString(R.styleable.CommIconTextNextLayout_itemText);
        ta.recycle();
        initViewWidget(context);
    }

    private void initViewWidget(Context context) {
        View.inflate(context, R.layout.comm_view_icon_text_next_layout, this);
        iconHolder = (ImageView) findViewById(R.id.icon);
        itemTextView = (TextView) findViewById(R.id.item_text);
        itemTextView.setText(itemText);
        iconHolder.setImageDrawable(icon);
    }

    public void setItemText(String text) {
     /*   if (text.equals("去设置") || text.contains("去认证") || text.contains("审核中")) {
            rightTextView.setTextColor(getResources().getColor(R.color.xiu_red));
        } else {
            rightTextView.setTextColor(getResources().getColor(R.color.common_gray));
        }*/
        itemTextView.setText(text);
    }
}
