package org.db.orm.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrmDbUtils {

    public static <E> List<E> creatArryList(E... element) {
        ArrayList list = new ArrayList<E>();
        Collections.addAll(list, element);
        return list;
    }

    public static <K, V> List<V> mapToArrayList(Map<K, V> map) {
        List<V> entryList = new ArrayList<V>();
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            entryList.add(iterator.next().getValue());
        }
        return entryList;
    }

    public static <T extends Comparable<? super T>> List<T> sort(@NonNull List<T> list) {
        Collections.sort(list);
        return list;
    }

    @NonNull
    public static <T extends Collection<Y>, Y> T checkNotEmpty(@NonNull T collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Must not be empty.");
        }
        return collection;
    }


    public static <T> boolean isEmpty(@NonNull Collection<T> collection) {
        if (collection == null || collection.size() == 0)
            return true;
        return false;
    }

    public static <T> boolean isNotEmpty(@NonNull Collection<T> collection) {
        if (collection != null && collection.size() > 0)
            return true;
        return false;
    }

    public static <T> List<T> addElement(List<T> list, T... params) {
        if (null == list) {
            throw new RuntimeException(" List  :" + list.getClass().getName() + " is Null ");
        }
        if (null == params) {
            return list;
        }
        if (null != params) {
            for (T t : params) {
                list.add(t);
            }
        }
        return list;
    }

}
