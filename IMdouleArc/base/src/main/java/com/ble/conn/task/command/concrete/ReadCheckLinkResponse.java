package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;
import com.ble.conn.utils.BleObservable;
import com.ble.conn.utils.model.BleResultData;

/**
 * File: ReadCheckLinkResponse.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:39
 * Description: 解析PEAKE的BLE设备返回的 链路检查命令的应答命令
 * -----------------------------------------------------------------
 * 2020-01-01 : Create ReadCheckLinkResponse.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ReadCheckLinkResponse extends BleCommand {

    public ReadCheckLinkResponse(BleMsgReceiver receiver, byte[] response, BleObservable<BleResultData> listener) {
        super(receiver, response, listener);
    }

    @Override
    public void Action() {
        this.receiver.handleCheckLinkResponse(this.response, this.listener);
    }
}
