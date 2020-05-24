package org.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {

    public static <E> List<E> creatArryList(E... element) {
        ArrayList list = new ArrayList<E>();
        Collections.addAll(list, element);
        return list;
    }

    @NonNull
    public static <T extends Collection<Y>, Y> T checkNotEmpty(@NonNull T collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Must not be empty.");
        }
        return collection;
    }


    public static <T> boolean isNotEmpty(@NonNull Collection<T> collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }

    public static <T> List<T> addElement(List<T> list, T... params) {
        if (null == list) {
            return list;
            //throw new RuntimeException(" List  :" + list.getClass().getName() + " is Null ");
        }

        if (null == params) {
            return list;
        }
        //将参数添加到List集合中
        if (null != params) {
            for (T t : params) {
                list.add(t);
            }
        }
        return list;
    }

    public static <T> boolean isEmpty(@NonNull Collection<T> collection) {
        if (collection == null || collection.size() == 0)
            return true;
        return false;
    }


}
