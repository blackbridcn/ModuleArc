package org.textview;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.commview.R;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 2019-06-27 10:23
 * Description: 倒计时View
 * Remark:
 */
public class CountDownTextView extends AppCompatTextView implements LifecycleObserver {

    //倒计时长
    private long millisInFuture = 60 * 1000;
    //倒计时间隔时间
    private long countDownInterval = 1000;
    private long delyTime;
    private CountDownTimer mCountDownTimer;
    private String count, normal;
    private OnClickListener onClickListener;

    private static final String SHARED_PREFERENCES_FILE = "CountDownTextView";
    private static final String SP_KEY_LAST_TIME = "last_count_time";
    private static final String SP_KEY_LAST_TIMESTAMP = "last_count_timestamp";
    private static final String SP_KEY_INTERVAL = "count_interval";
    private Handler handler = new Handler(Looper.getMainLooper());

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        autoBindLifecycle(context);
      //  setOnClickListener(this);
        count = getResources().getString(R.string.comm_view_count_down_text_interval);
        normal = getResources().getString(R.string.comm_view_count_down_text_normal);
    }

    /**
     * 控件自动绑定生命周期,宿主可以是activity或者fragment
     */
    private void autoBindLifecycle(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            for (Fragment fragment : fragments) {
                View parent = fragment.getView();
                if (parent != null) {
                    View find = parent.findViewById(getId());
                    if (find == this) {
                        fragment.getLifecycle().addObserver(this);
                        return;
                    }
                }
            }
        }
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (!checkLastCountTimestamp()) {
            countFinish();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mCountDownTimer != null && delyTime != 0) {
            setLastCountTimestamp(delyTime, countDownInterval);
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

 /*   @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(this);
        this.onClickListener = l;
    }*/

 /*   @Override
    public void onClick(View v) {
    //
        if (onClickListener == null) {
            onClickListener.onClick(v);
        }
    }*/

 public void startCount(){
     startCount(millisInFuture, countDownInterval);
 }

    /**
     * 开始倒计时
     *
     * @param millisInFuture    倒计时时间
     * @param countDownInterval 倒计时单位
     */
    public void startCount(long millisInFuture, long countDownInterval) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                setClickable(false);
                setPressed(true);
                delyTime = millisUntilFinished / 1000;
                handler.post(() -> setText(String.format(count, delyTime)));
            }

            @Override
            public void onFinish() {
                countFinish();
            }
        };
        mCountDownTimer.start();
    }

    /**
     * 倒计时结束恢复状态
     */
    private void countFinish() {
        setClickable(true);
        setPressed(false);
        handler.post(() -> {
            setText(normal);
        });
    }

    /**
     * 检查持久化参数
     *
     * @return 是否要保持持久化计时
     */
    private boolean checkLastCountTimestamp() {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        long lastCountTimestamp = sp.getLong(SP_KEY_LAST_TIMESTAMP + getId(), -1);
        long nowTimeMillis = System.currentTimeMillis();
        long time = sp.getLong(SP_KEY_LAST_TIME + getId(), -1);
        long diff = time - ((nowTimeMillis - lastCountTimestamp) / 1000);
        if (diff < 0) {
            sp.edit().clear();
            return false;
        }
        long interval = sp.getLong(SP_KEY_INTERVAL + getId(), -1);
        startCount(diff * 1000, interval);
        return true;
    }


    /**
     * 保存倒计时 时间点 View再次可见 由于恢复倒计时状态
     *
     * @param time
     * @param interval
     */
    private void setLastCountTimestamp(long time, long interval) {
        getContext()
                .getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit()
                .putLong(SP_KEY_LAST_TIME + getId(), time)
                .putLong(SP_KEY_LAST_TIMESTAMP + getId(), System.currentTimeMillis())
                .putLong(SP_KEY_INTERVAL + getId(), interval)
                .commit();
    }

    /**
     * 设置倒计时
     *
     * @param millisInFuture    倒计时长
     * @param countDownInterval 倒计时间隔
     * @return
     */
    public CountDownTextView setIntervalUnit(long millisInFuture, long countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        return this;
    }

}
