package org.group;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.commview.R;

public class TextNextIconLayout extends ConstraintLayout {

    public TextNextIconLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextNextIconLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    String leftText, rightText;
    TextView leftTextView, rightTextView;

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommTextNextIconLayout);
        leftText = ta.getString(R.styleable.CommTextNextIconLayout_LeftTitleText);
        rightText = ta.getString(R.styleable.CommTextNextIconLayout_RightValueText);
        ta.recycle();
        initViewWidget(context);
    }

    private void initViewWidget(Context context) {
        View.inflate(context, R.layout.comm_view_text_next_icon_layout, this);
        leftTextView = (TextView) findViewById(R.id.left_text);
        rightTextView = (TextView) findViewById(R.id.right_text);
        leftTextView.setText(leftText);
        rightTextView.setText(rightText);
    }

    public String getRightText() {
        return rightTextView.getText().toString();
    }

    public void setRightTextView(String text) {
     /*   if (text.equals("去设置") || text.contains("去认证") || text.contains("审核中")) {
            rightTextView.setTextColor(getResources().getColor(R.color.xiu_red));
        } else {
            rightTextView.setTextColor(getResources().getColor(R.color.common_gray));
        }*/
        rightTextView.setText(text);
    }


}
