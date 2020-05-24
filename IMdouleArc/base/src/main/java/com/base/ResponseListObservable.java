package com.base;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 2/28/2020 4:00 PM
 * Description:
 * Remark:
 */
public interface ResponseListObservable<T extends Object> {

    void then(boolean result, List<T> mList, String tipMsg);
}
