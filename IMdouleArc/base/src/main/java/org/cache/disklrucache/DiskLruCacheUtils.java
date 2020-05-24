package org.cache.disklrucache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.constant.BaseContstant;

import org.cache.disklrucache.source.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * Author: yuzzha
 * Date: 12/27/2019 3:00 PM
 * Description:
 * Remark:
 */
public class DiskLruCacheUtils {

    private DiskLruCache generateCache(Context mContext, String dirName, int maxSize) throws IOException {
        return DiskLruCache.open(
                getDiskCacheDir(mContext, dirName),
                getAppVersion(mContext),
                BaseContstant.CACHR_DISK_MAX_COUNT,
                maxSize);
    }

    private File getDiskCacheDir(Context mContext, String uniqueName) {
        //存到data目录
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


}
