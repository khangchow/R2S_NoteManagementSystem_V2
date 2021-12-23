package com.r2s.notemanagementsystem.service;

import com.r2s.notemanagementsystem.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardService {
    String GET_DASHBOARD = "get";

    @GET(GET_DASHBOARD)
    Call<BaseResponse> getDashboard(@Query("tab") String dashboard, @Query("email") String email);
}
