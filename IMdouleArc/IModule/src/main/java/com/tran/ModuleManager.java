package com.tran;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.application.BaseApplication;
import com.tran.itask.IModuleTask;
import com.tran.itask.handle.HandleModuleTask;

import org.imodule.core.impl.ModuleHandle;
import org.imodule.core.impl.register.IModuleRegister;
import org.imodule.task.IModule;
import org.utils.StringUtils;

import java.lang.reflect.Method;

/**
 * Author: yuzzha
 * Date: 12/17/2019 11:23 AM
 * Description: 在整个Apk中使用对module相关的一个对外操作接口
 * Remark: 所有涉及到模块函数操作  入口类
 */
public class ModuleManager {

    public static void initModule() {
        ModuleHandle.INSTANCE.initModuel();
    }

    public static void doModuleInitTask(Context mContext){
        HandleModuleTask.getInstance().doInitModuelTask(BaseApplication.getContext());
    }

    @Deprecated
    public static void register(IModuleRegister iModuleRegister) {
        iModuleRegister.doRegisterModuleTask(ModuleHandle.INSTANCE);
    }

    /**
     * 具体业务中Model的依赖注入接口
     *
     * @param object Fragment或者Activity
     */
    public static void injectModule(Object object) {
        injectModule(object, object.getClass());
    }

    /**
     * 具体业务中 Model的依赖注入接口
     *
     * @param object Fragment或者Activity
     * @param clazz  object.getClass.getName()
     */
    public static void injectModule(Object object, Class clazz) {
        ModuleHandle.INSTANCE.inject(object, clazz);
    }


    public static IModule getIModuleObj(String moduleTag, Class<? extends IModule> clazz) {
        return ModuleHandle.INSTANCE.getIModuleObj(moduleTag, clazz);
    }

    /**
     * 获取各个 Module分发任务 Service
     *
     * @return IModuleTask
     */
    public static IModuleTask getModuleTaskService() {
        return HandleModuleTask.getInstance();
    }


    public static void doReflectionActivityTask(@NonNull Activity mActivity, @NonNull String ModuleName, @NonNull String MethodName, @NonNull String operateId) {
        if (StringUtils.isEmpty(operateId)) {
            Log.e("TAG", "onMenuClickEvent: ----------------------->  IModule :");
            return;
        }
        IModule iModule = ModuleHandle.INSTANCE.getIModuleObj(ModuleName, null);
        try {
            Method method = iModule.getClass().getDeclaredMethod(MethodName, Context.class, String.class);
            method.invoke(iModule, mActivity, operateId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "onMenuClickEvent: ---------------------->  Exception :" + e.getMessage());
        }
    }

}
