package com.example.ratenews.crud.api.get.api;

import com.example.ratenews.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddVoteService {
    @FormUrlEncoded
    @POST("add_vote.php")
    Call<ServerResponse> saveVote(@Field("vote_link") String username,
                                  @Field("username") String password);
}
