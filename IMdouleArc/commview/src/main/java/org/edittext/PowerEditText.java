package org.edittext;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.commview.R;


/**
 * Author: yuzzha
 * Date: 2019-06-27 10:34
 * Description: EditText扩展类 主要是提供输入不合法时 View 抖动功能
 * Remark:
 */
public class PowerEditText extends AppCompatEditText implements TextWatcher {


    private static final int DEFAULT_CLEAR_RES = R.drawable.comm_view_widget_edittext_clear_all;
    private static final int DEFAULT_VISIBLE_RES = R.drawable.comm_view_widget_edittext_visible_pwsd;
    private static final int DEFAULT_INVISIBLE_RES = R.drawable.comm_view_widget_edittext_invisible;

    private static final int DEFAULT_STYLE_COLOR = Color.BLUE;

    private final int DEFAULT_BUTTON_PADDING = getResources().getDimensionPixelSize(R.dimen.comm_view_widget_edittext_padding);
    private final int DEFAULT_BUTTON_WIDTH =
            getResources().getDimensionPixelSize(R.dimen.comm_view_widget_edittext_width);

    private static final String STYLE_RECT = "rectangle";
    private static final String STYLE_ROUND_RECT = "roundRect";
    private static final String STYLE_HALF_RECT = "halfRect";
    private static final String STYLE_ANIMATOR = "animator";

    private static final int DEFAULT_ROUND_RADIUS = 20;
    private static final int ANIMATOR_TIME = 200;
    private static final int DEFAULT_FOCUSED_STROKE_WIDTH = 8;
    private static final int DEFAULT_UNFOCUSED_STROKE_WIDTH = 4;

    //按钮间隔
    private int mBtnPadding = 0;
    //按钮宽度
    private int mBtnWidth = 0;
    //右内边距
    private int mTextPaddingRight;

    private int mClearResId = 0;
    private int mVisibleResId = 0;
    private int mInvisibleResId = 0;
    private Bitmap mBitmapClear;
    private Bitmap mBitmapVisible;
    private Bitmap mBitmapInvisible;

    private String mBorderStyle = "";
    private int mStyleColor = -1;

    //出现和消失动画
    private ValueAnimator mGoneAnimator;
    private ValueAnimator mVisibleAnimator;
    //状态值
    private boolean isBtnVisible = false;
    private boolean isPassword = false;
    private boolean isPasswordVisible = false;

    private boolean isAnimatorRunning = false;
    private int mAnimatorProgress = 0;
    private ObjectAnimator mAnimator;
    public boolean isTel = false;

    //自定义属性动画
    private static final Property<PowerEditText, Integer> BORDER_PROGRESS
            = new Property<PowerEditText, Integer>(Integer.class, "borderProgress") {
        @Override
        public Integer get(PowerEditText powerfulEditText) {
            return powerfulEditText.getBorderProgress();
        }

        @Override
        public void set(PowerEditText powerfulEditText, Integer value) {
            powerfulEditText.setBorderProgress(value);
        }
    };

    private Paint mPaint;

    public PowerEditText(Context context) {
        this(context, null);
    }

    public PowerEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PowerEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        //抗锯齿和位图滤波
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        //读取xml文件中的配置
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommViewPowerEditText);
            isTel = array.getBoolean(R.styleable.CommViewPowerEditText_isTel, false);
            for (int i = 0; i < array.getIndexCount(); i++) {
                int attr = array.getIndex(i);
                if (attr == R.styleable.CommViewPowerEditText_clearDrawable) {
                    mClearResId = array.getResourceId(attr, DEFAULT_CLEAR_RES);
                } else if (attr == R.styleable.CommViewPowerEditText_visibleDrawable) {
                    mVisibleResId = array.getResourceId(attr, DEFAULT_VISIBLE_RES);
                } else if (attr == R.styleable.CommViewPowerEditText_invisibleDrawable) {
                    mInvisibleResId = array.getResourceId(attr, DEFAULT_INVISIBLE_RES);
                } else if (attr == R.styleable.CommViewPowerEditText_BtnWidth) {
                    mBtnWidth = array.getDimensionPixelSize(attr, DEFAULT_BUTTON_WIDTH);
                } else if (attr == R.styleable.CommViewPowerEditText_BtnSpacing) {
                    mBtnPadding = array.getDimensionPixelSize(attr, DEFAULT_BUTTON_PADDING);
                } else if (attr == R.styleable.CommViewPowerEditText_borderStyle) {
                    mBorderStyle = array.getString(attr);
                } else if (attr == R.styleable.CommViewPowerEditText_styleColor) {
                    mStyleColor = array.getColor(attr, DEFAULT_STYLE_COLOR);
                }
            }
            array.recycle();
        }

        //初始化按钮显示的Bitmap
        mBitmapClear = createBitmap(context, mClearResId, DEFAULT_CLEAR_RES);
        mBitmapVisible = createBitmap(context, mVisibleResId, DEFAULT_VISIBLE_RES);
        mBitmapInvisible = createBitmap(context, mInvisibleResId, DEFAULT_INVISIBLE_RES);
        //如果自定义，则使用自定义的值，否则使用默认值
        if (mBtnPadding == 0) {
            mBtnPadding = DEFAULT_BUTTON_PADDING;
        }
        if (mBtnWidth == 0) {
            mBtnWidth = DEFAULT_BUTTON_WIDTH;
        }
        //给文字设置一个padding，避免文字和按钮重叠了
        mTextPaddingRight = mBtnPadding * 4 + mBtnWidth * 2;

        //按钮出现和消失的动画
        mGoneAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME);
        mVisibleAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(ANIMATOR_TIME);

        //是否是密码样式
        isPassword = getInputType() == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);

        addTextChangedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //设置右内边距, 防止清除按钮和文字重叠
        setPadding(getPaddingLeft(), getPaddingTop(), mTextPaddingRight, getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.STROKE);

        //使用自定义颜色。如未定义，则使用默认颜色
        if (mStyleColor != -1) {
            mPaint.setColor(mStyleColor);
        } else {
            mPaint.setColor(DEFAULT_STYLE_COLOR);
        }

        //控件获取焦点时，加粗边框
        if (isFocused()) {
            mPaint.setStrokeWidth(DEFAULT_FOCUSED_STROKE_WIDTH);
        } else {
            mPaint.setStrokeWidth(DEFAULT_UNFOCUSED_STROKE_WIDTH);
        }

        //绘制清空和明文显示按钮
        drawBorder(canvas);
        //绘制边框
        drawButtons(canvas);
    }

    private void drawBorder(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        switch (mBorderStyle) {
            //矩形样式
            case STYLE_RECT:
                setBackground(null);
                canvas.drawRect(0, 0, width, height, mPaint);
                break;

            //圆角矩形样式
            case STYLE_ROUND_RECT:
                setBackground(null);
                float roundRectLineWidth = 0;
                if (isFocused()) {
                    roundRectLineWidth = DEFAULT_FOCUSED_STROKE_WIDTH / 2;
                } else {
                    roundRectLineWidth = DEFAULT_UNFOCUSED_STROKE_WIDTH / 2;
                }
                mPaint.setStrokeWidth(roundRectLineWidth);
                if (Build.VERSION.SDK_INT >= 21) {
                    canvas.drawRoundRect(
                            roundRectLineWidth / 2, roundRectLineWidth / 2, width - roundRectLineWidth / 2, height - roundRectLineWidth / 2,
                            DEFAULT_ROUND_RADIUS, DEFAULT_ROUND_RADIUS,
                            mPaint);
                } else {
                    canvas.drawRoundRect(
                            new RectF(roundRectLineWidth / 2, roundRectLineWidth / 2, width - roundRectLineWidth / 2, height - roundRectLineWidth / 2),
                            DEFAULT_ROUND_RADIUS, DEFAULT_ROUND_RADIUS,
                            mPaint);
                }
                break;

            //半矩形样式
            case STYLE_HALF_RECT:
                setBackground(null);
                canvas.drawLine(0, height, width, height, mPaint);
                canvas.drawLine(0, height / 2, 0, height, mPaint);
                canvas.drawLine(width, height / 2, width, height, mPaint);
                break;

            //动画特效样式
            case STYLE_ANIMATOR:
                setBackground(null);
                if (isAnimatorRunning) {
                    canvas.drawLine(width / 2 - mAnimatorProgress, height, width / 2 + mAnimatorProgress, height, mPaint);
                    if (mAnimatorProgress == width / 2) {
                        isAnimatorRunning = false;
                    }
                } else {
                    canvas.drawLine(0, height, width, height, mPaint);
                }
                break;
        }
    }

    private void drawButtons(Canvas canvas) {
        if (isBtnVisible) {
            //播放按钮出现的动画
            if (mVisibleAnimator.isRunning()) {
                float scale = (float) mVisibleAnimator.getAnimatedValue();
                drawClearButton(scale, canvas);
                if (isPassword) {
                    drawVisibleButton(scale, canvas, isPasswordVisible);
                }
                invalidate();
                //绘制静态的按钮
            } else {
                drawClearButton(1, canvas);
                if (isPassword) {
                    drawVisibleButton(1, canvas, isPasswordVisible);
                }
            }
        } else {
            //播放按钮消失的动画
            if (mGoneAnimator.isRunning()) {
                float scale = (float) mGoneAnimator.getAnimatedValue();
                drawClearButton(scale, canvas);
                if (isPassword) {
                    drawVisibleButton(scale, canvas, isPasswordVisible);
                }
                invalidate();
            }
        }
    }

    private void drawClearButton(float scale, Canvas canvas) {
        int right = (int) (getWidth() + getScrollX() - mBtnPadding - mBtnWidth * (1f - scale) / 2f);
        int left = (int) (getWidth() + getScrollX() - mBtnPadding - mBtnWidth * (scale + (1f - scale) / 2f));
        int top = (int) ((getHeight() - mBtnWidth * scale) / 2);
        int bottom = (int) (top + mBtnWidth * scale);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBitmapClear, null, rect, mPaint);
    }

    private void drawVisibleButton(float scale, Canvas canvas, boolean isVisible) {
        int right = (int) (getWidth() + getScrollX() - mBtnPadding * 3 - mBtnWidth - mBtnWidth * (1f - scale) / 2f);
        int left = (int) (getWidth() + getScrollX() - mBtnPadding * 3 - mBtnWidth - mBtnWidth * (scale + (1f - scale) / 2f));
        int top = (int) ((getHeight() - mBtnWidth * scale) / 2);
        int bottom = (int) (top + mBtnWidth * scale);
        Rect rect = new Rect(left, top, right, bottom);
        if (isVisible) {
            canvas.drawBitmap(mBitmapVisible, null, rect, mPaint);
        } else {
            canvas.drawBitmap(mBitmapInvisible, null, rect, mPaint);
        }
    }

    // 清除按钮出现时的动画效果
    private void startVisibleAnimator() {
        endAllAnimator();
        mVisibleAnimator.start();
        invalidate();
    }

    // 清除按钮消失时的动画效果
    private void startGoneAnimator() {
        endAllAnimator();
        mGoneAnimator.start();
        invalidate();
    }

    // 结束所有动画
    private void endAllAnimator() {
        mGoneAnimator.end();
        mVisibleAnimator.end();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        //播放按钮出现和消失动画
        if (focused && getText().length() > 0) {
            if (!isBtnVisible) {
                isBtnVisible = true;
                startVisibleAnimator();
            }
        } else {
            if (isBtnVisible) {
                isBtnVisible = false;
                startGoneAnimator();
            }
        }

        //实现动画特效样式
        if (focused && mBorderStyle.equals(STYLE_ANIMATOR)) {
            isAnimatorRunning = true;
            mAnimator = ObjectAnimator.ofInt(this, BORDER_PROGRESS, 0, getWidth() / 2);
            mAnimator.setDuration(ANIMATOR_TIME);
            mAnimator.start();
        }
    }

    protected void setBorderProgress(int borderProgress) {
        mAnimatorProgress = borderProgress;
        postInvalidate();
    }

    protected int getBorderProgress() {
        return mAnimatorProgress;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int after) {
        //这个方法被调用，说明在text字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本。在这个方法里面改变s，会报错。
        super.onTextChanged(text, start, before, after);
        if (text.length() > 0 && isFocused()) {
            if (!isBtnVisible) {
                isBtnVisible = true;
                startVisibleAnimator();
            }
        } else {
            if (isBtnVisible) {
                isBtnVisible = false;
                startGoneAnimator();
            }
        }
        if (checkBlank(text, start, before, after)) {
            setTelStyle(text, start, before, after);
        }
        if (this.listener != null) {
            this.listener.showTextTipTask();
        }
    }

    private OnShowTextTipListener listener;

    public void setOnShowTextTipListener(OnShowTextTipListener listener) {
        this.listener = listener;
    }

    public interface OnShowTextTipListener {
        void showTextTipTask();
    }

    @Override
    public void afterTextChanged(Editable s) {
        //将光标移动到末尾
        setSelection(getText().toString().length());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            boolean clearTouched =
                    (getWidth() - mBtnPadding - mBtnWidth < event.getX())
                            && (event.getX() < getWidth() - mBtnPadding)
                            && isFocused();
            boolean visibleTouched =
                    (getWidth() - mBtnPadding * 3 - mBtnWidth * 2 < event.getX())
                            && (event.getX() < getWidth() - mBtnPadding * 3 - mBtnWidth)
                            && isPassword && isFocused();

            if (clearTouched) {
                setError(null);
                setText("");
                return true;
            } else if (visibleTouched) {
                if (isPasswordVisible) {
                    isPasswordVisible = false;
                    setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    setSelection(getText().length());
                    invalidate();
                } else {
                    isPasswordVisible = true;
                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    setSelection(getText().length());
                    invalidate();
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    // 开始晃动的入口
    public void startShakeAnimation() {
        if (getAnimation() == null) {
            setAnimation(shakeAnimation(6));
        }
        startAnimation(getAnimation());
    }

    /**
     * 晃动动画
     *
     * @param counts 0.5秒钟晃动多少下
     * @return
     */
    private Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(5, -5, -1, 1);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    private Bitmap createBitmap(Context context, int resId, int defResId) {
        if (resId != 0) {
            return BitmapFactory.decodeResource(context.getResources(), resId);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), defResId);
        }
    }

    /**
     * 设置手机号码显示格式
     * xxx xxxx xxxx
     *
     * @param s
     * @param count
     */
    private void setTelStyle(CharSequence s, int count) {
        int length = s.toString().length();
        //删除数字
        if (count == 0) {
            if (length == 4) {
                setText(s.subSequence(0, 3));
            }
            if (length == 9) {
                setText(s.subSequence(0, 8));
            }
        }
        //添加数字
        if (count == 1) {
            if (length == 4) {
                String part1 = s.subSequence(0, 3).toString();
                String part2 = s.subSequence(3, length).toString();
                setText(part1 + " " + part2);
            }
            if (length == 9) {
                String part1 = s.subSequence(0, 8).toString();
                String part2 = s.subSequence(8, length).toString();
                setText(part1 + " " + part2);
            }
            if (length > 13) {
                setText(s.subSequence(0, 13).toString());
            }
        }
    }


    private boolean isRun = false;

    /**
     * 设置手机号码格式3-4-4
     *
     * @param s
     */
    private StringBuilder mSb = null;

    private void setTelStyle(CharSequence s, int start, int before, int count) {
        if (isRun) {//这几句要加，不然每输入一个值都会执行两次onTextChanged()，导致堆栈溢出，原因不明
            isRun = false;
            return;
        }
        isRun = true;

        //打开手机号码格式3-4-4
        if (isTel) {
            if (s == null || s.length() == 0)
                return;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                    continue;
                } else {
                    sb.append(s.charAt(i));
                    if ((sb.length() == 4 || sb.length() == 9) &&
                            sb.charAt(sb.length() - 1) != ' ') {
                        sb.insert(sb.length() - 1, ' ');
                    }
                }
            }
            if (!sb.toString().equals(s.toString())) {
                int index = start + 1;
                if (sb.charAt(start) == ' ') {
                    if (before == 0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }
                this.setText(sb.toString());
                this.setSelection(index);
            }
        }
    }

    //添加判断条件，防止出现 185 xxxx xxxxxxxx，和输入空格崩溃的情况
    public boolean checkBlank(CharSequence s, int start, int before, int count) {
        if (isTel) {
            if (start > 12) {
                if (s.length() >= 13) {
                    this.setText(s.subSequence(0, 13));
                    this.setSelection(13);
                }
                return false;
            }
            if (s.length() == 0 || s.length() == 4 || s.length() == 9) {
                return true;
            }
            if (s.charAt(s.length() - 1) != ' ') {
                return true;
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != s.length() - 1) {
                        sb.append(s.charAt(i));
                    }
                }
                this.setText(sb.toString());
                this.setSelection(s.length() - 1);
                return false;
            }
        } else {
            return false;
        }
    }


    public void setTel(boolean tel) {
        isTel = tel;
    }
}

