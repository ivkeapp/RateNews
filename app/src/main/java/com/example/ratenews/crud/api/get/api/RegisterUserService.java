package com.example.ratenews.crud.api.get.api;

import com.example.ratenews.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterUserService {
    @FormUrlEncoded
    @POST("add_user.php")
    Call<ServerResponse> saveUser(@Field("username") String username,
                                  @Field("password") String password);
}
