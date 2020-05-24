package org.bluetooth.ble.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public interface BleMsgCallback {

    void receiverRemoteBleMsg(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);

    void sendMsgError(String errorMsg, int errorRes);

}
