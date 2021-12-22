package com.r2s.notemanagementsystem.api;

import com.r2s.notemanagementsystem.constant.PriorityConstant;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {

    @GET(UserConstant.GET_USER)
    Call<BaseResponse> getAllUser(@Query("tab") String tab, @Query("email") String email);

    @GET(UserConstant.UPDATE_USER)
    Call<BaseResponse> updateUser(@Query("tab") String tab, @Query("email") String email,
                                  @Query("firstname") String firstname,
                                  @Query("lastname") String lastname);
}
