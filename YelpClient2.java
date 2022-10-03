package com.example.myyelp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpClient2 {
        public YelpAPI2 build(){

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> chain.proceed(chain
                            .request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer l4WpqWQhdFvZiPCvm4_kymq6VzSZAN2UVVgmnuSESoZH7Aj_mtaQnnUHSoAdSI1kXakez6fEwNu4noPSJ5REEe4PFGGjIjlyl5ZiEVffMh-c1UuL0oWMik14baeqYnYx")
                            .build())).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.yelp.com/v3/businesses/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            return retrofit.create(YelpAPI2.class);
        }
    }
