package com.jia0340.ems_android_app.network;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Retrofit client that holds base url for the Django database
 *
 * @author Anna Dingler
 * Created on 2/22/21
 */
public class RetrofitClientDistanceAPI {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://dev.virtualearth.net/REST/v1/Routes/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(BASE_URL);
            builder.addConverterFactory(JacksonConverterFactory.create());
            retrofit = builder.build();
        }
        return retrofit;
    }
}
