package org.imodule.core.impl.register;

import org.imodule.core.IModuleHandle;

/**
 * Author: yuzzha
 * Date: 12/17/2019 10:56 AM
 * Description:
 * Remark: 使用手动注册模块时的 interface
 */
public interface IModuleRegister {

    @Deprecated
    void doRegisterModuleTask(IModuleHandle iModuleHandle);
}
