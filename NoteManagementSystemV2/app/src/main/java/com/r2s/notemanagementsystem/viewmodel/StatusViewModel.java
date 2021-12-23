package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.repository.StatusRepository;
import com.r2s.notemanagementsystem.repository.UserRepository;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.List;

import retrofit2.Call;

public class StatusViewModel extends AndroidViewModel {

    private final StatusRepository mStatusRepository;
    private final RefreshLiveData<List<Status>> mStatuses;

    /**
     * Constructor with 1 parameter
     * @param application Application
     */
    public StatusViewModel(@NonNull Application application) {
        super(application);
        this.mStatusRepository = new StatusRepository();
        this.mStatuses = mStatusRepository.loadAllStatuses();
    }

    /**
     * This method refresh data after changes
     */
    public void refreshData() {
        mStatuses.refresh();
    }

    /**
     * This method returns all notes by current logged in user
     * @return LiveData List
     */
    public LiveData<List<Status>> getAllStatuses() {
        return mStatuses;
    }

    /**
     * This method inserts a new status
     * @param name String
     */
    public Call<BaseResponse> addStatus(String name) {
        return mStatusRepository.addStatus(name);
    }

    /**
     * This method updates a status
     * @param name String
     * @param nname String
     */
    public Call<BaseResponse> editStatus(String name, String nname) {
        return mStatusRepository.editStatus(name, nname);
    }

    /**
     * This method deletes a status
     * @param name String
     */
    public Call<BaseResponse> deleteStatus(String name) {
        return mStatusRepository.deleteStatus(name);
    }
}
