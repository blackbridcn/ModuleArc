package com.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import org.imageloader.ImageLoader;

/**
 * Author: yuzzha
 * Date: 2019-08-20 16:40
 * Description:
 * Remark:
 */
public class BaseApplication extends Application {

    private static BaseApplication mContext;
    private static int mMainThreadId;
    private Activity mTopActivity;
    protected long downloadFerence;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mMainThreadId = android.os.Process.myTid();

 /*     LeakCanary.Config config = LeakCanary.getConfig().newBuilder()
                .retainedVisibleThreshold(3)
                .computeRetainedHeapSize(false)
                .build();
        LeakCanary.setConfig(config);*/

        registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }


    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = this;
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.doClearMemoryImageCacheTask(this);
        ImageLoader.doClearDiskImageCacheTask(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static BaseApplication getContext() {
        return mContext;
    }


    /**
     * 返回主线程的pid
     *
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }


    //private Stack<Activity> mActivity = new Stack<>();

    public Activity getTopActivity() {
        return mTopActivity;
    }

    private ActivityLifecycleCallbacks lifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            mTopActivity = activity;
            // mActivity.push(activity);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            // mActivity.pop();
        }
    };


    public long getDownloadFerence() {
        return downloadFerence;
    }

    public void setDownloadFerence(long downloadFerence) {
        this.downloadFerence = downloadFerence;
    }
}
