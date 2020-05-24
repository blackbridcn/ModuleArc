package org.xbanner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 继承ViewPager，重写setPageTransformer方法，移除版本限制，通过反射设置参数和方法
 */
public class XBannerViewPager extends ViewPager {

    private boolean mIsAllowUserScroll = true;
    private AutoPlayDelegate mAutoPlayDelegate;

    public XBannerViewPager(Context context) {
        super(context);
    }

    public XBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        Class viewpagerClass = ViewPager.class;
        try {
            boolean hasTransformer = transformer != null;
            Field pageTransformerField = viewpagerClass.getDeclaredField("mPageTransformer");
            pageTransformerField.setAccessible(true);
            PageTransformer mPageTransformer = (PageTransformer) pageTransformerField.get(this);

            boolean needsPopulate = hasTransformer != (mPageTransformer != null);
            pageTransformerField.set(this, transformer);

            Method setChildrenDrawingOrderEnabledCompatMethod = viewpagerClass.getDeclaredMethod("setChildrenDrawingOrderEnabledCompat", boolean.class);
            setChildrenDrawingOrderEnabledCompatMethod.setAccessible(true);
            setChildrenDrawingOrderEnabledCompatMethod.invoke(this, hasTransformer);

            Field drawingOrderField = viewpagerClass.getDeclaredField("mDrawingOrder");
            drawingOrderField.setAccessible(true);
            if (hasTransformer) {
                drawingOrderField.setInt(this, reverseDrawingOrder ? 2 : 1);
            } else {
                drawingOrderField.setInt(this, 0);
            }

            if (needsPopulate) {
                Method populateMethod = viewpagerClass.getDeclaredMethod("populate");
                populateMethod.setAccessible(true);
                populateMethod.invoke(this);
            }
        } catch (Exception e) {
        }
    }


    /**
     * 设置ViewPager的滚动速度
     * <p>
     * 反射获取mScroller 修改滑动值
     *
     * @param duration page切换的时间长度
     */
    public void setScrollDuration(int duration) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new XBannerScroller(getContext(), duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换到指定索引的页面，主要用于自动轮播
     *
     * @param position
     */
    public void setBannerCurrentItemInternal(int position, boolean smoothScroll) {
        Class viewpagerClass = ViewPager.class;
        try {
            Method setCurrentItemInternalMethod = viewpagerClass.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class);
            setCurrentItemInternalMethod.setAccessible(true);
            setCurrentItemInternalMethod.invoke(this, position, smoothScroll, true);
            ViewCompat.postInvalidateOnAnimation(this);
        } catch (Exception e) {
        }
    }

    /**
     * 设置是否允许用户手指滑动
     *
     * @param iSallowUserScroll
     */
    public void setIsAllowUserScroll(boolean iSallowUserScroll) {
        mIsAllowUserScroll = iSallowUserScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIsAllowUserScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsAllowUserScroll) {
            if (mAutoPlayDelegate != null && (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP)) {
                mAutoPlayDelegate.handleAutoPlayActionUpOrCancel(getXVelocity());
                return false;
            } else {
                return super.onTouchEvent(ev);
            }
        } else {
            return false;
        }
    }

    private float getXVelocity() {
        float xVelocity = 0;
        Class viewpagerClass = ViewPager.class;
        try {
            Field velocityTrackerField = viewpagerClass.getDeclaredField("mVelocityTracker");
            velocityTrackerField.setAccessible(true);
            VelocityTracker velocityTracker = (VelocityTracker) velocityTrackerField.get(this);

            Field activePointerIdField = viewpagerClass.getDeclaredField("mActivePointerId");
            activePointerIdField.setAccessible(true);

            Field maximumVelocityField = viewpagerClass.getDeclaredField("mMaximumVelocity");
            maximumVelocityField.setAccessible(true);
            int maximumVelocity = maximumVelocityField.getInt(this);

            velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
            xVelocity = VelocityTrackerCompat.getXVelocity(velocityTracker, activePointerIdField.getInt(this));
        } catch (Exception ignored) {
        }
        return xVelocity;
    }

    public void setAutoPlayDelegate(AutoPlayDelegate autoPlayDelegate) {
        mAutoPlayDelegate = autoPlayDelegate;
    }

    public interface AutoPlayDelegate {
        void handleAutoPlayActionUpOrCancel(float xVelocity);
    }
}