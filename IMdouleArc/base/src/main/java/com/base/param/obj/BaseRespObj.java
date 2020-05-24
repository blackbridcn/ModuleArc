package com.base.param.obj;

/**
 * Author: yuzzha
 * Date: 3/9/2020 7:17 PM
 * Description:
 * Remark:
 */
public class BaseRespObj<E> {
    private E resultData;

    public E getResultData() {
        return resultData;
    }

    public void setResultData(E resultData) {
        this.resultData = resultData;
    }
}
