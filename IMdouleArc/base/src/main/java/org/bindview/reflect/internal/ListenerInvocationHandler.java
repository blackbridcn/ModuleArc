package org.bindview.reflect.internal;

import android.os.Build;
import android.util.ArrayMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ListenerInvocationHandler implements InvocationHandler {
    private Object target;
    //需要拦截的方法集合
    private Map<String, Method> map;
    //  private Map<String, Method> map = new HashMap();

    public ListenerInvocationHandler(Object target) {
        this.target = target;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            map = new ArrayMap<String, Method>();
        } else {
            map = new HashMap();
        }
    }

    @Override
    public Object invoke(Object proxy, Method methods, Object[] args) throws Throwable {
        if (this.target != null) {
            Method method = map.get(methods.getName());//替换为回调的私有方法
            if (method != null) {
                if (method.getGenericParameterTypes().length == 0) {
                    return method.invoke(target);
                }
                return method.invoke(target, args);
            }
        }
        return null;
    }

    public void addListenerMethod(String mathodName, Method mMethod) {
        map.put(mathodName, mMethod);
    }

}
