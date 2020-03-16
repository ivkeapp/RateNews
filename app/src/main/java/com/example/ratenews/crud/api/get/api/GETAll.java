package com.example.ratenews.crud.api.get.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GETAll {

    @GET("fetch_all.php")
    Call<String> getAllNews();

}