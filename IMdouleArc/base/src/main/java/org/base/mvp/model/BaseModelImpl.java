package org.base.mvp.model;



import androidx.annotation.Nullable;

import java.util.Collection;

/**
 * Author: yuzzha
 * Date: 2019-05-29 19:17
 * Description: ${DESCRIPTION}
 * Remark:
 */
public class BaseModelImpl<T> implements BaseModel {


    protected <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    protected <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException(reference.getClass().getCanonicalName() + " Not null");
        }
        return reference;
    }


    protected boolean checkListNotNull(Collection<?> collection) {
        if (collection == null) {
            return false;
        } else if (collection.size() > 0) {
            return true;
        } else return false;
    }

    //用户手动点击开锁按钮时间
     long LAST_CLICK_TIME;
    //用户手动点击时间间隔
    long CLICK_ONPEN_INTERVAL = 3000;

    protected boolean isNotRepeatClick() {
        if ((System.currentTimeMillis() - LAST_CLICK_TIME) > CLICK_ONPEN_INTERVAL) {
            LAST_CLICK_TIME = System.currentTimeMillis();
            return true;
        } else {
            LAST_CLICK_TIME = System.currentTimeMillis();
            // makeToast(UIUtils.getString(R.string.base_ble_operate_more));
            return false;
        }
    }


}
