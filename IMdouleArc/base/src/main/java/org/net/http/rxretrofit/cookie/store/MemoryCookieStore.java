package org.net.http.rxretrofit.cookie.store;

import android.text.TextUtils;

import org.utils.LogUtils;
import org.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 *
 */
public class MemoryCookieStore implements CookieStore {
    private final HashMap<String, List<Cookie>> allCookies = new HashMap<>();

    @Override
    public void add(HttpUrl url, List<Cookie> cookies) {
        if (cookies == null)
            return;
        List<Cookie> oldCookies = allCookies.get(url.host());
        if (oldCookies == null) {
            allCookies.put(url.host(), cookies);
        } else {
            Iterator<Cookie> itNew = cookies.iterator();
            Iterator<Cookie> itOld = oldCookies.iterator();
            String mCookieKey, mCookieName;
            while (itNew.hasNext()) {
                mCookieKey = itNew.next().name();
                if (StringUtils.isNotEmpty(mCookieKey))
                    while (itOld.hasNext()) {
                        mCookieName = itOld.next().name();
                        if (StringUtils.isNotEmpty(mCookieName) && TextUtils.equals(mCookieKey, mCookieName)) {
                            itOld.remove();
                            break;
                        }
                    }
            }
            oldCookies.addAll(cookies);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            sb.append(cookies.get(i).toString()).append("\n");
        }
        makeLog("add Cookie:", sb.toString());
    }

    @Override
    public List<Cookie> get(HttpUrl uri) {
        List<Cookie> cookies = allCookies.get(uri.host());
        if (cookies == null) {
            cookies = new ArrayList<>();
            allCookies.put(uri.host(), cookies);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            sb.append(cookies.get(i).toString()).append("\n");
        }
        makeLog("get Cookie:", sb.toString());
        return cookies;

    }

    @Override
    public boolean removeAll() {
        allCookies.clear();
        makeLog("removeAll Cookie:", "removeAll");
        return true;
    }

    @Override
    public List<Cookie> getCookies() {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = allCookies.keySet();
        for (String url : httpUrls) {
            cookies.addAll(allCookies.get(url));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            sb.append(cookies.get(i).toString()).append("\n");
        }
        makeLog("getCookies :", "cookies " + sb.toString());
        return cookies;
    }


    @Override
    public boolean remove(HttpUrl uri, Cookie cookie) {
        List<Cookie> cookies = allCookies.get(uri);
        if (cookie != null) {
            makeLog("remove :" + uri.toString(), "cookies " + cookie.toString());
            return cookies.remove(cookie);
        }
        return false;
    }


    private void makeLog(String tag, String msg) {
        LogUtils.e(tag, msg);
    }

}
