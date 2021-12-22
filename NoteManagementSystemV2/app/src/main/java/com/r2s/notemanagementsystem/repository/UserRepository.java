package com.r2s.notemanagementsystem.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.services.UserService;
import com.r2s.notemanagementsystem.utils.ApiClient;

import java.util.concurrent.Executors;

import retrofit2.Call;

public class UserRepository {
    Context context;
    UserService mUserService;

    public static UserService getService() {
        return ApiClient.getClient().create(UserService.class);
    }

    public UserRepository(Context context) {
        this.context = context;
        mUserService = getService();
    }

    public void insertUser(User user) {

    }

    public void updateUser(User user) {

    }

    public Call<BaseResponse> login(String email, String pass){
        return mUserService.login(email, pass);
    }

    public Call<BaseResponse> signUp(String email, String pass, String firstName, String lastName){
        return mUserService.signUp(email, pass, firstName, lastName);
    }
}
