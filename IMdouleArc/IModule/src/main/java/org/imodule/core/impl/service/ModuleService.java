package org.imodule.core.impl.service;

import androidx.annotation.NonNull;

import org.imodule.help.annotation.ModuleImpl;
import org.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceConfigurationError;

/**
 * Author: yuzzha
 * Date: 1/7/2020 10:49 AM
 * Description:
 * Remark:
 */
public class ModuleService<S> implements Iterable<S> {

    private static final String PREFIX = "META-INF/services/";

    private final Class<S> service;

    private final ClassLoader loader;
    private Map<String, S> map;

    private LinkedHashMap<String, S> providers = new LinkedHashMap<>();

    private LazyIterator lookupIterator;

    public static <S> ModuleService<S> load(Class<S> service,
                                            ClassLoader loader, Map<String, S> map) {
        return new ModuleService<S>(service, loader, map);
    }

    public static <S> ModuleService<S> load(Class<S> service, Map<String, S> map) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return load(service, cl, map);
    }

    private ModuleService(Class<S> svc, ClassLoader cl, Map<String, S> map) {
        service = Objects.requireNonNull(svc, "Service interface cannot be null");
        loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
        this.map = map;
        reload();
    }

    public void reload() {
        if (map != null) map.clear();
        providers.clear();
        lookupIterator = new LazyIterator(service, loader, map);
    }

    @NonNull
    @Override
    public Iterator<S> iterator() {
        return new Iterator<S>() {
            Iterator<Map.Entry<String, S>> mCacheProviders = providers.entrySet().iterator();

            @Override
            public boolean hasNext() {
                if (mCacheProviders.hasNext()) return true;
                return lookupIterator.hasNext();
            }

            @Override
            public S next() {
                if (mCacheProviders.hasNext())
                    return mCacheProviders.next().getValue();
                return lookupIterator.next();
            }
        };
    }

    private class LazyIterator implements Iterator<S> {
        Class<S> service;
        ClassLoader loader;

        String nextName = null;

        Enumeration<URL> configs = null;

        Iterator<String> pending = null;
        Map<String, S> map;

        public LazyIterator(Class<S> service, ClassLoader loader, Map<String, S> map) {
            this.service = service;
            this.loader = loader;
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            return hasModuleNext();
        }

        @Override
        public S next() {
            return nextModule();
        }

        private S nextModule() {
            if (!hasModuleNext())
                throw new NoSuchElementException();
            String clazzName = nextName;
            nextName = null;
            Class<?> moduleClazz = null;
            try {
                //这里是通过反射获取注册的类
                moduleClazz = Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (!service.isAssignableFrom(moduleClazz)) {
            }
            try {
                ModuleImpl module = (ModuleImpl) moduleClazz.getAnnotation(ModuleImpl.class);
                Object instance = moduleClazz.newInstance();
                S p = service.cast(instance);
                if (module != null && StringUtils.isNotEmpty(module.value())) {
                    map.put(module.value(), p);
                }
                providers.put(clazzName, p);
                return p;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            throw new Error();
        }

        private boolean hasModuleNext() {
            if (nextName != null) {
                return true;
            }
            if (configs == null) {
                String fullName = PREFIX + service.getName();
                try {
                    //这里通过类加载器 读取在META-INF/services/service.getName()
                    //下配置的类并且把资源文件绝对的路径通过URL返回
                    //这个路径是只读取一次
                    if (loader == null) {
                        configs = ClassLoader.getSystemResources(fullName);
                    } else
                        configs = loader.getResources(fullName);
                } catch (IOException e) {
                    e.printStackTrace();
                    fail(service, "Error locating configuration files", e);
                }
            }
            while ((pending == null) || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(service, configs.nextElement());
            }
            nextName = pending.next();
            return true;
        }
    }

    //这里是读取 META-INF/services/service.getName()中 配置的所有class 并存储在Iterator
    private Iterator<String> parse(Class<?> service, URL mResPath) throws ServiceConfigurationError {
        InputStream in = null;
        BufferedReader bufferedReader = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            in = mResPath.openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            int len = 1;
            while ((len = parseLine(service, mResPath, bufferedReader, len, names)) >= 0) ;
        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (in != null) in.close();
            } catch (IOException y) {
                fail(service, "Error closing configuration file", y);
            }
        }
        return names.iterator();
    }

    //这里是逐行读取 META-INF/services/  并且将读取到的每个类存储进 providers 缓存中
    private int parseLine(Class<?> service, URL u, BufferedReader bufferedReader, int readLineNum,
                          List<String> names) throws IOException {
        String readLine = bufferedReader.readLine();
        if (readLine == null) {
            return -1;
        }
        int ci = readLine.indexOf('#');
        if (ci >= 0) readLine = readLine.substring(0, ci);
        readLine = readLine.trim();
        int n = readLine.length();
        if (n != 0) {
            if ((readLine.indexOf(' ') >= 0) || (readLine.indexOf('\t') >= 0))
                fail(service, u, readLineNum, "Illegal configuration-file syntax");
            //这里是起始字符校验 判断是否符合java起始字符
            int cp = readLine.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                fail(service, u, readLineNum, "Illegal provider-class name: " + readLine);
            //
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = readLine.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    fail(service, u, readLineNum, "Illegal provider-class name: " + readLine);
            }
            //这里是判读
            if (!providers.containsKey(readLine) && !names.contains(readLine))
                names.add(readLine);
        }
        return readLineNum + 1;
    }


    private static void fail(Class<?> service, String msg, Throwable cause)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg,
                cause);
    }

    private static void fail(Class<?> service, URL u, int line, String msg)
            throws ServiceConfigurationError {
        // fail(service, u + ":" + line + ": " + msg);
    }

}
