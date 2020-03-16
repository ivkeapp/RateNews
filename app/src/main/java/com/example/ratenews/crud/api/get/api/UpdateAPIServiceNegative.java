package com.example.ratenews.crud.api.get.api;

import com.example.ratenews.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateAPIServiceNegative {
    @FormUrlEncoded
    @POST("update_news_negative.php")
    Call<ServerResponse> updatePostNegative(@Field("negative_votes") Integer positiveVotes,
                                    @Field("link") String link);
}
