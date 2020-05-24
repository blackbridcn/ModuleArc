package org.imodule.core;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import org.imodule.task.IModule;

import java.util.Map;

/**
 * Author: yuzzha
 * Date: 12/12/2019 2:47 PM
 * Description:
 * Remark:
 */
public interface IModuleHandle {

    /**
     * SPI 初始化加载所有功能模块
     */
    @UiThread
    void initModuel();

    /**
     * 具体业务中Model的依赖注入接口
     *
     * @param object Fragment或者Activity
     */
    @UiThread
    void inject(Object object);

    /**
     * 具体业务中Model的依赖注入接口
     *
     * @param object Fragment或者Activity
     * @param clazz  object.getClass.getName()
     */
    @UiThread
    void inject(Object object, Class clazz);

    /**
     * 手动将将Apk的各个Module注册到Map中
     *
     * @param moduleName IModule 实例的class类名
     * @param clazz      模块的实现IModule接口的具体实例类
     * @param module     IModule Object
     */
    @Deprecated @UiThread
    void doRegisterModuleTask(String moduleName, Class<? extends IModule> clazz, IModule module);


    /**
     * 获取指定模块的IModule对象,这里拿到的是某个模块的一个接口,一个模块可能有多个接口.
     *
     * @param moduleName 要获取的模块的名称。
     * @param clazz      要获取的接口名称,这里是接口，而不是具体的实现类名
     * @return IModule Object
     */
    @NonNull @UiThread
    IModule getManualRegisterModule(String moduleName, Class<? extends IModule> clazz);


    /**
     * 采用Spi方式进行模块注册的方式时,获取指定IModule对象
     *
     * @param moduleKey IModule 在容器中的Key
     * @return IModule Object
     */
    @NonNull @UiThread
    IModule getSpiRegisterModule(String moduleKey);

    /**
     * 获取IModule 实例对象  【既可以是SPI模式注册也可以是手动注册都可以使用这个函数获取IModule对象】
     *
     * @param moduleTag moduleKey 或者是 moduleName
     * @param clazz     Class<? extends IModule>
     * @return IModule Object
     */
    IModule getIModuleObj(String moduleTag, Class<? extends IModule> clazz);

    /**
     * 获取apk中IModule容器
     *
     * @return Map<String, IModule> 模块容器
     */
    Map<String, IModule> getApkModuleContainer();

}
