package com.example.ratenews.crud.api;

import android.util.Log;

import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.crud.api.get.api.RegisterUserService;
import com.example.ratenews.model.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser {
    RegisterUserService mAddUser;
    public AddUser() {
        mAddUser = ApiUtils.getAPIServiceAddUser();
    }

    public void addUser(String username, String password){
        mAddUser.saveUser(username, password).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(response.isSuccessful()) {
                    ServerResponse res = response.body();
                    Log.i("adding user", "user submitted to DB. " + res.getStatus() + res.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.i("adding user", "user submitting to DB FAILED. " + t.getMessage());
            }
        });
    }

}
