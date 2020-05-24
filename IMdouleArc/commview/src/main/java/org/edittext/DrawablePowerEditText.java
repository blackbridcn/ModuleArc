package org.edittext;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;


/**
 * Author: yuzzha
 * Date: 2019-07-01 17:32
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class DrawablePowerEditText extends PowerEditText {

    private int drawableWidth;
    private int iconPadding;
    private DrawablePosition position;

    Rect bounds;

    private enum DrawablePosition {
        NONE,
        LEFT_AND_RIGHT,
        LEFT,
        RIGHT
    }


    public DrawablePowerEditText(Context context) {
        super(context);
        init();
    }

    public DrawablePowerEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawablePowerEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        if (null == bounds) {
            bounds = new Rect();
        }
    }

    public void setIconPadding(int padding) {
        iconPadding = padding;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Paint textPaint = getPaint();
        String text = getText().toString();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        int textWidth = bounds.width();
        int factor = (position == DrawablePosition.LEFT_AND_RIGHT) ? 2 : 1;
        int contentWidth = drawableWidth + iconPadding * factor + textWidth;
        int horizontalPadding = (int) ((getWidth() / 2.0) - (contentWidth / 2.0));

        setCompoundDrawablePadding(-horizontalPadding + iconPadding);

        switch (position) {
            case LEFT:
                setPadding(horizontalPadding, getPaddingTop(), 0, getPaddingBottom());
                break;

            case RIGHT:
                setPadding(0, getPaddingTop(), horizontalPadding, getPaddingBottom());
                break;

            case LEFT_AND_RIGHT:
                setPadding(horizontalPadding, getPaddingTop(), horizontalPadding, getPaddingBottom());
                break;

            default:
                setPadding(0, getPaddingTop(), 0, getPaddingBottom());
        }
    }


    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

        if (left != null && right != null) {
            drawableWidth = left.getIntrinsicWidth() + right.getIntrinsicWidth();
            position = DrawablePosition.LEFT_AND_RIGHT;
        } else if (left != null) {
            drawableWidth = left.getIntrinsicWidth();
            position = DrawablePosition.LEFT;
        } else if (right != null) {
            drawableWidth = right.getIntrinsicWidth();
            position = DrawablePosition.RIGHT;
        } else {
            position = DrawablePosition.NONE;
        }
        requestLayout();
    }


}
