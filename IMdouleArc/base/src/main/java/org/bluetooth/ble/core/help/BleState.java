package org.bluetooth.ble.core.help;

/**
 * Author: yuzzha
 * Date: 2019-07-15 19:11
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class BleState {

    public interface ScanState {

        //正在扫描
        int SCAN_STATE_DOING = 1 << 1;
        //扫描空闲状态
        int SCAN_STATE_IDLE = 1 << 2;
    }

    public interface ConnState {
        //连接状态--->这里是指可以通信状态
        int CONN_STATE = 1 << 1;
        //连接空闲状态
        int CONN_STATE_IDLE = 1 << 1;
        //正在连接
        int CONN_STATE_CONNECTING = 1 << 2;
        //已经连接
        int CONN_STATE_CONNECTED = 1 << 3;
    }
}
