package org.pswdlevel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 密码强度等级
 * <p>
 */
public class PasswordStrongLevelView extends View {

    // 密码等级
    public static final int LEVEL_DEF = 0;
    public static final int LEVEL_LOWER = 1;
    public static final int LEVEL_MIDDLE = 2;
    public static final int LEVEL_SENIOR = 3;
    public static final int LEVEL_TOP = 4;

    //强度效果颜色
    /*默认等级：强度效果颜色Color 灰色*/
    private int COLOR_LEVEL_DEF = Color.parseColor("#d8d8d8");
    /*初级：强度效果颜色Color 红色*/
    private int COLOR_LEVEL_LOWER = Color.parseColor("#e72e1d");
    /*中级：强度效果颜色Color 橙黄色*/
    private int COLOR_LEVEL_MIDDLE = Color.parseColor("#eaa215");
    /*高级：强度效果颜色Color 蓝色*/
    private int COLOR_LEVEL_SENIOR = Color.parseColor("#267bea");
    /*最高级：强度效果颜色Color 绿色*/
    private int COLOR_LEVEL_TOP = Color.parseColor("#00FF00");

    private int passwordLevel = LEVEL_DEF;
    private Paint paint;
    private RectF oval1;
    private RectF oval2;
    private RectF oval3;

    public void setPasswordLevel(int passwordLevel) {
        this.passwordLevel = passwordLevel;
        invalidate();
    }


    public PasswordStrongLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreat();
    }

    public PasswordStrongLevelView(Context context) {
        super(context);
        onCreat();
    }

    private void onCreat() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        oval1 = new RectF(10, 25, 100, 45);
        oval2 = new RectF(120, 25, 210, 45);
        oval3 = new RectF(230, 25, 320, 45);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (passwordLevel) {
            case LEVEL_DEF:
                paint.setColor(COLOR_LEVEL_DEF);
                canvas.drawRoundRect(oval1, 10, 10, paint);
                canvas.drawRoundRect(oval2, 10, 10, paint);
                canvas.drawRoundRect(oval3, 10, 10, paint);

                break;
            case LEVEL_LOWER:
                paint.setColor(COLOR_LEVEL_LOWER);
                canvas.drawRoundRect(oval1, 10, 10, paint);
                paint.setColor(COLOR_LEVEL_DEF);
                canvas.drawRoundRect(oval2, 10, 10, paint);
                canvas.drawRoundRect(oval3, 10, 10, paint);
                break;
            case LEVEL_MIDDLE:
                //paint.setColor(COLOR_LEVEL_LOWER);
                paint.setColor(COLOR_LEVEL_MIDDLE);
                canvas.drawRoundRect(oval1, 10, 10, paint);
                canvas.drawRoundRect(oval2, 10, 10, paint);
                paint.setColor(COLOR_LEVEL_DEF);
                canvas.drawRoundRect(oval3, 10, 10, paint);
                break;
            case LEVEL_SENIOR:
                paint.setColor(COLOR_LEVEL_SENIOR);
                canvas.drawRoundRect(oval1, 10, 10, paint);
                canvas.drawRoundRect(oval2, 10, 10, paint);
                canvas.drawRoundRect(oval3, 10, 10, paint);
                break;
            case LEVEL_TOP:
                paint.setColor(COLOR_LEVEL_TOP);
                canvas.drawRoundRect(oval1, 10, 10, paint);
                canvas.drawRoundRect(oval2, 10, 10, paint);
                canvas.drawRoundRect(oval3, 10, 10, paint);
                break;

        }
    }
}
