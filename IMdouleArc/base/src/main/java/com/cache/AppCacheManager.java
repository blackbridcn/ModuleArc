package com.cache;

/**
 * Author: yuzzha
 * Date: 2019-12-04 16:38
 * Description:
 * Remark:
 */
public enum AppCacheManager {

    INSTANCE;
    //虚拟Id
    private String virtual_id;

    public String getVirtual_id() {
        return virtual_id;
    }

    public void setVirtual_id(String virtual_id) {
        this.virtual_id = virtual_id;
    }
}
