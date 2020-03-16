package com.example.ratenews.crud.api;

import android.util.Log;

import com.example.ratenews.crud.api.get.api.AddVoteService;
import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.model.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVote {
    AddVoteService mAddVote;
    public AddVote() {
        mAddVote = ApiUtils.getAPIServiceAddVote();
    }

    public void addVote(String voteLink, String username){
        mAddVote.saveVote(voteLink, username).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(response.isSuccessful()) {
                    ServerResponse res = response.body();
                    Log.i("adding vote", "vote submitted to DB. " + res.getStatus() + res.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.i("adding vote", "vote submitting to DB FAILED. " + t.getMessage());
            }
        });
    }

}
