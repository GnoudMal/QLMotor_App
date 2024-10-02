package com.vdsl.thithu_and103;

import static com.vdsl.thithu_and103.APIServices.DOMAIN;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private APIServices requestInterface;

    public HttpRequest() {
        requestInterface = new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APIServices.class);
    }

    public HttpRequest(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                return chain.proceed(request);
            }
        });
        requestInterface = new Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(APIServices.class);
    }

    public APIServices callAPI() {
        return requestInterface;
    }
}
