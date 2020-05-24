package com.ble.help.mvp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.bluetooth.ble.callback.BleConnCallback;
import org.bluetooth.ble.callback.BleMsgCallback;
import org.bluetooth.ble.model.BleDeviceData;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 1/4/2020 10:36 AM
 * Description:
 * Remark:
 */
public interface IBleContract {

    //蓝牙连接超时Task-->超时时间
    long BLE_CONN_TIMEOUT = 4000;

    //用户手动点击时间间隔
    long CLICK_ONPEN_INTERVAL = 3000;

    interface BasePresenter {
        /**
         * 只搜索Ble蓝牙,扫描结果回调到View
         */
        void doOnlyScanTask();

        //处理Ble蓝牙扫描前的准备工作：1.Sensor传感器监听解注册 备注:摇一摇传感器
        void doSacnBlePreTask();

        //蓝牙扫描结束 没有扫描到任何设备回调处理逻辑 备注：实现注册传感器监听等工作
        void doScanBleFinshTask();

        /**
         * 蓝牙扫描结束
         *
         * @param lists 扫描到是所有蓝牙设备List
         */
        void doScanBleFinshTask(List<BleDeviceData> lists);


        /**
         * 蓝牙连接前回调
         *
         * @param eventType 当前蓝牙连接通信事件类型
         */
        void doConnBlePreTask(int eventType);


        /**
         * 蓝牙通信结果回调
         *
         * @param isResult  成功还是失败
         * @param eventType 事件类型
         */
        void doConnBleFinshTask(boolean isResult, int eventType);

        /**
         * 蓝牙连接超时任务
         * <p>
         * 1.在单独扫描事件或者是连接BLE设备事件后执行
         * 2.在扫描到连接完整事件后执行
         */
        void doBleEventTimeOutTask(int eventType);


        /**
         * 打开蓝牙
         * 备注： 跳转到系统蓝牙设置页面
         */
        void gotoBluetoothSettingsPage();

        /**
         * 检查是定位服务是否打开
         */
        void checkLocationService();

        /**
         * 已经转在主线程中回调 Toast
         *
         * @param msg
         */
        void makeToast(String msg);

        /**
         * 当前是否为调试模式；
         * 调试模式下蓝牙会提示详细信息
         *
         * @return
         */
        boolean isTestModle();

        /**
         * @return
         */
        int getCurrtEventType();

        /**
         * Ble 蓝牙连接是否已经重置
         *
         * @return
         */
        boolean isBleReset();

        /**
         * 处理设置页面返回后，再次进行Ble扫描逻辑
         *
         * @param mActivity
         * @param requestCode
         * @param resultCode
         * @param data
         */
        void doOnActivityResultTask(Activity mActivity, int requestCode, int resultCode, Intent data);
    }

    interface IOpenDoorPresenter extends BasePresenter {
        /**
         * 扫描Ble蓝牙，并且直接连接并开锁蓝牙信号最强设备
         */
        void doScanAndOpenDoorTask();

        /**
         * 连接指定Ble设备,并且发起开锁请求；
         * 备注：只提供给开锁使用的接口函数；
         *
         * @param device BluetoothDevice
         */
        void onConnBleOpenDoor(BluetoothDevice device);

        /**
         * 用于在doSacnBleFinshTask 后判断是否自动连接蓝牙通信的情况
         * 目前只有摇一摇开锁为扫描自动连接事件
         *
         * @param bleEvenType
         * @return
         */
        boolean isScanConnEvent(int bleEvenType);

        /**
         * 得到当前卡号
         *
         * @return Card卡号
         */
        String getCardId();
    }

    interface ISettingPresenter extends BasePresenter {
        /**
         * 链路检测
         *
         * @param device BluetoothDevice
         */
        void doCheckLinkTask(BluetoothDevice device);

        /**
         * 连接指定的远程Ble设备，并且设置远程Ble基本信息；
         * 包括：名称、类型、信号强度
         *
         * @param device     BluetoothDevice
         * @param newBleName 名称
         * @param bleType    类型
         * @param rssiType   信号强度
         */
        void doSetBelInfoTask(BluetoothDevice device, @NonNull String newBleName, int bleType, int rssiType);
    }

    interface IFeePresenter extends BasePresenter {

    }

    interface BaseModel {
        /**
         * 打开蓝牙
         * 备注： 跳转到系统蓝牙设置页面
         */
        void gotoBleSettingsPage();

        /**
         * 检测手机定位是否开启
         */
        void checkLocationService();

        /**
         * @return
         */
        int getCurrtEventType();

        /**
         * 用于在doSacnBleFinshTask 后判断是否自动连接蓝牙通信的情况
         * 目前只有摇一摇开锁为扫描自动连接事件
         *
         * @param bleEvenType
         * @return
         */
        boolean isScanConnEvent(int bleEvenType);

        /**
         * Ble 蓝牙连接是否已经重置
         *
         * @return
         */
        boolean isBleReady();


        /***
         * 扫描搜索Ble设备
         *
         * @param eventType
         */
        void doScanTask(int eventType);

        /**
         * 只搜索Ble蓝牙
         */
        void doOnlyScanTask();

        /**
         * 连接远程Ble设备
         *
         * @param bleAddress   远程蓝牙Address
         * @param eventType    连接类型
         * @param connCallback BleConnCallback
         */
        void onConnBle(String bleAddress, int eventType, BleConnCallback connCallback);

        /**
         * 连接远程Ble设备
         *
         * @param device       BluetoothDevice
         * @param eventType    连接类型
         * @param connCallback BleConnCallback
         */
        void onConnBle(BluetoothDevice device, int eventType, BleConnCallback connCallback);

        /**
         * 连接远程Ble设备
         *
         * @param device       BluetoothDevice
         * @param eventType    连接类型
         * @param autoConnect  是否自动连接
         * @param connCallback BleConnCallback
         */
        void onConnBle(BluetoothDevice device, int eventType, boolean autoConnect, BleConnCallback connCallback);


        /**
         * 发送命令/信息的到Ble设备
         *
         * @param eventType   命令/信息 类型
         * @param msgCallback Ble信息回调
         */
        void sendMsgToBle(int eventType, BleMsgCallback msgCallback);

        /**
         * 蓝牙连接时开始 添加超时任务
         */
        void startConnTimeOutTask();

        /**
         * 蓝牙连接并事件完全后，解除超时任务
         */
        void stopConnTimeOutTask();

        /**
         * 分发处理 Ble应答/回执信息
         *
         * @param responeMsg
         */
        void handleBleResponeMsg(byte[] responeMsg);

        /**
         * 检测设置的Ble名称是否合理
         *
         * @param bleName
         * @return
         */
        boolean isValidName(String bleName);

        /*----------------------------------------------------------------------------------------------------*/

        /**
         * 蓝牙扫描、蓝牙连接等事件 发起之前的其他辅助逻辑处理
         * 备注：例如解注册摇一摇传感器监听，界面View改变等操作
         */
        void doConnBlePreTask();

        /**
         * 蓝牙扫描、蓝牙连接等事件结束 后其他辅助逻辑处理
         * 备注：例如注册摇一摇传感器监听，界面View改变等操作
         */
        void onConnBleNextTask(boolean isResult, int tipRes);

        /**
         * Toast 提示
         *
         * @param tipMsg
         */
        void makeToast(String tipMsg);


        /**
         * 获取CardId
         *
         * @return String
         */
        String getCardId();

        /**
         * Ble连接出现异常 后处理
         *
         * @param msgResId 异常提示Res
         */
        void doErrorEventTask(int msgResId);


        /**
         * 断开BLE 连接并且clear 回调Callback
         */
        void disConnAndClearBleGatt();

        /**
         * 重置Ble蓝牙
         */
        void resetBleState();

        /**
         * 检测手机是否支持Ble
         *
         * @return
         */
        boolean isBleEnabled();
    }

    interface IOpenDoorModel extends BaseModel {
        /**
         * 扫描Ble蓝牙，并且直接连接并开锁蓝牙信号最强设备
         */
        void doScanAndOpenDoorTask();

        /**
         * 连接指定Ble设备,并且发起开锁请求；
         * 备注：只提供给开锁使用的接口函数；
         *
         * @param device BluetoothDevice
         */
        void onConnBleOpenDoor(BluetoothDevice device);

        /**
         * 用于在doSacnBleFinshTask 后判断是否自动连接蓝牙通信的情况
         * 目前只有摇一摇开锁为扫描自动连接事件
         *
         * @param bleEvenType
         * @return
         */
        boolean isScanConnEvent(int bleEvenType);
    }

    interface ISettingModel extends BaseModel {
        /**
         * 链路检测
         *
         * @param device BluetoothDevice
         */
        void doCheckLinkTask(BluetoothDevice device);

        /**
         * 连接指定的远程Ble设备，并且设置远程Ble基本信息；
         * 包括：名称、类型、信号强度
         *
         * @param device     BluetoothDevice
         * @param newBleName 名称
         * @param bleType    类型
         * @param rssiType   信号强度
         */
        void doSetBelInfoTask(BluetoothDevice device, @NonNull String newBleName, int bleType, int rssiType);
    }

    interface IFeeModel extends BaseModel {

    }
}

