package com.ble.conn.utils;

/**
 * File: BleObservable.java
 * Author: yuzhuzhang
 * Create: 2020-01-01 21:14
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-01-01 : Create BleObservable.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public interface BleObservable <T extends Object>{

    void then(T t);
}
