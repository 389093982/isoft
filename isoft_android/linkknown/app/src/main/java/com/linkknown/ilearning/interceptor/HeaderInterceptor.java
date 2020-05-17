package com.linkknown.ilearning.interceptor;

import com.linkknown.ilearning.common.BaseApplication;
import com.linkknown.ilearning.util.LoginUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

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
        requestBuilder = requestBuilder.header("clientType", "app");
        String tokenString = LoginUtil.getTokenString(BaseApplication.getContext());
        if (StringUtils.isNotEmpty(tokenString)) {
            requestBuilder = requestBuilder.header("tokenString", tokenString);
        }
        return requestBuilder;
    }
}
