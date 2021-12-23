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

    private MutableLiveData<Boolean> _isUpdated = new MutableLiveData<>();

    private LiveData<Boolean> isUpdated = _isUpdated;

    public UserViewModel(@NonNull Application application) {
        super(application);

        this.mUserRepo = new UserRepository();

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

    public Call<BaseResponse> updateUser(String tab, String email
            , String nemail, String firstname, String lastname){
       return mUserRepo.editUser(tab, email, nemail, firstname, lastname);
    }

    public Call<BaseResponse> signUp(User user){
        return mUserRepo.signUp(user.getEmail()
                , user.getPassword(), user.getFirstName(), user.getLastName());
    }

    public Call<BaseResponse> changePassword(String tab, String email, String pass, String npass){
        return mUserRepo.changePassword(tab, email, pass, npass);
    }

    public void updatedUserData() {
        _isUpdated.postValue(true);
    }

    public LiveData<Boolean> isUserUpdated() {
        return isUpdated;
    }
}
