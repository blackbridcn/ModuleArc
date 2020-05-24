package org.camera;

import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * File: CameraParams.java
 * Author: yuzhuzhang
 * Create: 2020-02-06 12:43
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-02-06 : Create CameraParams.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class CameraParams {

    private CameraParams() {
    }

    private static final class ParamsHolder {
        private static final CameraParams INSTANCE = new CameraParams();
    }

    public static CameraParams getInstance() {
        return ParamsHolder.INSTANCE;
    }

    private CameraSizeComparator sizeComparator = new CameraSizeComparator();

    /**
     * 适配预览视频的长宽比
     *
     * @param list        Camera硬件支持的size
     * @param previewRate 长宽比（1.333）
     * @param minWidth    宽度最小值
     * @return 长宽比最优的size
     */
    public Camera.Size getPropPreviewSize(List<Camera.Size> list, float previewRate, int minWidth) {
        Collections.sort(list, sizeComparator);
        int i = 0;
        for (Camera.Size s : list) {
            if ((s.width >= minWidth) && equalRate(s, previewRate)) {
                Log.i("TAG", "PreviewSize:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    /***
     * 适配预览照片的长宽比
     * @param list Camera硬件支持的size
     * @param previewRate 长宽比（1.333）
     * @param minWidth 宽度最小值
     * @return 长宽比最优的size
     */
    public Camera.Size getPropPictureSize(List<Camera.Size> list, float previewRate, int minWidth) {
        Collections.sort(list, sizeComparator);

        int i = 0;
        for (Camera.Size s : list) {
            if ((s.width >= minWidth) && equalRate(s, previewRate)) {
                Log.i("TAG", "PictureSize : w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    /**
     * 判断设置的长宽比与Camera支持的长宽比差值是否小于0.003
     *
     * @param s    Camera支持的长宽比
     * @param rate 设置的长宽比
     * @return true为二者差值小于0.003、false反之
     */
    public boolean equalRate(Camera.Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        if (Math.abs(r - rate) <= 0.03) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    public void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            Log.i("TAG", "previewSizes:width = " + size.width + " height = " + size.height);
        }

    }

    /**
     * 打印支持的pictureSizes
     *
     * @param params
     */
    public void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
            Log.i("TAG", "pictureSizes:width = " + size.width
                    + " height = " + size.height);
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.i("TAG", "focusModes--" + mode);
        }
    }

    /**
     * 为了解决人脸检测坐标系和实际绘制坐标系不一致问题
     * 第三个参数90，是因为前手摄像头都设置了mCamera.setDisplayOrientation(90);
     * 接下来的Matrix和canvas两个旋转我传的都是0，
     * 所以此demo只能在手机0、90、180、270四个标准角度下得到的人脸坐标是正确的。
     * 其他情况下，需要将OrientationEventListener得到的角度传过来。为了简单，我这块就么写
     *
     * @param matrix
     * @param mirror
     * @param displayOrientation
     * @param viewWidth
     * @param viewHeight
     */
    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }

    /***
     * 对相机参数List进行排序（根据宽从大到小）
     */
    public class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }
}
