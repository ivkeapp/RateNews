package com.example.ratenews.crud.api.get.api;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://ivanzarkovic.com/newsAPI/";

    public static AddNewsService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(AddNewsService.class);
    }

    public static JSONPlaceHolderAPI getAPIServiceFetch() {

        return RetrofitClient.getClient(BASE_URL).create(JSONPlaceHolderAPI.class);
    }

    public static UpdateAPIServicePositive getAPIServiceUpdatePositive() {

        return RetrofitClient.getClient(BASE_URL).create(UpdateAPIServicePositive.class);
    }

    public static UpdateAPIServiceNegative getAPIServiceUpdateNegative() {

        return RetrofitClient.getClient(BASE_URL).create(UpdateAPIServiceNegative.class);
    }

    public static GETAll getAPIServiceFetchAll() {

        return RetrofitClient.getClient(BASE_URL).create(GETAll.class);
    }

    public static GetAllLies getAPIServiceFetchAllLies() {

        return RetrofitClient.getClient(BASE_URL).create(GetAllLies.class);
    }

    public static RegisterUserService getAPIServiceAddUser(){

        return RetrofitClient.getClient(BASE_URL).create(RegisterUserService.class);
    }

    public static GetUserVotes getAPIServiceUserVotes(){

        return RetrofitClient.getClient(BASE_URL).create(GetUserVotes.class);
    }

    public static AddVoteService getAPIServiceAddVote(){

        return RetrofitClient.getClient(BASE_URL).create(AddVoteService.class);
    }

}