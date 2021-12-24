package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.Dash;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.DashboardService;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.utils.ApiClient;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {
    private DashboardService mDashService;
    private User mUser  = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA)
            , User.class);

    public DashboardRepository() {
        mDashService = ApiClient.getClient().create(DashboardService.class);
    }

    public RefreshLiveData<List<Dash>> getDash() {
        final RefreshLiveData<List<Dash>> liveData = new RefreshLiveData<>(callback -> {
            mDashService.getDashboard("Dashboard", mUser.getEmail())
                    .enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    List<Dash> dashes = new ArrayList<>();
                    for (List<String> list: response.body().getData()) {
                        dashes.add(new Dash(list.get(0), list.get(1)));
                    }
                    callback.onDataLoaded(dashes);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("DashRepo", t.getMessage());
                }
            });
        });

        return liveData;
    }
}
