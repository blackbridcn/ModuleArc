package com.tran.itask.handle;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tran.itask.IModuleTask;

import org.imodule.core.impl.ModuleHandle;
import org.imodule.task.IModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 * Author: yuzzha
 * Date: 12/17/2019 12:06 PM
 * Description:这里其实是 IModuleTask 的一个静态代理；
 * 这里是向各个Module 模块分发全局apk信息改变函数的一个代理；
 * Remark:
 */
public class HandleModuleTask implements InvocationHandler, IModuleTask {

    private HandleModuleTask() {
    }

    private static class HandleModuleTaskHolder {
        private static final HandleModuleTask Instance = new HandleModuleTask();
    }

    public static HandleModuleTask getInstance() {
        return HandleModuleTaskHolder.Instance;
    }

    @Override
    public void doInitModuelTask(Context mContext) {
        Collection<IModule> iModules = ModuleHandle.INSTANCE.getApkModuleContainer().values();
        for (IModule iModule : iModules) {
            if (IModuleTask.class.isInstance(iModule)) {
                ((IModuleTask) iModule).doInitModuelTask(mContext);
            }
        }
    }

    @Override
    public void doLoginNotifyTask(@NonNull Context mContext, @NonNull String accout) {
        Collection<IModule> iModules = ModuleHandle.INSTANCE.getApkModuleContainer().values();
        for (IModule iModule : iModules) {
            if (IModuleTask.class.isInstance(iModule)) {
                ((IModuleTask) iModule).doLoginNotifyTask(mContext,  accout);
            }
        }
    }


    @Override
    public void doLogoutNotifyTask(@NonNull Context mContext) {
        Collection<IModule> iModules = ModuleHandle.INSTANCE.getApkModuleContainer().values();
        for (IModule iModule : iModules) {
            if (IModuleTask.class.isInstance(iModule)) {
                ((IModuleTask) iModule).doLogoutNotifyTask(mContext);
            }
        }
    }

    @Override
    public void doExitApkNotifyTask(@NonNull Context mContext) {
        Collection<IModule> iModules = ModuleHandle.INSTANCE.getApkModuleContainer().values();
        for (IModule iModule : iModules) {
            if (IModuleTask.class.isInstance(iModule)) {
                ((IModuleTask) iModule).doExitApkNotifyTask(mContext);
            }
        }
    }


    private IModuleTask iModuleObj;

    private IModuleTask handleModuleTask(IModuleTask iModuleTask) {
        iModuleObj = (IModuleTask) Proxy.newProxyInstance(
                iModuleTask.getClass().getClassLoader(),
                iModuleTask.getClass().getInterfaces(),
                (object, method, args) -> {
                    if (method.getGenericParameterTypes().length == 0)
                        return method.invoke(iModuleTask);
                    return method.invoke(iModuleTask, args);
                });
        return iModuleObj;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (method.getGenericParameterTypes().length == 0)
            return method.invoke(iModuleObj);
        return method.invoke(iModuleObj, args);
    }
}
