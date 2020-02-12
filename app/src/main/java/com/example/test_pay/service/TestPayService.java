package com.example.test_pay.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TestPayService {

    @GET("/verify/{reference}")
    Call<ResponseBody> verifyTransaction(@Path("reference") String reference);

}
