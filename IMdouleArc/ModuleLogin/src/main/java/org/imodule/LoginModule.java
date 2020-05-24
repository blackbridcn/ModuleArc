package org.imodule;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.tran.ann.ModuleName;
import com.tran.imodule.ILoginModule;

import org.imodule.help.annotation.ModuleImpl;
import org.imodule.task.IModule;

/**
 * File: LoginModule.java
 * Author: yuzhuzhang
 * Create: 2020/5/24 5:44 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/5/24 : Create LoginModule.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
@AutoService(IModule.class)
@ModuleImpl(value = ModuleName.MODUEL_LOGIN)
public class LoginModule  implements ILoginModule {

    @Override
    public void doInitModuelTask(Context context) {

    }

    @Override
    public void doLoginNotifyTask(@NonNull Context mContext, @NonNull String accout) {

    }

    @Override
    public void doLogoutNotifyTask(@NonNull Context mContext) {

    }

    @Override
    public void doExitApkNotifyTask(@NonNull Context mContext) {

    }
}
