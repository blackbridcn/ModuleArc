package org.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * File: CameraUtils.java
 * Author: yuzhuzhang
 * Create: 2020-02-08 22:35
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-08 : Create CameraUtils.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class CameraUtils {

    public static Intent creatCropIntent(@NotNull Uri url,@NotNull String temPath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//重要的，，，
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(url, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 4);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 750);
        intent.putExtra("outputY", 1000);
        //intent.putExtra("return-data", true);
        //先删除已经存在的图片解决
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+temPath ;
        File file = new File(path);
        if (file.exists())
            file.delete();
        Uri mTempFileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), temPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempFileUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }
}
