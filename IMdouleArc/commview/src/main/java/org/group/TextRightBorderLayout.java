package org.group;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.commview.R;

import org.jetbrains.annotations.NotNull;
import org.utils.ViewUtils;

/**
 * Author: yuzzha
 * Date: 1/21/2020 3:40 PM
 * Description:
 * Remark:
 */
public class TextRightBorderLayout extends ConstraintLayout {

    public TextRightBorderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextRightBorderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TextRightBorderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    int leftIcon;
    String leftText, rightText;
    TextView leftTextView, rightTextView;
    ImageView leftImageIcon;

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommTextRightBorderLayout);
        leftText = ta.getString(R.styleable.CommTextRightBorderLayout_leftText);
        rightText = ta.getString(R.styleable.CommTextRightBorderLayout_rightText);
        leftIcon = ta.getResourceId(R.styleable.CommTextRightBorderLayout_leftIcon, R.drawable.comm_view_ble_shcutcut);
        ta.recycle();
        initViewWidget(context);
    }


    private void initViewWidget(Context context) {
        View rootView = View.inflate(context, R.layout.comm_view_extends_text_layout, this);
        leftImageIcon = (ImageView) findViewById(R.id.left_icon);
        leftTextView = (TextView) findViewById(R.id.left_text);
        rightTextView = (TextView) findViewById(R.id.right_text);
        leftTextView.setText(leftText);
        if (!ViewUtils.isEmpty(rightText))
            rightTextView.setText(rightText);
        leftImageIcon.setImageResource(leftIcon);
    }

    public void setLeftText(@NotNull String text) {
        leftTextView.setText(text);
    }

    public void setLeftIcon(@DrawableRes int leftIcon) {
        leftImageIcon.setImageResource(leftIcon);
    }

    public void setRightText(@NotNull String text) {
        rightTextView.setText(text);
    }

    public void setRightOnClick(@androidx.annotation.Nullable OnClickListener l) {
        rightTextView.setOnClickListener(l);
    }


}
