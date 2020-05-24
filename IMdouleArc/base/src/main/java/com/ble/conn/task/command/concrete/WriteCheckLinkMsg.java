package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;

import org.bluetooth.ble.callback.BleMsgCallback;

/**
 * File: WriteCheckLinkMsg.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:34
 * Description: 发送链路检查命令
 * -----------------------------------------------------------------
 * 2020-01-01 : Create WriteCheckLinkMsg.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class WriteCheckLinkMsg   extends BleCommand {

    public WriteCheckLinkMsg(BleMsgReceiver receiver, BleMsgCallback msgCallback) {
        super(receiver, msgCallback);
    }

    @Override
    public void Action() {
        receiver.writeCheckLinkMsgAction(msgCallback);
    }
}
