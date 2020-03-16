package com.example.ratenews.crud.api.get.api;

import com.example.ratenews.model.ResultVotes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUserVotes {

    @GET("fetch_user_votes.php")
    Call<ResultVotes> getVotes(@Query("username") String username,
                               @Query("vote_link") String voteLink);

}
