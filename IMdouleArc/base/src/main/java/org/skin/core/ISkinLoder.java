package org.skin.core;

import org.skin.action.ISkinUpdate;

/**
 * File: ISkinLoder.java
 * Author: yuzhuzhang
 * Create: 2020/3/20 9:38 PM
 * Description: TODO 用来添加、删除需要皮肤更新的界面以及通知界面皮肤更新
 * -----------------------------------------------------------------
 * 2020/3/20 : Create ISkinLoder.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public interface ISkinLoder {


    void attach(ISkinUpdate observer);

    void detach(ISkinUpdate observer);

    void notifySkinUpdate();
}
