package com.r2s.notemanagementsystem.service;

import com.r2s.notemanagementsystem.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StatusService {
    String GET_STATUS = "get";
    String ADD_STATUS = "add";
    String UPDATE_STATUS = "update";
    String DELETE_STATUS = "del";

    @GET(GET_STATUS)
    Call<BaseResponse> getAllStatuses(@Query("tab") String tab, @Query("email") String email);

    @GET(ADD_STATUS)
    Call<BaseResponse> addStatus(@Query("tab") String tab, @Query("email") String email,
                                 @Query("name") String name);

    @GET(UPDATE_STATUS)
    Call<BaseResponse> updateStatus(@Query("tab") String tab, @Query("email") String email,
                                    @Query("name") String name, @Query("nname") String nname);

    @GET(DELETE_STATUS)
    Call<BaseResponse> deleteStatus(@Query("tab") String tab, @Query("email") String email,
                                    @Query("name") String name);
}