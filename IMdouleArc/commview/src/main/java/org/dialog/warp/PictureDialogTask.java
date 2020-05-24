package org.dialog.warp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.commview.R;

import org.dialog.fragment.buttom.CancelListButtomDialog;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * File: PictureDialogTask.java
 * Author: yuzhuzhang
 * Create: 2020-02-09 14:31
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-09 : Create PictureDialogTask.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class PictureDialogTask {

    private PictureDialogTask() {
    }

    private static final class PictureDialogHolder {
        private static final PictureDialogTask INSTANCE = new PictureDialogTask();
    }

    public static PictureDialogTask getInstance() {
        return PictureDialogHolder.INSTANCE;
    }


    public void doTakePicTask(@NotNull FragmentActivity mActivity, int albumCode, int photoCode) {
        String[] mPicSource = mActivity.getResources().getStringArray(R.array.base_pic_source);
        CancelListButtomDialog instance = CancelListButtomDialog.newInstance();
        instance.setRecyclerViewData(Arrays.asList(mPicSource));
        instance.setOnItemClickListener((positon) -> {
            if (positon == 0) {
                takePhotoPic(mActivity, photoCode);
            } else takeAlbumPic(mActivity, albumCode);
        });
        instance.showDialog(mActivity.getSupportFragmentManager());
    }

    /**
     * 相册
     *
     * @param mActivity
     * @param albumReqCode
     */
    private void takeAlbumPic(Activity mActivity, int albumReqCode) {
        Intent intents = new Intent(Intent.ACTION_PICK, null);
        intents.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intents, albumReqCode);
    }

    // private Uri photoUri;

    /**
     * 拍照获取图片
     */
    private String takePhotoPic(Activity mActivity, int takePhongReqCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        Uri photoUri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        mActivity.startActivityForResult(intent, takePhongReqCode);
        return photoUri != null ? photoUri.toString() : null;
    }

    /**
     * 剪切图片-->保存图片
     *
     * @param uri Uri
     */
    public void startPhotoZoom(Activity activity, Uri uri, int requestCode,String picTemPath) {
       /* if (uri == null && photoUri != null) {
            uri = photoUri;
        } else*/
        if (uri == null) {
            return;//防止在裁剪图片时点击返回键导致的NullPointerException
        }
        activity.startActivityForResult(creatCropIntent(uri, picTemPath), requestCode);
    }

    Bitmap mFaceBitmap;

    public Bitmap cutPicToView(Context mContext, String filePath) {
        // showDialog("正在处理...");
        Uri mTempFileUri = FileProvider.getUriForFile(mContext, filePath,
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filePath));
        if (mTempFileUri != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 2;//采样率  375 * 500
                if (mFaceBitmap != null) mFaceBitmap.recycle();
                mFaceBitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(mTempFileUri), new Rect(-1, -1, -1, -1), options);
                //if (mFaceBitmap != null) iPresnter.showFaceImage(mFaceBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {

            }
        }
        return mFaceBitmap;
    }

    private  Intent creatCropIntent(@NotNull Uri url, @NotNull String temPath) {
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
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + temPath;
        File file = new File(path);
        if (file.exists())
            file.delete();
        Uri mTempFileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), temPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempFileUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }

}
