package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.repository.PriorityRepository;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;

import java.util.List;

public class PriorityViewModel extends AndroidViewModel {

    private PriorityRepository mPriorityRepository;
    private LiveData<List<Priority>> mPriorities;

    /**
     * Constructor with 1 parameter
     * @param application Application
     */
    public PriorityViewModel(@NonNull Application application) {
        super(application);
        this.mPriorityRepository = new PriorityRepository(application);

        this.mPriorities = mPriorityRepository.getAllPrioritiesByUserId();
    }

    /**
     * This method returns all notes by current logged in user
     * @return LiveData List
     */
    public LiveData<List<Priority>> getAllPrioritiesByUserId() {
        return mPriorities;
    }

    /**
     * This method inserts a new priority
     * @param priority Priority
     */
    public void insertPriority(Priority priority) {
        mPriorityRepository.insertPriority(priority);
    }

    /**
     * This method updates a priority by id
     * @param priority Priority
     */
    public void updatePriority(Priority priority) {
        mPriorityRepository.updatePriority(priority);
    }

    /**
     * This method deletes a priority
     * @param priority Priority
     */
    public void deletePriority(Priority priority) {
        mPriorityRepository.deletePriority(priority);
    }
}
