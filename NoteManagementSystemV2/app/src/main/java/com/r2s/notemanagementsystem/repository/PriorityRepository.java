package com.r2s.notemanagementsystem.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.PriorityService;
import com.r2s.notemanagementsystem.utils.ApiClient;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityRepository {
    private PriorityService mService;

    /**
     * This method is used as constructor for PriorityRepository class
     */
    public PriorityRepository() {
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
     * @param tab String
     * @param email String
     * @param name String
     * @return Call
     */
    public Call<BaseResponse> addPriority(String tab, String email, String name) {
        return mService.addPriority(tab, email, name);
    }

    /**
     * This method deletes a priority
     * @param tab String
     * @param email String
     * @param name String
     * @return Call
     */
    public Call<BaseResponse> deletePriority(String tab, String email, String name) {
        return mService.deletePriority(tab, email, name);
    }

    /**
     * This method updates a priority by name
     * @param tab String
     * @param email String
     * @param name String
     * @param nname String
     * @return Call
     */
    public Call<BaseResponse> editPriority(String tab, String email, String name, String nname) {
        return mService.editPriority(tab, email, name, nname);
    }
}
