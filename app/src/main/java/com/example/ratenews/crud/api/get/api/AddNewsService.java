package com.example.ratenews.crud.api.get.api;

import com.example.ratenews.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddNewsService {
    @FormUrlEncoded
    @POST("add_news.php")
    Call<ServerResponse> savePost(@Field("title") String title,
                                  @Field("description") String desc,
                                  @Field("link") String link,
                                  @Field("date") String date,
                                  @Field("image") String image,
                                  @Field("positive_votes") Integer positiveVotes,
                                  @Field("negative_votes") Integer negativeVotes,
                                  @Field("paper") String paper);
}