package com.ble.conn.task.command.concrete;

import com.ble.conn.task.command.BleCommand;
import com.ble.conn.task.receiver.BleMsgReceiver;

import org.bluetooth.ble.callback.BleMsgCallback;

/**
 * File: WriteEncryptKeyMsg.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 20:33
 * Description:  客户端 发送加密密钥
 *   <p>ENCRYPT_KEY
 *  蓝牙握手 秘钥校验命令
 * -----------------------------------------------------------------
 * 2020-01-01 : Create WriteEncryptKeyMsg.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class WriteEncryptKeyMsg  extends BleCommand {


    public WriteEncryptKeyMsg(BleMsgReceiver receiver, BleMsgCallback bleMsgCallback) {
        super(receiver,bleMsgCallback);
    }


    @Override
    public void Action() {
        this.receiver.writeSecretMsgAction(this.msgCallback);
    }

}