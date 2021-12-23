package com.r2s.notemanagementsystem.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.StatusConstant;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.StatusService;
import com.r2s.notemanagementsystem.utils.ApiClient;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusRepository {
    private static StatusService mStatusService;
    private LiveData<List<Status>> mStatuses;
    private User mUser;

    /**
     * This method is used as constructor for StatusRepository class
     */
    public StatusRepository() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
        mStatusService = ApiClient.getClient().create(StatusService.class);
    }

    /**
     * This method loads all statuses
     * @return RefreshLiveData
     */
    public RefreshLiveData<List<Status>> loadAllStatuses() {
        final RefreshLiveData<List<Status>> liveData = new RefreshLiveData<>((callback) -> {
            mStatusService.getAllStatuses(StatusConstant.STATUS_TAB, mUser.getEmail())
            .enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    List<Status> mStatuses = new ArrayList<>();
                    assert response.body() != null;
                    for (List<String> status : response.body().getData()) {
                        mStatuses.add(new Status(status.get(0), status.get(1), status.get(2)));
                    }
                    callback.onDataLoaded(mStatuses);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("StatusRepository", t.getMessage());
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
    public Call<BaseResponse> addStatus(String name) {
        return mStatusService.addStatus(StatusConstant.STATUS_TAB, mUser.getEmail(), name);
    }

    /**
     * This method deletes a status
     * @param name String
     * @return Call
     */
    public Call<BaseResponse> deleteStatus(String name) {
        return mStatusService.deleteStatus(StatusConstant.STATUS_TAB, mUser.getEmail(), name);
    }

    /**
     * This method updates a status
     * @param name String
     * @param nname String
     * @return Call
     */
    public Call<BaseResponse> editStatus(String name, String nname) {
        return mStatusService.updateStatus(StatusConstant.STATUS_TAB, mUser.getEmail(), name,
                nname);
    }

    public static StatusService getService() {
        mStatusService = ApiClient.getClient().create(StatusService.class);
        return mStatusService;
    }
}
