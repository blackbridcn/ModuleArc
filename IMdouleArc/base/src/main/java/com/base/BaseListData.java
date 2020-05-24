package com.base;

import java.util.List;

/**
 * Author: yuzzha
 * Date: 12/19/2019 3:56 PM
 * Description:
 * Remark:
 */
public class BaseListData<T> {

    private List<T> list;

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return this.list;
    }

}
