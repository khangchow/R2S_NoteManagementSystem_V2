package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import com.r2s.notemanagementsystem.api.APIClient;
import com.r2s.notemanagementsystem.service.DashboardService;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Dash;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {
    private DashboardService mDashService;

    public DashboardRepository() {
        mDashService = APIClient.getDash().create(DashboardService.class);
    }

    public RefreshLiveData<List<Dash>> getDash() {
        final RefreshLiveData<List<Dash>> liveData = new RefreshLiveData<>(callback -> {
            mDashService.getDashboard("Dashboard", "kylh84@gmail.com")
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
