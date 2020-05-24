package org.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtils {

    public static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Closeable... stream) {
        try {
            for (Closeable closeable : stream) {
                if (closeable == null) continue;
                closeable.close();
            }
        } catch (IOException e) {

        }
    }
}
