package com.example.ratenews.crud.api;

import android.util.Log;

import com.example.ratenews.crud.api.get.api.AddNewsService;
import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.crud.api.get.api.UpdateAPIServiceNegative;
import com.example.ratenews.crud.api.get.api.UpdateAPIServicePositive;
import com.example.ratenews.model.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNews {
    private AddNewsService mAddNewsService;
    private UpdateAPIServicePositive uAPIServicePositive;
    private UpdateAPIServiceNegative uAPIServiceNegative;

    public AddNews() {
        mAddNewsService = ApiUtils.getAPIService();
        uAPIServicePositive = ApiUtils.getAPIServiceUpdatePositive();
        uAPIServiceNegative = ApiUtils.getAPIServiceUpdateNegative();

    }

    public void addNews(String title, String desc, String link, String date, String img, int positive, int negative, String paper) {
        mAddNewsService.savePost(title, desc, link, date, img, positive, negative, paper).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                //Toast.makeText(MainActivity.this, "usao", Toast.LENGTH_LONG).show();
                if(response.isSuccessful()) {
                    ServerResponse res = response.body();
                    Log.i("posting", "post submitted to API. " + "status: " + res.getStatus() + " response msg: " + res.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("posting", "Unable to submit post to API. " + t.getMessage());
            }
        });
    }

    public void updateNewsVotePositive(int positive, String link) {
        uAPIServicePositive.updatePost(positive, link).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                //Toast.makeText(MainActivity.this, "usao", Toast.LENGTH_LONG).show();
                if(response.isSuccessful()) {
                    ServerResponse res = response.body();
                    //Log.i("post2", "update submitted to API. " + res.getStatus() + res.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("post2", "Unable to submit update to API." + t.getMessage());
            }
        });
    }

    public void updateNewsVoteNegative(int negative, String link) {
        uAPIServiceNegative.updatePostNegative(negative, link).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                //Toast.makeText(MainActivity.this, "usao", Toast.LENGTH_LONG).show();
                if(response.isSuccessful()) {
                    ServerResponse res = response.body();
                    //Log.i("post2", "update submitted to API. " + res.getStatus() + res.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("post2", "Unable to submit update to API." + t.getMessage());
            }
        });
    }



}
