package com.base;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.annotation.Nullable;

import org.base.mvp.presenter.BasePresenter;
import org.base.mvp.view.BaseMvpActivity;
import org.bluetooth.ble.model.BleDeviceData;
import org.utils.RxToast;
import org.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yuzzha
 * Date: 1/3/2020 3:31 PM
 * Description:
 * Remark:
 */
public abstract class BaseBleMvpActivity<T extends BasePresenter> extends BaseMvpActivity<T> implements SensorEventListener {

    protected static final int OPEN_BLE_REQUEST_CODE = 0x1F;
    protected static final int LOCATION_REQUEST_CODE = 0x0F;

    protected long lastTime = 0;
    protected long sensorInterval = 3 * 1000;

    protected Vibrator mVibrator;
    protected SensorManager mSensorManager;
    protected Sensor mSensor;

    protected String SAMSUNG = "samsung";
    protected String HUAWEI = "HUAWEI";

    protected List<BleDeviceData> bleList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSensor();
    }

    //初始化加速感应器
    protected void initSensor() {
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mSensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    //请求开启蓝牙
    public void gotoBleSettingPage() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, OPEN_BLE_REQUEST_CODE);
        RxToast.showToastShort(this, UIUtils.getString(R.string.comm_view_please_open_ble));
    }

    //打开位置信息设置界面
    public void gotoLocatePage() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, LOCATION_REQUEST_CODE);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[SensorManager.DATA_X];
        if (Build.BRAND.equals(SAMSUNG) || Build.BRAND.equals(HUAWEI)) {
            x /= 2.0;
        }
        if (Math.abs(x) >= 18 && (System.currentTimeMillis() - lastTime) > sensorInterval/*|| Math.abs(y) >= 14 || Math.abs(z) >= 14*/) {
            lastTime = System.currentTimeMillis();
            unregisterSensorListener();//取消监听加速感应器，如果不取消的话会有问题
            onSensorEvent();
            startVibrato();
        }
    }

    protected abstract void onSensorEvent();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSensorListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensorListener();
    }

    //避免重复注册于解注册而影响性能
    boolean mSensorFlag;

    //注册摇一摇传感器
    public void registerSensorListener() {
        if (!mSensorFlag) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
            mSensorFlag = true;
        }
    }

    //解注册摇一摇传感器
    public void unregisterSensorListener() {
        if (mSensorFlag) {
            mSensorManager.unregisterListener(this, mSensor);
            mSensorFlag = false;
        }
    }

    //震动同时响铃
    private void startVibrato() {
        // 先暂时屏蔽响铃
        MediaPlayer player = MediaPlayer.create(this, R.raw.comm_view_find_device_voice);
        player.setLooping(false);
        player.start();
        player.setOnCompletionListener((mp) -> mp.release());
        mVibrator.vibrate(new long[]{500, 200, 500, 200}, -1);
    }

}
