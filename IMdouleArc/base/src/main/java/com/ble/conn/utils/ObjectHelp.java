package com.ble.conn.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;

public class ObjectHelp {

    @NonNull
    public static <T> T checkNotNull(@Nullable T arg) {
        return checkNotNull(arg, "Argument must not be null");
    }

    @NonNull
    public static <T> T checkNotNull(@Nullable T arg, @NonNull String message) {
        if (arg == null) {
            throw new NullPointerException(message);
        }
        return arg;
    }

    @NonNull
    public static String checkNotEmpty(@Nullable String string) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException( "Must not be null or empty");
        }
        return string;
    }

    @NonNull
    public static String checkNotEmpty(@Nullable String string,String Msg) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException( Msg+"Must not be null or empty");
        }
        return string;
    }

    @NonNull
    public static <T extends Collection<Y>, Y> T checkNotEmpty(@NonNull T collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Must not be empty.");
        }
        return collection;
    }




}
