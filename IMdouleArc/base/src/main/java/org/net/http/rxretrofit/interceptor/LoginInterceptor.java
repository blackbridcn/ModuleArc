package org.net.http.rxretrofit.interceptor;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.application.BaseApplication;
import com.base.BaseJson;
import com.base.BaseNetErrorCode;
import com.constant.BaseContstant;

import org.net.http.rxretrofit.api.BaseUrl;
import org.utils.SPUtils;
import org.utils.StringUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public class LoginInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) {
        Response response = null;
        try {
            Request request = chain.request();
            String url = request.url().toString();
            response = chain.proceed(request);
            Log.e("TAG", "intercept: ----------------> url : " + url);
            if (!url.contains("/PeakeCloud/api/user/login")) {
                Log.e("TAG", "intercept: ----------------->  ");

                if (onFilterRespBody(response)) {
                    ResponseBody body = response.body();
                    BufferedSource source = body.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Charset charset = UTF8;
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        try {
                            charset = mediaType.charset(UTF8);
                        } catch (UnsupportedCharsetException e) {
                            return response;
                        }
                    }
                    Buffer buffer = source.buffer();
                    if (!isPlaintext(buffer)) {
                        return response;
                    }
                    long length = body.contentLength();
                    if (length != 0) {
                        String result = buffer.clone().readString(charset);
                        String string = result;
                        if (!StringUtils.isNotEmpty(result)) {
                            Log.e("TAG", "LoginInterceptor: --------------->  " + result);
                            BaseJson baseJson = com.alibaba.fastjson.JSONObject.parseObject(string, BaseJson.class);
                            if (baseJson.getErrorCode() == BaseNetErrorCode.LOGIN_TIME_OUT_ERROR.getValue() /*&& ModuleDataManager.getInstance().isLogin()*/) {
                                Response loginRequestApi = doLoginRequestApi(request, response, chain);
                                return loginRequestApi;
                            }
                        }
                    }
                }
            } else return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * Content-Encoding: gzip
     * Content-Encoding: compress
     * Content-Encoding: deflate
     * Content-Encoding: identity  用于指代自身（例如：未经过压缩和修改）。除非特别指明，这个标记始终可以被接受
     * Content-Encoding: br
     *
     * @param headers
     * @return
     */
    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity");
    }

    //这里过滤response是不是有body
    private boolean onFilterRespBody(Response response) {
        return response.isSuccessful() && HttpHeaders.hasBody(response) && !bodyEncoded(response.headers());
    }

    private Response doLoginRequestApi(Request sourceRequest, Response sourceResponse, Chain chain) throws IOException {
        String mPhone = getAccout();
        String mPassWord = getAccoutPswd();
        Log.e("TAG", "doLoginRequestApi: -------------------------> mPhone : " + mPhone + "  mPassWord:" + mPassWord);
        sourceResponse.body().source().close();
        Request loginRequest = getLoginRequest(mPhone, mPassWord);
        Response proceed = chain.proceed(loginRequest);
        Charset charset = UTF8;
        if (onFilterRespBody(proceed)) {
            ResponseBody loginBody = proceed.body();
            MediaType loginMediaType = loginBody.contentType();
            if (loginMediaType != null) {
                try {
                    charset = loginMediaType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    return sourceResponse;
                }
            }
            BufferedSource loginSource = loginBody.source();
            loginSource.request(Long.MAX_VALUE);
            Buffer loginBuffer = loginSource.buffer();
            if (!isPlaintext(loginBuffer)) {
                return sourceResponse;
            }
            long contentLength = loginBody.contentLength();
            if (contentLength != 0) {
                String loginResult = loginBuffer.clone().readString(charset);
                if (StringUtils.isNotEmpty(loginResult)) {
                    Log.e("TAG", "doLoginRequestApi: --------------> :" + loginResult);
                    BaseJson baseJson = com.alibaba.fastjson.JSONObject.parseObject(loginResult, BaseJson.class);
                    if (baseJson.getResultCode() && baseJson.getErrorCode() == 0) {
                        // LoginData userBean = JSON.parseObject(baseJson.getResultData().toString(), LoginData.class);
                        // userBean.setPhone(mPhone);
                        //ModuleDataManager.getInstance().setLoginData(userBean, mPassWord);
                        return chain.proceed(sourceRequest);// 重新执行;
                    }
                }
            }
        }

        return chain.proceed(sourceRequest);
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                //isISOControl 确定指定的字符是ISO控制字符。一个字符被认为是一个ISO控制字符，如果它的代码是在'符u0000'到'u001F'或' u009F'到'u007F'范围
                if (Character.isISOControl(codePoint)
                        && !Character.isWhitespace(codePoint)) {// isWhitespace方法用于判断指定字符是否为空白字符，空白符包含：空格、tab 键、换行符。
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    public Request getLoginRequest(String accout, String pwsd) {
        Map<String, String> params = new HashMap<String, String>(2);
        params.put("phone", accout);
        params.put("password", pwsd);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("params", JSON.toJSONString(params));
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(BaseUrl.getInstance().getBaseUrl() + "/PeakeCloud/api/user/login")
                .post(formBody).build();
        return request;
    }


    private String getAccoutPswd() {
        return SPUtils.getStringValue(BaseApplication.getContext(), BaseContstant.KEY_LOGIN_PASSWORD);
    }

    private String getAccout() {
        return SPUtils.getStringValue(BaseApplication.getContext(), BaseContstant.KEY_LOGIN_ACCOUNT);
    }
}
