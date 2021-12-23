package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import androidx.annotation.NonNull;

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
    private static PriorityService mPriorityService;
    private User mUser;

    /**
     * This method is used as constructor for PriorityRepository class
     */
    public PriorityRepository() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
        mPriorityService = ApiClient.getClient().create(PriorityService.class);
    }

    /**
     * This method loads all priorities
     * @return RefreshLiveData
     */
    public RefreshLiveData<List<Priority>> loadAllPriorities() {
        final RefreshLiveData<List<Priority>> liveData = new RefreshLiveData<>((callback) -> {
            mPriorityService.getAllPriorities(PriorityConstant.PRIORITY_TAB,
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
        return mPriorityService.addPriority(PriorityConstant.PRIORITY_TAB, mUser.getEmail(), name);
    }

    /**
     * This method deletes a priority
     * @param name String
     * @return Call
     */
    public Call<BaseResponse> deletePriority(String name) {
        return mPriorityService.deletePriority(PriorityConstant.PRIORITY_TAB, mUser.getEmail(), name);
    }

    /**
     * This method updates a priority by name
     * @param name String
     * @param nname String
     * @return Call
     */
    public Call<BaseResponse> editPriority(String name, String nname) {
        return mPriorityService.editPriority(PriorityConstant.PRIORITY_TAB, mUser.getEmail(), name, nname);
    }

    public static PriorityService getService() {
        mPriorityService = ApiClient.getClient().create(PriorityService.class);
        return mPriorityService;
    }
}
