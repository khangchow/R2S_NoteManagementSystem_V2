package com.r2s.notemanagementsystem.service;

import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Priority;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PriorityService {
    String GET_PRIORITY = "get";
    String ADD_PRIORITY = "add";
    String UPDATE_PRIORITY = "update";
    String DELETE_PRIORITY = "del";

    @GET(GET_PRIORITY)
    Call<BaseResponse> getAllPriorities(@Query("tab") String tab, @Query("email") String email);

    @GET(ADD_PRIORITY)
    Call<BaseResponse> addPriority(@Query("tab") String tab, @Query("email") String email,
                                       @Query("name") String name);

    @GET(UPDATE_PRIORITY)
    Call<BaseResponse> editPriority(@Query("tab") String tab, @Query("email") String email,
                                    @Query("name") String name, @Query("nname") String nname);

    @GET(DELETE_PRIORITY)
    Call<BaseResponse> deletePriority(@Query("tab") String tab, @Query("email") String email,
                                      @Query("name") String name);
}
