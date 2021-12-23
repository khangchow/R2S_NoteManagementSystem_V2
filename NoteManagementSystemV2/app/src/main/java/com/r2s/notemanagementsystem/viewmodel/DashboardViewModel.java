package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.r2s.notemanagementsystem.model.Dash;
import com.r2s.notemanagementsystem.repository.DashboardRepository;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {
    private DashboardRepository mDashRepo;
    private RefreshLiveData<List<Dash>> mDashList;

    public DashboardViewModel(@NonNull Application application) {
        super(application);

        initView();
    }

    private void initView() {
        this.mDashRepo = new DashboardRepository();
        this.mDashList = mDashRepo.getDash();
    }

    public void refreshDataDash() {
        mDashList.refresh();
    }

    public LiveData<List<Dash>> getDashById() {
        return mDashList;
    }
}
