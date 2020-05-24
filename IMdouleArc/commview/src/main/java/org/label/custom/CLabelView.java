package org.label.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;

import com.commview.R;

/**
 * File: CLabelView.java
 * Author: yuzhuzhang
 * Create: 2020-01-19 22:55
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-01-19 : Create CLabelView.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class CLabelView extends View {
    //https://blog.csdn.net/qq_26787115/article/details/50466655
    //https://www.jianshu.com/p/bef2482c675d

    Paint mTextPaint;
    int mTextColor;
    float mTextSize;
    float mTextHeight;
    float mTextWidth;
    int mTextStyle;

    float mTopPadding;
    float mBottomPadding;
    float mCenterPadding;

    Paint mTrianglePaint;
    int mBackGroundColor;

    float mDegrees;

    String mText = "Top";
    //String mNum = "01";

    int width;
    int height;

    public static final int DEGREES_LEFT = -45;
    public static final int DEGREES_RIGHT = 45;


    public CLabelView(Context context) {
        this(context, null);
    }

    public CLabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
        mTopPadding = ta.getDimension(R.styleable.LabelTextView_labelTopPadding, dp2px(7));
        mCenterPadding = ta.getDimension(R.styleable.LabelTextView_labelCenterPadding, dp2px(3));
        mBottomPadding = ta.getDimension(R.styleable.LabelTextView_labelBottomPadding, dp2px(3));

        mBackGroundColor = ta.getColor(R.styleable.LabelTextView_backgroundColor, Color.parseColor("#66000000"));
        mTextColor = ta.getColor(R.styleable.LabelTextView_textColor, Color.WHITE);
        mTextSize = ta.getDimension(R.styleable.LabelTextView_textSize, sp2px(8));
        mText = ta.getString(R.styleable.LabelTextView_text);
        mTextStyle = ta.getInt(R.styleable.LabelTextView_textStyle, 0);
        mDegrees = ta.getInt(R.styleable.LabelTextView_direction, 45);
        ta.recycle();

        initTextPaint();
        initNumPaint();
        initTrianglePaint();

        resetTextStatus();
    }

    public void setText(String text) {
        mText = text;
        resetTextStatus();
        invalidate();
    }

    public void setBackGroundColor(@ColorInt int color) {
        mTrianglePaint.setColor(color);
        invalidate();
    }

    private void initTextPaint() {
        //初始化绘制修饰文本的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        if (mTextStyle == 1) {
            mTextPaint.setTypeface(Typeface.SANS_SERIF);
        } else if (mTextStyle == 2) {
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void initNumPaint() {
        //初始化绘制数字文本的画笔
    /*    mNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumPaint.setColor(mNumColor);
        mNumPaint.setTextAlign(Paint.Align.CENTER);
        mNumPaint.setTextSize(mNumSize);
        if (mNumStyle == 1) {
            mNumPaint.setTypeface(Typeface.SANS_SERIF);
        } else if (mNumStyle == 2) {
            mNumPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }*/
    }

    private void initTrianglePaint() {
        //初始化绘制三角形背景的画笔
        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setColor(mBackGroundColor);
    }

    private void resetTextStatus() {
        // 测量文字高度
        Rect rectText = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), rectText);
        mTextWidth = rectText.width();
        mTextHeight = rectText.height();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //绘制三角形背景
        Path path = new Path();
        //位移和旋转canvas
        canvas.translate(0, (float) ((height * Math.sqrt(2)) - height));
        if (mDegrees == DEGREES_LEFT) {
            canvas.rotate(mDegrees, 0, height);
        } else if (mDegrees == DEGREES_RIGHT) {
            canvas.rotate(mDegrees, width, height);
        }

        path.moveTo(0, height);
        path.lineTo(height >> 1, height >> 1);
        path.lineTo((height >> 1) * 3, height >> 1);
        path.lineTo(width, height);
        path.close();

        canvas.drawPath(path, mTrianglePaint);
        // 绘制文字需要居中显示的区域  绘制时控制文本绘制的范围
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int baseline = (height >> 1) * 3 - (fontMetricsInt.top + fontMetricsInt.bottom) >> 1;
        canvas.drawText(mText, width >> 1, baseline, mTextPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = (int) ((mTopPadding + mCenterPadding + mBottomPadding + mTextHeight /*+ mNumHeight*/) * (1.5));
        width = height << 1;
        //控件的真正高度，勾股定理...
        int realHeight = (int) (height * Math.sqrt(2));
        setMeasuredDimension(width, realHeight);
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }
}

