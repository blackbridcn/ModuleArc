package org.bluetooth.ble.model;

import android.bluetooth.BluetoothDevice;

/**
 * Author: yuzzha
 * Date: 2019/4/30 9:22
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class BleDeviceData {
    private BluetoothDevice device;
    private int rssi;
    private byte[] scanRecord;
    private long mTimestampNanos;

    public BleDeviceData() {
        this.mTimestampNanos =System.currentTimeMillis();
    }

    public BleDeviceData(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
        this.mTimestampNanos =System.currentTimeMillis();
    }

    public BleDeviceData(BluetoothDevice device, int rssi, byte[] scanRecord, long mTimestampNanos) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
        this.mTimestampNanos = mTimestampNanos;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }

    public long getmTimestampNanos() {
        return mTimestampNanos;
    }

    public void setmTimestampNanos(long mTimestampNanos) {
        this.mTimestampNanos = mTimestampNanos;
    }
}
