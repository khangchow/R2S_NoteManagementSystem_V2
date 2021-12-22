package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.repository.PriorityRepository;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;

import java.util.List;

import retrofit2.Call;

public class PriorityViewModel extends AndroidViewModel {

    private PriorityRepository mPriorityRepository;
    private LiveData<List<Priority>> mPriorities;

    /**
     * Constructor with 1 parameter
     * @param application Application
     */
    public PriorityViewModel(@NonNull Application application) {
        super(application);
        this.mPriorityRepository = new PriorityRepository();

//        this.mPriorities = mPriorityRepository.loadAllPriorities();
    }

    /**
     * This method returns all notes by current logged in user
     * @return LiveData List
     */
    public LiveData<List<Priority>> getAllPriorities() {
        return mPriorities;
    }

    /**
     * This method adds a new priority
     * @param priority Priority
     */
    public Call<BaseResponse> addPriority(Priority priority) {
        return mPriorityRepository.addPriority(priority);
    }

    /**
     * This method updates a priority
     * @param name String
     * @param nname String
     */
    public Call<BaseResponse> editPriority(String name, String nname) {
        return mPriorityRepository.editPriority(name, nname);
    }


    /**
     * This method deletes a priority
     * @param name String
     */
    public void deletePriority(String name) {
        mPriorityRepository.deletePriority(name);
    }
}
