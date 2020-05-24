package org.bluetooth.ble.core;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.bluetooth.ble.callback.BleConnCallback;
import org.bluetooth.ble.callback.BleMsgCallback;
import org.bluetooth.ble.callback.BleScanCallback;
import org.bluetooth.ble.core.config.BluethConfig;
import org.bluetooth.ble.core.help.BleHelp;
import org.bluetooth.ble.core.help.BleState;
import org.bluetooth.ble.exception.BleErrorCode;
import org.bluetooth.ble.exception.EnumErrorCode;
import org.bluetooth.ble.utils.ObjectHelp;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BleCoreImp implements BleCore, BluetoothAdapter.LeScanCallback {
    private Context mContext;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BleConnCallback bleGattCallback;
    private BluethConfig connConfig;
    private BleScanCallback scanCallback;

    /*----------------------------------------------------------------------------------*/
    //连接状态--->这里是指可以通信状态
    private int CONN_STATE = BleState.ConnState.CONN_STATE_IDLE;
    //蓝牙扫描状态
    private int SCAN_STATE = BleState.ScanState.SCAN_STATE_IDLE;
    /*----------------------------------------------------------------------------------*/
    private byte[][] msgPackage;
    private int count;
    private BleMsgCallback msgCallback;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic characteristic;

    /*----------------------------------------------------------------------------------*/
    private BleCoreImp() {
    }

    private final static class BleManagerHolder {
        private static final BleCoreImp Instance = new BleCoreImp();
    }

    public static BleCoreImp getInstance() {
        return BleManagerHolder.Instance;
    }

    @Override
    public void initBluetooth(@NonNull Context mContexts, @NonNull BluethConfig connConfig) {
        this.mContext = mContexts.getApplicationContext();
        this.connConfig = connConfig;
        bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void setScanCallback(@NonNull BleScanCallback callbacks) {
        if (callbacks != null) {
            long scanTimeou = callbacks.getScanTimeou();
            if (scanTimeou != -1) {
                this.connConfig.setScanTimeout(scanTimeou);
            }
        }
        this.scanCallback = callbacks;
    }

    @Override
    public void startScan() {
        startScan(new UUID[]{UUID.fromString(connConfig.getServiceUUID())}/*null*/);
    }

    @Override
    public void startScan(UUID[] serviceUuids) {
        this.mBluetoothAdapter.stopLeScan(this);
        SCAN_STATE = BleState.ScanState.SCAN_STATE_DOING;
        if (this.scanCallback != null)
            this.scanCallback.onPreScan();
        if (serviceUuids != null) {
            this.mBluetoothAdapter.startLeScan(serviceUuids, this);
        } else
            this.mBluetoothAdapter.startLeScan(this);
        StopScanTask(this.connConfig.getScanTimeout());
    }

    @Override
    public void stopLeScan() {
        this.mBluetoothAdapter.stopLeScan(this);
        if (this.scanCallback != null) {
            this.scanCallback.onStopScan();
            this.scanCallback.onScanFinsh();
        }
        SCAN_STATE = BleState.ScanState.SCAN_STATE_IDLE;
    }

    //BluetoothAdapter.LeScanCallback
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (this.scanCallback != null) {
            this.scanCallback.onLeScan(device, rssi, scanRecord);
        }
    }

    /**
     * 蓝牙扫描超时 操作--> 停止扫描
     *
     * @param scanTimeout 超时时间
     *                    备注：这个是在子线程中操作的
     */
    private void StopScanTask(@NonNull long scanTimeout) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(BleCoreImp.this);
                SCAN_STATE = BleState.ScanState.SCAN_STATE_IDLE;
                if (scanCallback != null) {
                    scanCallback.onScanTimeout();
                    scanCallback.onScanFinsh();
                    scanCallback = null;
                }
            }
        }, scanTimeout);
    }

    @Override
    public void connect(@NonNull String address, BleConnCallback bleGattCallback) {
        BluetoothDevice device = null;
        if (mBluetoothAdapter != null) {
            device = mBluetoothAdapter.getRemoteDevice(ObjectHelp.checkNotEmpty(address));
            connect(device, false, bleGattCallback);
        } else {
            throw new RuntimeException(" connect remout device shuold be inint initBluetooth");
        }
    }

    @Override
    public void connect(@NonNull BluetoothDevice device, boolean autoConnect, BleConnCallback bleGattCallback) {
        doPreConnResetParams();
        this.bleGattCallback = bleGattCallback;
        if (device != null) {
            CONN_STATE = BleState.ConnState.CONN_STATE_CONNECTING;
            device.connectGatt(mContext, autoConnect, connectGattCallback);
        } else if (bleGattCallback != null) {
            bleGattCallback.onConnFailure(BleErrorCode.CONN_NO_FOUND_DEVICE_SERVICE, EnumErrorCode.getDesc(BleErrorCode.CONN_NO_FOUND_DEVICE_SERVICE));
            disConnAndClearBleGatt();
            CONN_STATE = BleState.ConnState.CONN_STATE_IDLE;
        }
    }


    private void doPreConnResetParams() {
        this.bleGattCallback = null;
        msgPackage = null;
        count = 0;
        if (bluetoothGatt != null) {
            bluetoothGatt = null;
        }
        if (characteristic != null) {
            characteristic = null;
        }
    }

    /**
     * 发送byte[] 到远程BLE服务端/从机/外围设备,并通过回调通知接收返回数据;
     *
     * @param msg         发送的数据
     * @param msgCallback
     */
    @Override
    public void sendMsgToBleDevice(byte[] msg, BleMsgCallback msgCallback) {
        this.msgCallback = msgCallback;
        this.msgPackage = BleHelp.getInstance().splitMsgPackage(msg);
        if (count != 0) {
        //    Log.e("TAG", "sendMsgToBleDevice: ------------------->  上一次数据未完全发送：count：  " + count);
        }
        count = 0;
        characteristic.setValue(msgPackage[count]);
        bluetoothGatt.writeCharacteristic(characteristic);
    }

    //远程蓝牙连接回调
    private BluetoothGattCallback connectGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                return;
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                if (bleGattCallback != null) {
                    CONN_STATE = BleState.ConnState.CONN_STATE_CONNECTING;
                    bleGattCallback.onConning();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean services = gatt.discoverServices();
                    if (!services) {
                        bleGattCallback.onConnFailure(BleErrorCode.CONN_NO_FOUND_DEVICE_SERVICE, EnumErrorCode.getDesc(BleErrorCode.CONN_NO_FOUND_DEVICE_SERVICE));
                        onDisAndClear(gatt, characteristic);
                    }
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if (bleGattCallback != null) {
                    bleGattCallback.disConnDevice();
                    onDisAndClear(gatt, characteristic);
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            processServiceDiscovered(gatt, status, connConfig.getServiceUUID(), connConfig.getCharacterUUID(), bleGattCallback);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            processCharacteristicWrite(gatt, characteristic, status, bleGattCallback);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            processCharacteristicChanged(gatt, characteristic);
        }
    };

    /**
     * 连接成功并且处理发现服务 ,通知用户，可以开始发送数据
     *
     * @param gatt            BluetoothGatt
     * @param status          int
     * @param serviceUUID     String
     * @param characterUUID   String
     * @param bleGattCallback BleConnCallback
     */
    public void processServiceDiscovered(BluetoothGatt gatt, int status, String serviceUUID, String characterUUID, BleConnCallback bleGattCallback) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            bleGattCallback.onConnFailure(BleErrorCode.CONN_SERVICE_STATE_ERROR, EnumErrorCode.getDesc(BleErrorCode.CONN_SERVICE_STATE_ERROR));
            onDisAndClear(gatt, characteristic);
            return;
        }
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUUID));
        if (service == null) {
            bleGattCallback.onConnFailure(BleErrorCode.CONN_NO_FOUND_DEVICE_UUID_SERVICE_ERROR, EnumErrorCode.getDesc(BleErrorCode.CONN_NO_FOUND_DEVICE_UUID_SERVICE_ERROR));
            onDisAndClear(gatt, characteristic);
            return;
        }
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(characterUUID));
        if (characteristic == null) {
            bleGattCallback.onConnFailure(BleErrorCode.CONN_DEVICE_CHARACTERISTIC_ERROR, EnumErrorCode.getDesc(BleErrorCode.CONN_DEVICE_CHARACTERISTIC_ERROR));
            onDisAndClear(gatt, null);
            return;
        }
        //设置通知,实时监听Characteristic变化 这个很重要,如果没有设置Notification就收不到服务端/从机/外围设备返回的数据回调
        gatt.setCharacteristicNotification(characteristic, true);
        bleGattCallback.onConnSuccess(gatt, characteristic);
        CONN_STATE = BleState.ConnState.CONN_STATE_CONNECTED;
        this.characteristic = characteristic;
        this.bluetoothGatt = gatt;
        bleGattCallback.startSendMsg(gatt);
    }


    /**
     * 数据发送成功后的,Ble回调函数
     * <p>
     * 检测数据包是否全部发送完成，如果没有继续发送，否则置空；
     */
    public void processCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status, BleConnCallback bleGattCallback) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            bleGattCallback.onConnFailure(BleErrorCode.CONN_WRITE_SERVICE_STATE_ERROR, EnumErrorCode.getDesc(BleErrorCode.CONN_WRITE_SERVICE_STATE_ERROR));
            onDisAndClear(gatt, characteristic);
            return;
        }
        count++;
        if (count < this.msgPackage.length) {
            characteristic.setValue(this.msgPackage[count]);
            gatt.writeCharacteristic(characteristic);
        } else {
            this.msgPackage = null;
        }
    }

    /**
     * 处理返回消息数据
     *
     * @param gatt
     * @param characteristic
     */
    public void processCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (this.msgCallback != null) {
            this.msgCallback.receiverRemoteBleMsg(gatt, characteristic);
        }
    }

    @Override
    public boolean isScan() {
        return SCAN_STATE == BleState.ScanState.SCAN_STATE_DOING;
    }

    @Override
    public boolean isConnect() {
        return (CONN_STATE & (BleState.ConnState.CONN_STATE_CONNECTING | BleState.ConnState.CONN_STATE_CONNECTED)) != 0;
    }

    @Override
    public void disConnAndClearBleGatt() {
        onDisAndClear(bluetoothGatt, characteristic);
    }

    private void onDisAndClear(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (gatt != null) {
            gatt.disconnect();
        }
        if (gatt != null) {
            BleHelp.getInstance().refreshDeviceCache(gatt);
        }
        if (gatt != null) {
            gatt.close();
        }
        if (gatt != null) {
            gatt = null;
        }
        if (characteristic != null) {
            characteristic = null;
        }
        CONN_STATE = BleState.ConnState.CONN_STATE_IDLE;
        clearCallback();
    }

    private void clearCallback() {
        //ble scan
        scanCallback = null;
        //ble conn
        bleGattCallback = null;
        msgCallback = null;
        msgPackage = null;
        count = 0;
    }

    @Override
    public boolean isEnabled() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        }
        return false;
    }

    @Override
    public boolean enableBluetooth(Activity mActivity, int requestCode) {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableBtIntent, requestCode);
        }
        return false;
    }
}
