package com.example.ratenews.crud.api.get.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetAllLies {

    @GET("fetch_all_negative.php")
    Call<String> getAllNews();

}
