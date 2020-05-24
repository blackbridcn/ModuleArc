package com.ble.conn.utils.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.ble.BleConstants;

import org.bluetooth.ble.callback.BleScanCallback;
import org.bluetooth.ble.model.BleDeviceData;
import org.utils.HandlerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: yuzzha
 * Date: 2019/4/29 18:15
 * Description: ${DESCRIPTION}
 * Remark:
 */
public abstract class DefualtScanCallback extends BleScanCallback {

    private List<BleDeviceData> list;
    private AtomicBoolean hasFound = new AtomicBoolean(false);
    private String bleType;

    public DefualtScanCallback() {
        super();
        this.list = new ArrayList<BleDeviceData>();
    }

    public DefualtScanCallback(List<BleDeviceData> list) {
        super();
        this.list = list;
    }

    public DefualtScanCallback(long scanTimeou, List<BleDeviceData> list) {
        super(scanTimeou);
        this.list = list;
    }

    @Override
    public void onPreScan() {
        if (list != null)
            list.clear();
    }

    @Override
    public void onScanTimeout() {
        super.onScanTimeout();
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (checkBleValid(device))
            synchronized (this) {
                hasFound.set(false);
                for (BleDeviceData deviceData : list) {
                    if (deviceData.getDevice().equals(device)) {
                        hasFound.set(true);
                        break;
                    }
                }
                if (!hasFound.get() && checkRangeValid(rssi)) {
                    BleDeviceData deviceData = new BleDeviceData(device, rssi, scanRecord);
                    if (list != null)
                        list.add(deviceData);
                }
            }
    }

    @Override
    public void onScanFinsh() {
        if (!list.isEmpty()) {
            Collections.sort(list, (o1, o2) -> {
                return (int) ((getFeature(o1.getRssi()) - getFeature(o2.getRssi())) * 10);
            });
        }
        HandlerUtils.postTask(() -> onScanFinsh(list));
    }

    public abstract void onScanFinsh(List<BleDeviceData> list);


    /**
     * 蓝牙距离过滤
     *
     * @param rssi
     * @return
     */
    private boolean checkRangeValid(int rssi) {
        if (isRangeFliter()) {
            return getFeature(rssi) <= BleConstants.PEAKE_MIN_RSSI_VALUE;
        } else return true;
    }

    protected boolean isRangeFliter() {
        return true;
    }

    /**
     * 扫描到设备有效性检测
     *
     * @param device
     * @return 备注：是否为PK蓝牙设备 :1.ble蓝牙名字不为空；2.蓝牙名字以pkm开头
     */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private boolean checkBleValid(BluetoothDevice device) {
        bleType = getBleType();
        return !TextUtils.isEmpty(device.getName()) && device.getName().toUpperCase().startsWith(bleType);
    }


    protected String getBleType() {
        return BleConstants.PEAKE_BLEDEVICE_PRE_M_NAME;
    }

    /**
     * 计算蓝牙信号强度
     *
     * @param rssi
     * @return
     */
    private double getFeature(int rssi) {
        return (Math.abs(rssi) - 59) / (10 * 2.0);
    }

}
