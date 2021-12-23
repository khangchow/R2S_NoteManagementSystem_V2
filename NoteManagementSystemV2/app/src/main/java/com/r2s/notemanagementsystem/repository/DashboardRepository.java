package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import com.r2s.notemanagementsystem.api.APIClient;
import com.r2s.notemanagementsystem.api.DashboardService;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;
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

    public RefreshLiveData<List<Category>> getAllCate() {
        final RefreshLiveData<List<Category>> liveData = new RefreshLiveData<>(callback -> {
            mDashService.getDashboard("Dashboard", "a").enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    List<Category> categories = new ArrayList<>();
                    for (List<String> category: response.body().getData()) {
                        categories.add(new Category(category.get(0), category.get(1)));
                    }
                    callback.onDataLoaded(categories);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("CateRepo", t.getMessage());
                }
            });
        });

        return liveData;
    }
}
