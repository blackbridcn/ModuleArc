package org.net.http.urlconection;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * File: URLConnUtils.java
 * Author: yuzhuzhang
 * Create: 2020/3/14 10:30 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/14 : Create URLConnUtils.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public final class URLConnUtils {

    private int connTimeOut = 5 * 1000;
    private int readTimeOut = 2 * 1000;

    private String postMethod = "POST";
    private String getMethod = "GET";
    private String headMethod = "HEAD";
    private String putMethod = "PUT";
    private String deleteMethod = "DELETE";

    public void doPostRequestTask(String urlPath, String params, List<NameValueParur> paramsList) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream inputStream = null;
        try {
            URL newUrl = new URL(urlPath);
            urlConnection = (HttpURLConnection) newUrl.openConnection();
            urlConnection.setConnectTimeout(connTimeOut);
            urlConnection.setReadTimeout(readTimeOut);
            urlConnection.setRequestMethod(postMethod);
            urlConnection.setDoInput(true);//接收输入流
            urlConnection.setDoOutput(true);//启动输出流
            urlConnection.setRequestProperty("Connection", "Keep-Alive");//添加Header
            urlConnection.connect();
            writeParams(urlConnection.getOutputStream(), paramsList);
            //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream())
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }

    public void doLoadFileTask(String urlPath, String filePath) {

    }


    private void writeParams(OutputStream outputStream, List<NameValueParur> paramsList) throws IOException {
        StringBuilder param = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            paramsList.forEach(nameValueParur -> {
                if (!TextUtils.isEmpty(param)) {
                    param.append("&");
                }
                try {
                    param.append(URLEncoder.encode(nameValueParur.key, "UTF-8"));
                    param.append("=");
                    param.append(URLEncoder.encode(nameValueParur.value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            });
        }

        OutputStreamWriter writerStream = new OutputStreamWriter(outputStream, "UTF-8");
        writerStream.write(param.toString());
        writerStream.flush();
        writerStream.close();

    }

    public static class NameValueParur {
        String key;
        String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
