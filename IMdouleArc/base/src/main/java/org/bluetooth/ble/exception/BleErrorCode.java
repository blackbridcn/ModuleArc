package org.bluetooth.ble.exception;

/**
 * Author: yuzzha
 * Date: 2019/4/25 15:29
 * Description: ${DESCRIPTION}
 * Remark:
 */
public interface BleErrorCode {

    int CODE_PARAM_ERROR = 1 << 31;
    int CONN_NO_FOUND_DEVICE_SERVICE = 1 << 1;
    int CONN_SERVICE_STATE_ERROR = 1 << 2;
    int CONN_WRITE_SERVICE_STATE_ERROR = 1 << 6;
    int CONN_READ_MSG_ERROR = 1 << 3;
    int CONN_NO_FOUND_DEVICE_UUID_SERVICE_ERROR = 1 << 4;
    int CONN_DEVICE_CHARACTERISTIC_ERROR = 1 << 5;

}
