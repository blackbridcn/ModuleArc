package org.bindview.annotation.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解上的注解；
 * 用于告诉注解需要反射处理函数事件
 * <p>
 * 功能对应于Butterknife 中的  ListenerClass 和 ListenerMethod 二者的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MethodEvent {
    /**
     * 这里定义具体注解 设置的监听函数的api函数名字
     * 备注：
     * 记录 给控件设置回调的监听函数名称
     * 例如：setOnClickListener   、 setOnLongClickListener
     * Register a callback to be invoked
     *
     * @return
     */
    String registerCallbaceLisenerApiMethodName();

    /**
     * 这里定义具体注解 设置监听函数中 interface 参数
     * 备注：
     * 设置被注解监听函数的参数
     * 例如：
     * setOnClickListener中的 interface 参数 OnClickListener 、
     * setOnLongClickListener中的 interface 参数 OnLongClickListener
     */
    Class<?>[] callbackListenerInterfaceParams();

    /**
     * 这里定义具体注解的 interface 中具体回调的函数名称
     * 备注：
     * 例如：OnClickListener 中的函数 ：onClick
     * OnLongClickListener 中的函数 onLongClick
     *
     * @return
     */
    String onLisenerMethodCallbackMehod();
}
