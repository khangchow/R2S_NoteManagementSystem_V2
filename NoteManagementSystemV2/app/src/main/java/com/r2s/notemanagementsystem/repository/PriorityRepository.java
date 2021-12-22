package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.PriorityConstant;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.PriorityService;
import com.r2s.notemanagementsystem.utils.ApiClient;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityRepository {
    private PriorityService mService;
    private User mUser;

    /**
     * This method is used as constructor for PriorityRepository class
     */
    public PriorityRepository() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
        mService = ApiClient.getClient().create(PriorityService.class);
    }

    /**
     * This method loads all users
     * @param tab String
     * @param email String
     * @return RefreshLiveData
     */
    public RefreshLiveData<BaseResponse> loadAllPriorities(String tab, String email) {
        final RefreshLiveData<BaseResponse> liveData = new RefreshLiveData<>((callback -> {
            mService.getAllPriorities(tab, email).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    callback.onDataLoaded(response.body());
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("PriorityRepository", t.getMessage());
                }
            });
        }));
        return liveData;
    }

    /**
     * This method adds a new priority
     * @param priority Priority
     * @return Call
     */
    public Call<BaseResponse> addPriority(Priority priority) {
        return mService.addPriority(PriorityConstant.PRIORITY_TAB, mUser.getEmail(), priority);
    }

    /**
     * This method deletes a priority
     * @param name String
     * @return Call
     */
    public Call<BaseResponse> deletePriority(String name) {
        return mService.deletePriority(PriorityConstant.PRIORITY_TAB, mUser.getEmail(), name);
    }

    /**
     * This method updates a priority by name
     * @param name String
     * @param nname String
     * @return Call
     */
    public Call<BaseResponse> editPriority(String name, String nname) {
        return mService.editPriority(PriorityConstant.PRIORITY_TAB, mUser.getEmail(), name, nname);
    }
}
