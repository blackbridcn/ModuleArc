package com.base;

import java.util.List;

/**
 * File: ListDataObservable.java
 * Author: yuzhuzhang
 * Create: 2020-01-20 20:41
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020-01-20 : Create ListDataObservable.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public interface ListDataObservable<T extends Object> {

    void then(boolean result, List<T> mList, T mData, String tipMsg);
}
