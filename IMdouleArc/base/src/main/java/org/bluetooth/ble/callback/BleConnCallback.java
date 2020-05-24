package org.bluetooth.ble.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Author: yuzzha
 * Date: 2019/4/25 14:41
 * Description: 远程Ble蓝牙连接状态回调
 * Remark:
 */
public abstract class BleConnCallback extends BluetoothGattCallback {

    /**
     * Ble 连接成功后回调
     *
     * @param gatt           BluetoothGatt
     * @param characteristic BluetoothGattCharacteristic
     */
    public void onConnSuccess(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
    }

    /**
     * Ble  正在连接
     */
    public void onConning() {
    }

    /**
     * Ble 连接失败
     *
     * @param code BleErrorCode 失败Code
     * @param msg  连接失败ResId Msg
     *             备注：失败回调后，会自动断开连接并且清空所用的回调
     */
    public void onConnFailure(int code, int msg) {
    }

    /**
     * Ble  断开连接
     * 备注：断开连接回调后，会自动断开连接并且清空所用的回调
     */
    public void disConnDevice() {
    }

    /**
     * Ble连接成功 , 并且可以开始向Ble设备发消息
     *
     * @param gatt BluetoothGatt
     */
    public abstract void startSendMsg(BluetoothGatt gatt);

}