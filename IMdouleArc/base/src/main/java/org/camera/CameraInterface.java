package org.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.List;

public class CameraInterface {

    private String TAG = CameraInterface.class.getSimpleName();
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;
    private int pWidth;
    private int pHeight;

    private int CameraId;

    public interface CamOpenOverCallback {
         void cameraHasOpened();
    }

    private CameraInterface() {

    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**
     * 打开相机, 默认使用前置相机，如果没有前置打开后置相机
     *
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public void doOpenCamera(final CamOpenOverCallback callback) {
        if (mCamera == null) {
            try {
                CameraId = isFrontCamera(true);
                if (CameraId == -1) {
                    CameraId = isFrontCamera(false);
                }
                if (CameraId == -1) {
                } else {
                    mCamera = Camera.open(CameraId);

                    /* mCamera.setDisplayOrientation(90);*/
                    mCamera.setDisplayOrientation(0);
                }
                if (callback != null) {
                    callback.cameraHasOpened();
                }
            } catch (Exception e) {
                doStopCamera();
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        doOpenCamera(callback);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }).start();
            }
        } else {
            doStopCamera();
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    doOpenCamera(callback);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 寻找指定相机
     *
     * @param isfront true 为前置相机  false 表示后置相机
     * @return 1、表示前置相机 ；0、表示后置相机； -1、表示没有找到相机
     */
    private int isFrontCamera(boolean isfront) {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (isfront) {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                    return camIdx;
                }
            } else {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                    return camIdx;
                }
            }
        }
        return -1;
    }

    /**
     * 切换相机
     *
     * @param callback
     */
    public void switchCamera(final SurfaceTexture surface, final float previewRate, final Camera.PreviewCallback callback) {
        if (CameraId != -1) {
            doStopCamera();
            if (CameraId == isFrontCamera(true)) {
                CameraId = isFrontCamera(false);
            } else {
                CameraId = isFrontCamera(true);
            }
            if (CameraId != -1) {
                mCamera = Camera.open(CameraId);
                mCamera.setDisplayOrientation(90);
                if (callback != null) {
                    doStartPreview(surface, previewRate, callback);
                }
            } else {
            }
        } else {
            doOpenCamera(new CamOpenOverCallback() {
                @Override
                public void cameraHasOpened() {
                    doStartPreview(surface, previewRate, callback);
                }
            });
        }
    }

    /**
     * 切换相机
     *
     * @param callback
     */
    public void switchCamera(final SurfaceHolder surface, final float previewRate, final Camera.PreviewCallback callback) {
        if (CameraId != -1) {
            if (CameraId == isFrontCamera(true)) {
                CameraId = isFrontCamera(false);
            } else {
                CameraId = isFrontCamera(true);
            }
            if (CameraId != -1) {
                doStopCamera();
                mCamera = Camera.open(CameraId);
                /*mCamera.setDisplayOrientation(90);*/
                mCamera.setDisplayOrientation(0);
                if (callback != null) {
                    doStartPreview(surface, previewRate, callback);
                }
            } else {

            }
        } else {
            doOpenCamera(new CamOpenOverCallback() {
                @Override
                public void cameraHasOpened() {
                    doStartPreview(surface, previewRate, callback);
                }
            });
        }
    }

    /**
     * 开始在Surfaceview预览视频
     *
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate, Camera.PreviewCallback callback) {
        if (previewRate == 0) {
            previewRate = 1.3333f;
        }
        if (isPreviewing) {
            //mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.setOneShotPreviewCallback(callback);
                //mCamera.setPreviewCallback(callback);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCamera(previewRate);
        }

    }

    /**
     * ʹ开始在TextureView中预览Camera
     *
     * @param surface
     * @param previewRate
     */
    public void doStartPreview(SurfaceTexture surface, float previewRate, Camera.PreviewCallback callback) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surface);
                mCamera.setPreviewCallback(callback);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }
    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    private void initCamera(float previewRate) {
        if (mCamera != null) {
            try {
                mParams = mCamera.getParameters();

                Camera.Size previewSize = CameraParams.getInstance().getPropPreviewSize(mParams.getSupportedPreviewSizes(), previewRate, 640);
                Log.d("log", String.valueOf(previewSize.width) + "-" + previewSize.height);
                pWidth = previewSize.width;
                pHeight = previewSize.height;
                mParams.setPreviewSize(pWidth, pHeight);

                List<String> focusModes = mParams.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                mCamera.setParameters(mParams);
                mCamera.startPreview();
                isPreviewing = true;
                mPreviwRate = previewRate;

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     * 拍照
     */
    public void doTakePicture() {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    /**
     * 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
     */
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };
    /**
     * 拍摄的未压缩原数据的回调,可以为null
     */
    Camera.PictureCallback mRawCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub

        }
    };
    /**
     * 对jpeg图像数据的回调,最重要的一个回调
     */
    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();
                isPreviewing = false;
            }
            //保存图片到sdcard
            if (null != b) {
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
                Bitmap rotaBitmap = getRotateBitmap(b, 90.0f);
                //FileUtil.saveBitmap(rotaBitmap);
            }
            //再次进入预览
            mCamera.startPreview();
            isPreviewing = true;
        }
    };

    public int getPreviewHeight() {
        return this.pHeight;
    }

    public int getPreviewWidth() {
        return this.pWidth;
    }

    /**
     * 显示当前相机运行的信息
     */
    public void showCameraInfo(Context context) {
        String info = "";
        if (isUsingFrontCamera()) {
            info += "Use Front Camera!\n";
        } else {
            info += "Use Back Camera!\n";
        }
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    /**
     * 是否使用前置摄像头
     *
     * @return
     */
    public boolean isUsingFrontCamera() {
        return CameraId >= 0 && CameraId == isFrontCamera(true);
    }

    /**
     * 获取当前相机ID  0 代表后置相机 1 代表前置相机
     *
     * @return CameraId
     */
    public int getCameraId() {
        return CameraId;
    }

    /**
     * 获取Camera.Parameters
     *
     * @return
     */
    public Camera.Parameters getCameraParams() {
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            return mParams;
        }
        return null;
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    public Camera getCameraDevice() {
        return mCamera;
    }

    /**
     * Bitmap旋转
     *
     * @param b
     * @param rotateDegree
     * @return bitmap
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }

}
