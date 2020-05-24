package org.cache.element;

import androidx.annotation.NonNull;

import java.nio.charset.Charset;

/**
 * Author: yuzzha
 * Date: 12/27/2019 5:50 PM
 * Description:
 * Remark:
 */
public interface IEntryKey {

    String STRING_CHARSET_NAME = "UTF-8";
    Charset CHARSET = Charset.forName(STRING_CHARSET_NAME);


    void updateDiskCacheKey(@NonNull String messageDigest);


    @Override
    boolean equals(Object o);


    @Override
    int hashCode();

}
