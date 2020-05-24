package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;

import org.bluetooth.ble.callback.BleMsgCallback;

/**
 * File: WriteCardIdMsg.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:35
 * Description: 客户端 发送 加密ID号
 * 校验卡信息命令
 * -----------------------------------------------------------------
 * 2020-01-01 : Create WriteCardIdMsg.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class WriteCardIdMsg extends BleCommand {
    private String cardNo;

    public WriteCardIdMsg(BleMsgReceiver receiver, BleMsgCallback msgCallback, String cardNo) {
        super(receiver, msgCallback);
        this.cardNo = cardNo;
    }

    @Override
    public void Action() {
        this.receiver.writeCardIdMsgAction(this.msgCallback, this.cardNo);
    }
}
