package org.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


/**
 * <p> 图片加载工具类</p>
 *
 * @name ImageUtils
 */
public class ImageLoader {

    /**
     * 默认加载
     */
    public static void loadImageView(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).into(mImageView);
    }

    public static void loadImageView(Uri path, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).into(mImageView);
    }

    public static void loadImageDiskCache(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext())
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(mImageView);
    }

    public static void loadImageDiskCache(@NonNull String path, @DrawableRes int placeHolder, ImageView mImageView) {
        if (mImageView != null) {
            Glide.with(mImageView.getContext())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(placeHolder)
                    .placeholder(placeHolder)
                    .into(mImageView);
        }
    }

    public static void loadImageCache(String path, int errorRes, int placeHolder, ImageView mImageView) {
        Glide.with(mImageView.getContext())
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(errorRes)
                .placeholder(placeHolder)
                .into(mImageView);
    }

    public static void loadImageDiskCache(Context mContext, String path, Drawable placeHolder, ImageView mImageView) {
        if (mContext != null) {
            Glide.with(mContext)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(placeHolder)
                    .placeholder(placeHolder)
                    .into(mImageView);
        }
    }

    /**
     * 使用Disk缓存，但是跳过内存缓存
     * 适合加载banner/头像/ 等
     *
     * @param mContext    Context
     * @param path        url
     * @param placeHolder 占位符
     * @param mImageView  ImageView
     */
    public static void loadImageDiskCache(Context mContext, String path, int placeHolder, ImageView mImageView) {
        if (mContext != null && mImageView != null) {
            Glide.with(mContext)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
                    .error(placeHolder)
                    .placeholder(placeHolder)
                    .into(mImageView);
        }
    }

    public static void loadDrawableImage(@DrawableRes int resId, @DrawableRes int placeHolder, @NonNull ImageView mImageView) {
        if (mImageView != null) {
            Glide.with(mImageView.getContext()).load(resId)
                    .error(placeHolder)
                    .placeholder(placeHolder)
                    .into(mImageView);


        }
    }


    public static void loadDrawableImage(@DrawableRes int resId, @NonNull ImageView mImageView) {
        if (mImageView != null) {
            Glide.with(mImageView.getContext()).load(resId)
                    .into(mImageView);


        }
    }

    public static void loadCircleImage(@NonNull String path, @DrawableRes int placeHolder, @NonNull ImageView mImageView) {
        if (mImageView != null) {
            Glide.with(mImageView.getContext())
                    .load(path)
                    .error(placeHolder)
                    .placeholder(placeHolder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageView);
        }
    }


    /**
     * 设置加载中以及加载失败图片
     */
    public static void loadImageWithLoading(@NonNull String path, @NonNull ImageView mImageView, @DrawableRes int lodingImage, @DrawableRes int errorRes) {
        Glide.with(mImageView.getContext()).load(path).placeholder(lodingImage).
                error(errorRes).into(mImageView);
    }

    /**
     * 设置加载动画
     * api也提供了几个常用的动画：比如crossFade()
     */
    public static void loadImageViewAnim(@NonNull String path, @AnimatorRes int anim, ImageView mImageView) {
        Glide.with(mImageView.getContext()).load(path).transition(GenericTransitionOptions.<Drawable>with(anim)).into(mImageView);
    }


    /**
     * 加载为bitmap
     *
     * @param path     图片地址
     * @param listener 回调
     */
    public static void loadBitmap(Context mContext, String path, final onLoadBitmap listener) {
        Glide.with(mContext).asBitmap().load(path).into(
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        listener.onReady(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        listener.onFailed();
                    }
                });

    }

    /**
     * 显示加载进度
     *
     * @param path       图片地址
     * @param mImageView 图片控件
     * @param loadView   加载view
     */
    public static void loadImageWithProgress(String path, final ImageView mImageView, final View loadView, int errorRes) {
        Glide.with(mImageView.getContext()).load(path).error(errorRes).into(new DrawableImageViewTarget(mImageView) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                if (loadView.getVisibility() == View.VISIBLE)
                    loadView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                loadView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if (loadView.getVisibility() == View.VISIBLE)
                    loadView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 清除view上的图片
     *
     * @param view 视图
     */
    public static void clearImageView(View view) {
        Glide.with(view.getContext()).clear(view);
    }

    /**
     * 清理磁盘缓存需要在子线程中执行
     */
    public static void doClearDiskImageCacheTask(Context mContext) {
        Glide.get(mContext).clearDiskCache();
    }

    /**
     * 清理内存缓存可以在UI主线程中进行
     */
    public static void doClearMemoryImageCacheTask(Context mContext) {
        Glide.get(mContext).clearMemory();
    }

    /**
     * 加载bitmap回调
     */
    public interface onLoadBitmap {
        void onReady(Bitmap resource);

        void onFailed();
    }

}