package com.r2s.notemanagementsystem.services;

import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {
    final String LOGIN = "login";
    final String SIGNUP = "signup";

    @GET(Constants.BASE_URL + LOGIN)
    Call<BaseResponse> login(@Query("email") String email, @Query("pass") String pass);

    @GET(Constants.BASE_URL + SIGNUP)
    Call<BaseResponse> signUp(@Query("email") String email, @Query("pass") String pass
            , @Query("firstname") String firstname, @Query("lastname") String lastname);

    @GET(UserConstant.GET_USER)
    Call<BaseResponse> getAllUser(@Query("tab") String tab, @Query("email") String email);

    @GET(UserConstant.UPDATE_USER)
    Call<BaseResponse> updateUser(@Query("tab") String tab, @Query("email") String email,
                                  @Query("firstname") String firstname,
                                  @Query("lastname") String lastname);
}
