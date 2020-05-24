package org.cache;

import androidx.annotation.NonNull;

import org.cache.element.IElement;
import org.cache.element.IEntryKey;

import java.util.concurrent.Executor;

/**
 * Author: yuzzha
 * Date: 12/27/2019 5:45 PM
 * Description:
 * Remark:
 */
public interface IEngine {

    IEngine loadElement(@NonNull String key, boolean isMemoryCacheable, Executor callbackExecutor);


    IElement loadFromMemory(@NonNull IEntryKey key, boolean isMemoryCacheable, long startTime);


}
