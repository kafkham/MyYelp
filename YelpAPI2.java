package com.example.myyelp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface YelpAPI2 {

    @GET
    Call<Response2> getFav(@Url String id);
}