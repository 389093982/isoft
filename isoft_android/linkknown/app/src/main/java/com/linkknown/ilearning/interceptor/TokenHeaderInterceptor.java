package com.linkknown.ilearning.interceptor;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenHeaderInterceptor implements Interceptor {

    // 解决线程安全问题
    public static final AtomicReference<String> TOKEN_STRING = new AtomicReference<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();

        // 设置请求头
        requestBuilder = setHeader(requestBuilder);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    private Request.Builder setHeader(Request.Builder requestBuilder) {
        String tokenString = TOKEN_STRING.get();
        if (StringUtils.isNotEmpty(tokenString)) {
            requestBuilder = requestBuilder.header("tokenString", TOKEN_STRING.get());
        }
        return requestBuilder;
    }
}
