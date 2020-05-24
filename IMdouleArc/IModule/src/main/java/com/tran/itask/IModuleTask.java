package com.tran.itask;

import android.content.Context;

import androidx.annotation.NonNull;

import org.imodule.task.IModule;

/**
 * Author: yuzzha
 * Date: 12/12/2019 4:42 PM
 * Description:  向所有模块分发notify的顶级 interface 接口
 * Remark:
 */
public interface IModuleTask extends IModule {

    /**
     * 执行初始化模块Task.
     * (错误会log出来.)
     *
     * @param context Context
     */
    void doInitModuelTask(Context context);

    /**
     * 登录状态发生改变后向 所有模块统一发出通知信息
     *
     * @param mContext Context
     * @param accout   账号信息
     */
    void doLoginNotifyTask(@NonNull Context mContext, @NonNull String accout);

    /**
     * 退出账号时 notify Task
     *
     * @param mContext
     */
    void doLogoutNotifyTask(@NonNull Context mContext);

    /**
     * 退出Apk时 释放内存
     *
     * @param mContext
     */
    void doExitApkNotifyTask(@NonNull Context mContext);

}
