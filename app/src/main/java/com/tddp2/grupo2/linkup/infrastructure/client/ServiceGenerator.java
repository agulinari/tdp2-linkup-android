package com.tddp2.grupo2.linkup.infrastructure.client;

import com.tddp2.grupo2.linkup.utils.Configuration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ServiceGenerator {

    public static Retrofit defaultRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(Configuration.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        httpClient.connectTimeout(Configuration.CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        httpClient.writeTimeout(Configuration.CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
                        .build();

                return chain.proceed(request);
            }
        });

        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        return new Retrofit.Builder()
                .baseUrl(Configuration.DEFAULT_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

}
