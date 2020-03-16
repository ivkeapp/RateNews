package com.example.ratenews.crud.api.get.api;

import com.example.ratenews.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderAPI {

    @GET("fetch_single_news.php")
    Call<Result> getNews(@Query("link") String link);

}