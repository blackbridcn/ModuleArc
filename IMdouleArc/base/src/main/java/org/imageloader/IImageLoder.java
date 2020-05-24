package org.imageloader;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * File: IImageLoder.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 11:31
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-01-01 : Create IImageLoder.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public interface IImageLoder {

    /**
     *
     * @param path
     * @param placeHolder
     * @param mImageView
     */
    void loadImageDiskCache(@NonNull String path, @DrawableRes int placeHolder, @NonNull ImageView mImageView);


    void loadCircleImage(@NonNull String path, @DrawableRes int placeHolder, @NonNull ImageView mImageView);


    void clearMemoryCache(Context mContext);

    void clearDiskCache(Context mContext);

    void clearAllCache(Context mContext);
}
