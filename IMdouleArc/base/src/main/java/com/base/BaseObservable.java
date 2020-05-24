package com.base;

/**
 * Author: yuzzha
 * Date: 12/23/2019 7:58 PM
 * Description:
 * Remark:
 */
public interface BaseObservable<T extends Object> {

    void then(T t, String tipMsg);

}
