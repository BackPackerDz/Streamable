package com.squalala.streamable.data.api;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Fayçal KADDOURI on 08/09/17.
 */

public class BasicAuthInterceptor implements Interceptor {

    public BasicAuthInterceptor() {}

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", "Basic c3VwZXJib2RldEBnbWFpbC5jb206MTIzNDU2Nzg5MA==").build();
        return chain.proceed(authenticatedRequest);
    }

}