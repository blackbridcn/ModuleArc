package org.bluetooth.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import org.bluetooth.ble.callback.BleConnCallback;
import org.bluetooth.ble.callback.BleMsgCallback;
import org.bluetooth.ble.callback.BleScanCallback;
import org.bluetooth.ble.core.BleCore;
import org.bluetooth.ble.core.BleCoreImp;
import org.bluetooth.ble.core.config.BluethConfig;
import org.bluetooth.ble.utils.ObjectHelp;
import org.bluetooth.ble.utils.StringUtils;

import java.util.UUID;

public class BleClent {

    private static String TAG = BleClent.class.getSimpleName();

    private Context mContext;

    private BleCore mBlutoothClent;
    private static BleClent instance;

    public static BleClent getInstance() {
        return instance;
    }

    public static void initBlutooth(Context mContext, BluethConfig connConfig) {
        if (instance == null) {
            synchronized (BleClent.class) {
                if (instance == null)
                    instance = new BleClent(mContext, connConfig);
            }
        }
    }

    private BleClent(Context context, BluethConfig connConfig) {
        if (this.mContext != null) {
            throw new RuntimeException("U Can't repeat initBlueooth");
        }
        Context mContexts = ObjectHelp.checkNotNull(context, "Context must not be null ");
        this.mContext = mContexts.getApplicationContext();
        if (checkBluetoothSupport(this.mContext)) {
            mBlutoothClent = BleCoreImp.getInstance();
            mBlutoothClent.initBluetooth(mContext, connConfig);
        }
    }

    public boolean isSupportBle(Context mContext) {
        return checkBluetoothSupport(mContext);
    }

    private boolean checkBluetoothSupport(Context mContext) {
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.w(TAG, "Bluetooth LE is not supported");
            return false;
        }
        return true;
    }

    /**
     * BLE是否可以立即连接
     *
     * @return Ready Statue
     */
    public boolean isReady() {
        return !(mBlutoothClent.isConnect() | mBlutoothClent.isScan());
    }

    public void sendMsgToBleDevice(String hexStr, BleMsgCallback msgCallback) {
        if (mBlutoothClent != null && mBlutoothClent.isConnect()) {
            sendMsgToBleDevice(StringUtils.HexString2Bytes(hexStr), msgCallback);
        }
    }

    public void sendMsgToBleDevice(byte[] msg, BleMsgCallback msgCallback) {
        if (mBlutoothClent != null) {
            mBlutoothClent.sendMsgToBleDevice(msg, msgCallback);
        }
    }

    public void startScan(BleScanCallback mBleScanCallback) {
        if (mBlutoothClent != null) {
            mBlutoothClent.setScanCallback(mBleScanCallback);
            mBlutoothClent.startScan();
        }
    }

    public void startScan(UUID[] serviceUuids, BleScanCallback mBleScanCallback) {
        if (mBlutoothClent != null) {
            mBlutoothClent.setScanCallback(mBleScanCallback);
            mBlutoothClent.startScan(serviceUuids);
        }
    }

    public void stopLeScan() {
        if (mBlutoothClent != null) {
            mBlutoothClent.stopLeScan();
        }
    }

    public void connect(String address, BleConnCallback bleGattCallback) {
        if (mBlutoothClent != null) {
            mBlutoothClent.connect(address, bleGattCallback);
        }
    }

    public void connect(BluetoothDevice device, BleConnCallback bleGattCallback) {
        if (mBlutoothClent != null) {
            mBlutoothClent.connect(device, false, bleGattCallback);
        }
    }

    public void connect(BluetoothDevice device, boolean autoConnect, BleConnCallback bleGattCallback) {
        if (mBlutoothClent != null) {
            mBlutoothClent.connect(device, autoConnect, bleGattCallback);
        }
    }

    public boolean isScanning() {
        return mBlutoothClent.isScan();
    }

    public boolean isConnect() {
        return mBlutoothClent.isConnect();
    }

    public void disConnAndClearBleGatt() {
        mBlutoothClent.disConnAndClearBleGatt();
    }

    public void stopScan() {
        mBlutoothClent.stopLeScan();
    }

    public void doResetBleTask() {
        if (isScanning()) {
            stopLeScan();
        }
        if (isConnect()) {
            disConnAndClearBleGatt();
        }
    }

    public boolean isEnabled() {
        if (mBlutoothClent != null) {
            return mBlutoothClent.isEnabled();
        }
        return false;
    }
}
