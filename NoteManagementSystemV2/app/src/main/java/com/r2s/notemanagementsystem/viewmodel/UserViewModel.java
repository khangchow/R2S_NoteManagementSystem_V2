package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.repository.UserRepository;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;

import retrofit2.Call;

public class UserViewModel extends AndroidViewModel {
    private UserRepository mUserRepo;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.mUserRepo = new UserRepository(application);

        if (AppPrefsUtils.getString(UserConstant.KEY_USER_DATA) != null) {
//            this.mUser = mUserRepo.getUserById(
//                    new Gson().fromJson(
//                            AppPrefsUtils.getString(UserConstant.KEY_USER_DATA)
//                            , User.class).getUid());
        }
    }

    public Call<BaseResponse> login(User user){
        return mUserRepo.login(user.getEmail(), user.getPassword());
    }

    public  void updateUser(String tab, String email, String firstname, String lastname){
        mUserRepo.editUser(tab, email, firstname, lastname);
    }

    public Call<BaseResponse> signUp(User user){
        return mUserRepo.signUp(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName());
    }
}
