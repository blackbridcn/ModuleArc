package org.bluetooth.ble.callback;

import android.bluetooth.BluetoothDevice;

public abstract class BleScanCallback {

    protected long scanTimeou = -1;

    public BleScanCallback() {
        this.scanTimeou = -1;
    }

    public BleScanCallback(long scanTimeout) {
        this.scanTimeou = scanTimeout;
    }

    public long getScanTimeou() {
        return scanTimeou;
    }

    public void onPreScan() {}

    /**
     * Callback reporting an LE device found during a device scan initiated
     *
     * @param device     Identifies the remote device
     * @param rssi       The RSSI value for the remote device as reported by the Bluetooth hardware. 0
     *                   if no RSSI value is available.
     * @param scanRecord The content of the advertisement record offered by the remote device.
     */
    public abstract void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);

    /**
     * 超时回调
     */
    public void onScanTimeout() {}

    public void onStopScan() {}

    /**
     * 调用Stop中断
     */
    public abstract void onScanFinsh();

}
