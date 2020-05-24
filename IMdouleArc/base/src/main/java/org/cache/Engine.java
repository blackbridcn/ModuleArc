package org.cache;

import android.util.Log;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LogTime;

import org.cache.element.IElement;
import org.cache.element.IEntryKey;

import java.util.concurrent.Executor;

/**
 * Author: yuzzha
 * Date: 12/27/2019 6:09 PM
 * Description:
 * Remark:
 */
public class Engine implements IEngine {

    private static final String TAG = "CacheEngine";
    private static final boolean VERBOSE_IS_LOGGABLE = Log.isLoggable(TAG, Log.VERBOSE);

    @Override
    public IEngine loadElement(String key, boolean isMemoryCacheable, Executor callbackExecutor) {
        long startTime = VERBOSE_IS_LOGGABLE ? LogTime.getLogTime() : 0;
        IEntryKey cacheKey = buildKey(key);
        IElement result;
        synchronized (this) {
            result = loadFromMemory(cacheKey, isMemoryCacheable, startTime);
            if (result == null) {
            }
        }

        return null;
    }

    @Override
    public IElement loadFromMemory(IEntryKey key, boolean isMemoryCacheable, long startTime) {

        return null;
    }


    private static void logWithTimeAndKey(String log, long startTime, Key key) {
        Log.v(TAG, log + " in " + LogTime.getElapsedMillis(startTime) + "ms, key: " + key);
    }

    private IEntryKey buildKey(String key) {
        return null;
    }
}
