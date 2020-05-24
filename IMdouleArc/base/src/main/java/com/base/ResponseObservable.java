package com.base;

/**
 * Author: yuzzha
 * Date: 1/22/2020 11:58 AM
 * Description:
 * Remark:
 */
public interface ResponseObservable<T extends Object> {
    //result false 是表示Http返回数据出错，需要toast ，true 时表示：http成功但是数据为空，不进行toast
    void then(boolean result, T t, String tipMsg);
}
