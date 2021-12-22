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
    private MutableLiveData<User> _mUser = new MutableLiveData<>();
    private LiveData<User> mUser = _mUser;

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

    public void insertUser(User user) {

        mUserRepo.insertUser(user);
    }

//    public void updateUser(User user) {
//        mUserRepo.updateUser(user);
//
//        mUser = mUserRepo.getUserById(user.getUid());
//    }
//
//    public LiveData<Integer> count(String email){ return mUserRepo.count(email); }
//
//    public LiveData<User> login(String email, String pass){return mUserRepo.login(email, pass);}
//
//    public LiveData<User> getUser() {
//        return mUser;
//    }

    public Call<BaseResponse> login(User user){
        return mUserRepo.login(user.getEmail(), user.getPassword());
    }
}
