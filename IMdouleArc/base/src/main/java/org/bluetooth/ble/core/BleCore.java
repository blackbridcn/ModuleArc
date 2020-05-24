package org.bluetooth.ble.core;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import androidx.annotation.NonNull;

import org.bluetooth.ble.callback.BleConnCallback;
import org.bluetooth.ble.callback.BleMsgCallback;
import org.bluetooth.ble.callback.BleScanCallback;
import org.bluetooth.ble.core.config.BluethConfig;

import java.util.UUID;

public interface BleCore {
    /**
     * 初始化BLE
     *
     * @param mContext   Application
     * @param connConfig BluethConfig
     */
    void initBluetooth(Context mContext, BluethConfig connConfig);

    /**
     * 设置ble扫描回调
     *
     * @param callback BleScanCallback
     */
    void setScanCallback(@NonNull BleScanCallback callback);

    /**
     * 开始扫描Ble 外围设备
     */
    void startScan();

    /**
     * 扫描指定UUID   Ble蓝牙
     *
     * @param serviceUuids
     */
    void startScan(UUID[] serviceUuids);

    /**
     * 停止Ble扫描
     */
    void stopLeScan();

    /**
     * 连接远程Ble蓝牙
     *
     * @param address         蓝牙address
     * @param bleGattCallback 连接回调
     */
    void connect(String address, BleConnCallback bleGattCallback);

    /**
     * 连接远程Ble蓝牙
     *
     * @param device          远程蓝牙BluetoothDevice
     * @param bleGattCallback 连接回调BleConnCallback
     */
    void connect(BluetoothDevice device, boolean autoConnect, BleConnCallback bleGattCallback);

    /**
     * 发送byte[] 到远程BLE服务端/从机/外围设备,并通过回调通知接收返回数据;
     *
     * @param msg         发送的数据
     * @param msgCallback
     */
    void sendMsgToBleDevice(byte[] msg, BleMsgCallback msgCallback);

    /**
     * 是否正在扫描Ble
     *
     * @return true 表示正在扫描
     */
    boolean isScan();

    /**
     * 是否可以连接蓝牙BLE
     *
     * @return true 表示已经连接
     */
    boolean isConnect();

    /**
     * 断开并且清理状态
     */
    void disConnAndClearBleGatt();

    /**
     * 本地蓝牙是否开启
     *
     * @return
     */
    boolean isEnabled();


    boolean enableBluetooth(Activity mActivity, int requestCode);


}
