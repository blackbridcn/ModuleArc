package org.net.http.rxretrofit.cookie;


import org.net.http.rxretrofit.cookie.store.CookieStore;
import org.net.http.rxretrofit.cookie.store.HasCookieStore;
import org.net.http.rxretrofit.cookie.store.MemoryCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zhy on 16/3/10.
 */
public class CookieJarImpl implements CookieJar, HasCookieStore {
    private MemoryCookieStore cookieStore;

    private static class CookieJarHolder {
        private static final CookieJarImpl Instance = new CookieJarImpl();
    }

    public static CookieJarImpl getInstance() {
        return CookieJarHolder.Instance;
    }

    private CookieJarImpl() {
        this.cookieStore = new MemoryCookieStore();
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    @Override
    public CookieStore getCookieStore() {
        return this.cookieStore;
    }
}
