package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

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

import java.util.ArrayList;
import java.util.List;

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
     * @return RefreshLiveData
     */
    public RefreshLiveData<List<Priority>> loadAllPriorities() {
        final RefreshLiveData<List<Priority>> liveData = new RefreshLiveData<>((callback) -> {
            mService.getAllPriorities(PriorityConstant.PRIORITY_TAB,
                    mUser.getEmail()).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse> call,
                                       @NonNull Response<BaseResponse> response) {
                    List<Priority> mPriorites = new ArrayList<>();
                    assert response.body() != null;
                    for (List<String> priority : response.body().getData()) {
                        mPriorites.add(new Priority(priority.get(0), priority.get(1), priority.get(2)));
                    }
                    callback.onDataLoaded(mPriorites);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("PrioriryRepository", t.getMessage());
                }
            });
        });
        return liveData;
    }

    /**
     * This method adds a new priority
     * @param name String
     * @return Call
     */
    public Call<BaseResponse> addPriority(String name) {
        return mService.addPriority(PriorityConstant.PRIORITY_TAB, mUser.getEmail(), name);
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
