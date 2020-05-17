package com.linkknown.ilearning.interceptor;

import android.content.Intent;
import android.util.Log;

import com.linkknown.ilearning.common.BaseApplication;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class HttpLogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        logRequest(request);

        Response response = chain.proceed(request);

        logResponse(response);

        return response;
    }

    // 打印响应信息日志
    private void logResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            Charset charset = Charset.forName("UTF8");
            String rBody = buffer.clone().readString(charset);
            Log.i("http_response ==>", rBody);
        }

        if (response.code() == 401) {
            // 触发自动登录
            Intent intent = new Intent();
            intent.setAction("com.linkknown.ilearning.broadcast.AutoLoginAction");
            BaseApplication.getContext().sendBroadcast(intent);
        }
    }

    // 打印请求信息日志
    private void logRequest(Request request) {
        Log.i("http_url ==>", request.url().toString());
        Log.i("http_method ==>", request.method());
        Headers headers = request.headers();
        for (String headerName : request.headers().names()){
            Log.i("http_header ==>", request.header(headerName));
        }
    }
}
