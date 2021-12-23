package com.r2s.notemanagementsystem.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.r2s.notemanagementsystem.api.UserService;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.ApiClient;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    Context context;
    UserService mUserService;

    private UserService mUserService;


    public UserRepository(Context context) {
        mUserService = ApiClient.getClient().create(UserService.class);

    }

    public Call<BaseResponse> editUser(String tab, String email, String firstname, String lastname) {
        return mUserService.updateUser(tab, email, firstname, lastname);
    }

    public Call<BaseResponse> login(String email, String pass){
        return mUserService.login(email, pass);
    }

    public Call<BaseResponse> signUp(String email, String pass, String firstName, String lastName){
        return mUserService.signUp(email, pass, firstName, lastName);
    }
}
