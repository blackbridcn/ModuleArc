package com.ble.help.param;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * File: BleEvent.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 22:41
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-01-01 : Create BleEvent.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
@IntDef(value = {BleEvent.EVENT_IDLE, BleEvent.EVENT_ENCRYPT,
        BleEvent.EVENT_CHECK_LINK, BleEvent.EVENT_SET_BLEINFO, BleEvent.EVENT_ONLY_SCAN})
@Retention(RetentionPolicy.SOURCE)
public @interface BleEvent {
    // Ble设备 处于空闲状态
    int EVENT_IDLE = 1 << 31;
    //加密 --> 这里其实 发送开锁命令前的 握手命令
    int EVENT_ENCRYPT = 1 << 1;
    //链路检查
    int EVENT_CHECK_LINK = 1 << 2;
    //设置peake设备的基本信息
    int EVENT_SET_BLEINFO = 1 << 3;
    //只是Ble扫描
    int EVENT_ONLY_SCAN = 1 << 4;

}
