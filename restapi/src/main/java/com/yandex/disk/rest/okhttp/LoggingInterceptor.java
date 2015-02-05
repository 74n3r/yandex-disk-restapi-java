package com.yandex.disk.rest.okhttp;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yandex.disk.rest.Log;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.d(String.format("Sending request %s %s%n on %s%n%s",
                request.method(), request.url(), chain.connection(), request.headers()));

        Response src = chain.proceed(request);
        Response response = src.newBuilder()
                .addHeader("Y-Code", String.valueOf(src.code()))
                .addHeader("Y-Message", src.message())
                .build();

        long t2 = System.nanoTime();
        Log.d(String.format("Received response for %s%n in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
