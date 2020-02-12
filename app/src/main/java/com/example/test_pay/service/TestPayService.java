package com.example.test_pay.service;

import com.example.test_pay.model.AuthorizationObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TestPayService {

    @GET("/new-access-code")
    Call<String> initializeTransaction();

    @GET("/verify/{reference}")
    Call<ResponseBody> verifyTransaction(@Path("reference") String reference);

}
