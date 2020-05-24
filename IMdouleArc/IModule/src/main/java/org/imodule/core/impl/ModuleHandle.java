package org.imodule.core.impl;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.collection.ArrayMap;

import org.imodule.core.IModuleHandle;
import org.imodule.core.impl.service.ModuleService;
import org.imodule.help.annotation.Module;
import org.imodule.task.IModule;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * Author: yuzzha
 * Date: 12/12/2019 2:52 PM
 * Description: Module 模块的核心类
 * Remark:
 */
public enum ModuleHandle implements IModuleHandle {

    INSTANCE;

    private boolean isManual = false;

    private static final Map<String, IModule> moduleMap = new ArrayMap<String, IModule>(7);

    /**
     * 利用Java的Spi原理自动将Apk的各个Module注册到Map中
     */
    @Override
    @UiThread
    public void initModuel() {
        moduleMap.clear();
        // ServiceLoader实际还是通过路径名反射来完成，只是其通过配置到META_INF中的目录文件来完成解耦
        ModuleService<IModule> moduleService = ModuleService.load(IModule.class, moduleMap);
        Iterator<IModule> mIterator = moduleService.iterator();
        while (mIterator != null && mIterator.hasNext()) {
            mIterator.next();
        }

    }

    @Override
    public void inject(Object object) {
        injectModule(object, object.getClass());
    }

    @Override
    public void inject(Object object, Class clazz) {
        injectModule(object, clazz);
    }

    private void injectModule(Object object, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        boolean foundAnnotation = false;
        for (Field mField : fields) {
            Module module = mField.getAnnotation(Module.class);
            if (module != null) {
                foundAnnotation = true;
                Class type = mField.getType();
                if (IModule.class.isAssignableFrom(type)) {
                    String value = module.value();
                    IModule iModule = getIModuleObj(value, type);
                    // Log.e("TAG", "injectModule: IModule :" + iModule.getClass().getSimpleName() + " clazz: " + clazz.getSimpleName());
                    mField.setAccessible(true);
                    try {
                        mField.set(object, iModule);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //todo 这样也可能会漏掉一些提示,如果有多个需要注入的字段，有些在父类有些在子类里定义.
        if (!foundAnnotation) {
            logMsg(object.getClass().getSimpleName() + " 请求注入却没有发现要注入的字段．");
        }
    }

    @NonNull
    @Override
    public IModule getManualRegisterModule(@NonNull String moduleName, Class<? extends IModule> c) {
        String moduleKey = buildModuleKey(moduleName, c);
        IModule iModule = moduleMap.get(moduleKey);
        if (iModule == null) {
            String error = "模块 " + c.getSimpleName() + " 未注册!!";
            logMsg(error);
            return null;
        }
        return iModule;
    }

    @NonNull
    @Override
    public IModule getSpiRegisterModule(@NonNull String moduleKey) {
        IModule iModule = moduleMap.get(moduleKey);
        if (iModule == null) {
            String error = "模块 " + moduleKey + " 未注册!!";
            logMsg(error);
            return null;
        }
        return iModule;
    }

    @Override
    @Deprecated
    public void doRegisterModuleTask(String moduleName, Class<? extends IModule> clazz, IModule module) {
        isManual = true;
        String moduleKey = buildModuleKey(moduleName, clazz);
        onRegisterModule(moduleKey, module);
    }

    /**
     * 将Apk的各个Module注册到Map中
     *
     * @param moduleKey IModule 在容器中的 Key
     * @param module    IModule Object
     */
    private void onRegisterModule(String moduleKey, IModule module) {
        moduleMap.put(moduleKey, module);
        logMsg("onRegisterModule  :" + moduleKey + " " + module.getClass().getSimpleName());
    }

    @Override
    public Map<String, IModule> getApkModuleContainer() {
        return moduleMap;
    }

    @NonNull
    public IModule getIModuleObj(String moduleTag, Class<? extends IModule> clazz) {
        if (isManual)
            moduleMap.get(buildModuleKey(moduleTag, clazz));
        return moduleMap.get(moduleTag);
    }

    private String buildModuleKey(String moduleName, Class<?> clazz) {
        return moduleName + ":" + (clazz != null ? clazz.getName() : "null");
    }

    private void logMsg(String msg) {
        Log.e("TAG", "logMsg: ---------------->  " + msg);
    }

}
