package org.bindview.reflect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import org.bindview.annotation.ContentView;
import org.bindview.annotation.field.res.BindAnim;
import org.bindview.annotation.field.res.BindBitmap;
import org.bindview.annotation.field.res.BindDrawable;
import org.bindview.annotation.field.res.BindFont;
import org.bindview.annotation.field.values.BindBool;
import org.bindview.annotation.field.values.BindColor;
import org.bindview.annotation.field.values.BindDimen;
import org.bindview.annotation.field.values.BindFloat;
import org.bindview.annotation.field.values.BindString;
import org.bindview.annotation.field.widget.BindView;
import org.bindview.annotation.field.widget.BindViews;
import org.bindview.annotation.internal.MethodEvent;
import org.bindview.reflect.internal.ListenerInvocationHandler;
import org.bindview.reflect.internal.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.STATIC;

public class BindEvent {
    private static boolean isReflect = true;

    public static void bind(Activity mActivity) {
        bind(mActivity, mActivity.getWindow().getDecorView());
    }

    public static void bind(@NonNull Object target, @NonNull View source) {
        String clsName = target.getClass().getName();
        //如果不是 android 、java 、androidx 对象直接屏蔽绑定事件
        if (clsName.startsWith("android.") || clsName.startsWith("java.") || clsName.startsWith("androidx."))
            return;
        if (target instanceof Activity) {
            BindLayout((Activity) target);
        }
        bingField(target, source);
        BindMethod(target, source);
    }

    @SuppressLint("ResourceType")
    private static void BindLayout(Activity activity) {
        ContentView contentView = activity.getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            int layId = contentView.value();
            if (layId != -1) {
                try {
                    if (isReflect) {
                        Method setContentView = activity.getClass().getMethod("setContentView", int.class);
                        setContentView.invoke(activity, layId);
                    } else {
                        activity.setContentView(layId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 绑定class 中的 Filed 对象
     *
     * @param target Class 对象
     * @param source rootView
     */
    private static void bingField(@NonNull Object target, @NonNull View source) {
        Field[] fields = target.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            boolean parseResult;
            for (Field field : fields) {
                //绑定单个Field的View
                parseResult = BindView(target, source, field);
                if (parseResult) continue;
                //绑定一个View数组
                parseResult = BindViews(target, source, field);
                if (parseResult) continue;
                //绑定一个Color资源对象
                parseResult = BindColor(target, source, field);
                if (parseResult) continue;
                //绑定一个Dimen 资源对象
                parseResult = BindDimen(target, source, field);
                if (parseResult) continue;
                //绑定一个Drawable 资源对象
                parseResult = BindDrawable(target, source, field);
                if (parseResult) continue;
                //绑定一个String资源对象
                parseResult = BindString(target, source, field);
                if (parseResult) continue;
                //绑定一个Anim资源对象
                parseResult = BindAnim(target, source, field);
                if (parseResult) continue;
                //绑定一个Bitmap 资源对象
                parseResult = BindBitmap(target, source, field);
                if (parseResult) continue;
                //绑定一个boolean 资源对象
                parseResult = BindBool(target, field, source);
                if (parseResult) continue;
                //绑定一个float 资源值
                parseResult = BindFloat(target, field, source);
                if (parseResult) continue;
                //绑定一个font字体 资源
                parseResult = BindFont(target, field, source);
                if (parseResult) continue;

            }
        }
    }

    /**
     * 绑定Field 单个View
     *
     * @param target Field 的 class 对象
     * @param source rootView
     * @param field  Field 对象
     * @return true 表示 对本 Field  就是本函数处理的对象类型 并已经为Field对象 绑定View
     */
    private static boolean BindView(@NonNull Object target, @NonNull View source, Field field) {
        BindView bind = field.getAnnotation(BindView.class);
        if (bind == null) {
            return false;
        }
        try {
            validateMember(field);
            if (field.get(target) != null) { //已经赋值了
                return true;
            }
            validateViewField(field);
            View viewValue = Utils.findRequiredView(source, bind.value(), "field '" + field.getName() + "'");
                        /*if (isReflect) {
                            Method method = target.getClass().getMethod("findViewById", int.class);
                            Object invoke = method.invoke(target,  bind.value());
                            field.set(target, invoke);
                        } else {
                            field.set(target, source.findViewById( bind.value()));
                        }*/
            Utils.doSetFieldValueTask(field, target, viewValue);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 绑定一个 View 数组
     *
     * @param target Class对象
     * @param source rootView
     * @param field  Field
     * @return 返回当前Field是否为当前绑定类型  true 表示
     */
    private static boolean BindViews(@NonNull Object target, @NonNull View source, Field field) {
        BindViews bindViews = field.getAnnotation(BindViews.class);
        if (bindViews == null) {
            return false;
        }
        validateMember(field);
        Class<?> fieldClass = field.getType();
        Class<?> viewClass;
        boolean isArary = fieldClass.isArray();
        if (isArary) {
            viewClass = fieldClass.getComponentType();
        } else if (fieldClass == List.class) {  //List情况下实现读取List<T>泛型
            Type fieldType = field.getGenericType();
            if (fieldType instanceof ParameterizedType) {
                // Type[] getActualTypeArguments() 返回表示此类型实际类型参数的 Type 对象的数组
                Type viewType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                // TODO real rawType impl!!!!
                viewClass = (Class<?>) viewType;
            } else {
                throw new IllegalStateException("@BindViews List must have a generic component. ("
                        + field.getDeclaringClass().getName()
                        + '.'
                        + field.getName()
                        + ')');
            }
        } else {
            throw new IllegalStateException("@BindViews must be a List or array. ("
                    + field.getDeclaringClass().getName()
                    + '.'
                    + field.getName()
                    + ')');
        }
        //有效性检查
        if (!View.class.isAssignableFrom(viewClass) && !viewClass.isInterface()) {
            throw new IllegalStateException(
                    "@BindViews List or array type must extend from View or be an interface. ("
                            + field.getDeclaringClass().getName()
                            + '.'
                            + field.getName()
                            + ')');
        }
        int[] resIds = bindViews.value();
        if (resIds.length == 0) {
            throw new IllegalStateException("@BindViews must specify at least one ID. ("
                    + field.getDeclaringClass().getName()
                    + '.'
                    + field.getName()
                    + ')');
        }
        List<Object> views = new ArrayList<>(resIds.length);
        String who = "field '" + field.getName() + "'";
        Object view;
        for (int resId : resIds) {
            view = Utils.findRequiredView(source, resId, who);
            if (view != null) {
                views.add(view);
            }
        }
        Object value;
        if (isArary) {
            Object[] viewArray = (Object[]) Array.newInstance(viewClass, resIds.length);
            value = views.toArray(viewArray);
        } else {
            value = views;
        }
        Utils.doSetFieldValueTask(field, target, value);
        return true;
    }

    private static boolean BindColor(@NonNull Object target, @NonNull View source, Field field) {
        BindColor bindColor = field.getAnnotation(BindColor.class);
        if (bindColor == null) {
            return false;
        }
        validateMember(field);
        try {
            if (field.get(target) != null) { //已经赋值了
                return true;
            }
            int id = bindColor.value();
            Context context = source.getContext();
            Object value;
            Class<?> fieldType = field.getType();
            if (fieldType == int.class) {
                value = ContextCompat.getColor(context, id);
            } else if (fieldType == ColorStateList.class) {
                value = ContextCompat.getColorStateList(context, id);
            } else {
                throw new IllegalStateException("@BindColor field type must be 'int' or 'ColorStateList'. ("
                        + field.getDeclaringClass().getName()
                        + '.'
                        + field.getName()
                        + ')');
            }
            Utils.doSetFieldValueTask(field, target, value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean BindDimen(@NonNull Object target, @NonNull View source, Field field) {
        BindDimen bindDimen = field.getAnnotation(BindDimen.class);
        if (bindDimen == null) {
            return false;
        }
        validateMember(field);
        try {
            if (field.get(target) != null) { //已经赋值了
                return true;
            }
            int id = bindDimen.value();
            Resources resources = source.getContext().getResources();
            Object value;
            Class<?> fieldType = field.getType();
            if (fieldType == int.class) {
                value = resources.getDimensionPixelSize(id);
            } else if (fieldType == float.class) {
                value = resources.getDimension(id);
            } else {
                throw new IllegalStateException("@BindDimen field type must be 'int' or 'float'. ("
                        + field.getDeclaringClass().getName()
                        + '.'
                        + field.getName()
                        + ')');
            }
            Utils.doSetFieldValueTask(field, target, value);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private static boolean BindDrawable(@NonNull Object target, @NonNull View source, Field field) {
        BindDrawable bindDimen = field.getAnnotation(BindDrawable.class);
        if (bindDimen == null) {
            return false;
        }
        validateMember(field);
        try {
            if (field.get(target) != null) { //已经赋值了
                return true;
            }
            Context context = source.getContext();
            int id = bindDimen.value();
            int tint = bindDimen.tint();
            Class<?> fieldType = field.getType();
            Object value;
            if (fieldType == Drawable.class) {
                value = tint != -1 ? Utils.getTintedDrawable(context, id, tint) : ContextCompat.getDrawable(context, id);
            } else {
                throw new IllegalStateException("@BindDrawable field type must be 'Drawable'. ("
                        + field.getDeclaringClass().getName()
                        + '.'
                        + field.getName()
                        + ')');
            }
            Utils.doSetFieldValueTask(field, target, value);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private static boolean BindString(@NonNull Object target, @NonNull View source, Field field) {
        BindString bindDimen = field.getAnnotation(BindString.class);
        if (bindDimen == null) {
            return false;
        }
        validateMember(field);
        try {
            if (field.get(target) != null) { //已经赋值了
                return true;
            }
            Class<?> fieldType = field.getType();
            int id = bindDimen.value();
            Context mContext = source.getContext();
            Object value;
            if (fieldType == String.class) {
                value = mContext.getResources().getString(id);
                Utils.doSetFieldValueTask(field, target, value);
                return true;
            } else {
                throw new IllegalStateException("@BindString field type must be 'String'. ("
                        + field.getDeclaringClass().getName()
                        + '.'
                        + field.getName()
                        + ')');
            }

        } catch (Exception e) {

        }
        return false;
    }

    private static boolean BindAnim(Object target, @NonNull View source, Field field) {
        BindAnim bindAnim = field.getAnnotation(BindAnim.class);
        if (bindAnim == null) {
            return false;
        }
        validateMember(field);
        int id = bindAnim.value();
        Context context = source.getContext();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == Animation.class) {
            value = AnimationUtils.loadAnimation(context, id);
        } else {
            throw new IllegalStateException("@BindAnim field type must be 'Animation'. ("
                    + field.getDeclaringClass().getName()
                    + '.'
                    + field.getName()
                    + ')');
        }
        Utils.doSetFieldValueTask(field, target, value);
        return true;
    }

    private static boolean BindBitmap(Object target, @NonNull View source, Field field) {
        BindBitmap bindBitmap = field.getAnnotation(BindBitmap.class);
        if (bindBitmap == null) {
            return false;
        }
        validateMember(field);
        int id = bindBitmap.value();
        Resources resources = source.getContext().getResources();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == Animation.class) {
            value = BitmapFactory.decodeResource(resources, id);
        } else {
            throw new IllegalStateException("@BindAnim field type must be 'Animation'. ("
                    + field.getDeclaringClass().getName()
                    + '.'
                    + field.getName()
                    + ')');
        }
        Utils.doSetFieldValueTask(field, target, value);
        return true;
    }

    private static boolean BindBool(Object target, Field field, View source) {
        BindBool bindBool = field.getAnnotation(BindBool.class);
        if (bindBool == null) {
            return false;
        }
        validateMember(field);
        int id = bindBool.value();
        Resources resources = source.getContext().getResources();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == boolean.class) {
            value = resources.getBoolean(id);
        } else {
            throw new IllegalStateException("@BindBool field type must be 'boolean'. ("
                    + field.getDeclaringClass().getName()
                    + '.'
                    + field.getName()
                    + ')');
        }
        Utils.doSetFieldValueTask(field, target, value);
        return true;
    }

    private static boolean BindFloat(Object target, Field field, View source) {
        BindFloat bindInt = field.getAnnotation(BindFloat.class);
        if (bindInt == null) {
            return false;
        }
        validateMember(field);
        int id = bindInt.value();
        Context context = source.getContext();

        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == float.class) {
            value = Utils.getFloat(context, id);
        } else {
            throw new IllegalStateException("@BindFloat field type must be 'float'. ("
                    + field.getDeclaringClass().getName()
                    + '.'
                    + field.getName()
                    + ')');
        }
        Utils.doSetFieldValueTask(field, target, value);
        return true;
    }

    private static boolean BindFont(Object target, Field field, View source) {
        BindFont bindFont = field.getAnnotation(BindFont.class);
        if (bindFont == null) {
            return false;
        }
        validateMember(field);

        int id = bindFont.value();
        int style = bindFont.style();
        Context context = source.getContext();

        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == Typeface.class) {

            Typeface font = ResourcesCompat.getFont(context, id);
            switch (style) {
                case Typeface.NORMAL:
                    value = font;
                    break;
                case Typeface.BOLD:
                case Typeface.ITALIC:
                case Typeface.BOLD_ITALIC:
                    value = Typeface.create(font, style);
                    break;
                default:
                    throw new IllegalStateException(
                            "@BindFont style must be NORMAL, BOLD, ITALIC, or BOLD_ITALIC. ("
                                    + field.getDeclaringClass().getName()
                                    + '.'
                                    + field.getName()
                                    + ')');
            }
        } else {
            throw new IllegalStateException("@BindFont field type must be 'Typeface'. ("
                    + field.getDeclaringClass().getName()
                    + '.'
                    + field.getName()
                    + ')');
        }
        Utils.doSetFieldValueTask(field, target, value);
        return true;
    }

    private static void BindMethod(@NonNull Object target, @NonNull View source /*Activity activity*/) {
        final Class<?> aClass = target.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method mMethod : declaredMethods) {
            mMethod.setAccessible(true);
            //获取 函数上注解
            Annotation[] annotations = mMethod.getDeclaredAnnotations();
            //遍历每个函数上的多个注解
            for (Annotation mAnnotation : annotations) {
                //或者每个注解上的   注解类型(ANNOTATION_TYPE修饰的注解)
                Class<? extends Annotation> annotationType = mAnnotation.annotationType();
                if (annotationType != null) {
                    //获取函数注解的注解
                    MethodEvent methodEvent = annotationType.getAnnotation(MethodEvent.class);
                    if (methodEvent != null) {
                        //得到需要被监听绑定的函数名称
                        String lisenerMethodName = methodEvent.registerCallbaceLisenerApiMethodName();
                        //得到需要被监听绑定的函数 的[必须是interface类型]参数
                        final Class<?>[] lisenerParentInterface = methodEvent.callbackListenerInterfaceParams();
                        //需要将被监听绑定的函数回调的函数
                        final String invokeMethodName = methodEvent.onLisenerMethodCallbackMehod();
                        try {
                            //反射获取函数上注解中 的method对象  例如： onClick中的value 函数
                            Method value = annotationType.getDeclaredMethod("value");
                            //通过method 获取 value中规定id值  可能是同时多个widget对象
                            int[] viewResId = (int[]) value.invoke(mAnnotation);
                            /*--------------------------------------------------------------------------------------------------------------------*/
                            //这里生成动态代理 作为设置各个点击事件的监听 interface参数
                            ListenerInvocationHandler handler = new ListenerInvocationHandler(target);
                            handler.addListenerMethod(invokeMethodName, mMethod);
                            Object instance = Proxy.newProxyInstance(lisenerParentInterface[0].getClassLoader(), lisenerParentInterface, handler);
                            /*--------------------------------------------------------------------------------------------------------------------*/

                            for (int viewId : viewResId) {
                                //找到被注解的 widget
                                View evenView = source.findViewById(viewId);
                                //发射获取需求绑定的 监听函数
                                Method eventMethod = evenView.getClass().getMethod(lisenerMethodName, lisenerParentInterface);
                                //通过invoke设置监听事件  instance这个动态代理作为点击事件的interface参数
                                //例如 ：setOnClickListener(OnClickListener listener)
                                eventMethod.invoke(evenView, instance);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

    }

    /**
     * 有效性检查
     * <p>
     * 判断一个Filed 对象是否static 和private 修饰,同时设置 setAccessible(true)
     *
     * @param object
     * @param <T>
     */
    private static <T extends AccessibleObject & Member> void validateMember(T object) {
        int modifiers = object.getModifiers();
        if ((modifiers & (Modifier.PRIVATE | STATIC)) != 0) {
            throw new IllegalStateException(object.getDeclaringClass().getName()
                    + "."
                    + object.getName()
                    + " must not be private or static");
        }
        if ((modifiers & Modifier.PUBLIC) == 0) {
            object.setAccessible(true);
        }
    }

    /**
     * 检查Fiele 是否为 View  或者View的子类
     * <p>
     * 备注：bindView 对象必须是View 或者是View 的子类
     *
     * @param viewField
     */
    private static void validateViewField(Field viewField) {
        Class<?> viewClass = viewField.getType();
        if (!View.class.isAssignableFrom(viewClass) && !viewClass.isInterface()) {
            throw new IllegalStateException(
                    "@BindView fields must extend from View or be an interface. ("
                            + viewField.getDeclaringClass().getName()
                            + '.'
                            + viewField.getName()
                            + ')');
        }
    }

    public Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        Class[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(owner, args);
    }
}
