package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;
import com.ble.conn.utils.BleObservable;
import com.ble.conn.utils.model.BleResultData;

/**
 * File: ReadEncryptKeyResponse.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:37
 * Description: 解析PEAKE的BLE设备返回的 握手命令的应答命令
 * -----------------------------------------------------------------
 * 2020-01-01 : Create ReadEncryptKeyResponse.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ReadEncryptKeyResponse extends BleCommand {

    public ReadEncryptKeyResponse(BleMsgReceiver receiver, byte[] response, BleObservable<BleResultData> listener) {
        super(receiver, response, listener);
    }

    @Override
    public void Action() {
        this.receiver.handleSecretResponse(this.response, this.listener);
    }
}
