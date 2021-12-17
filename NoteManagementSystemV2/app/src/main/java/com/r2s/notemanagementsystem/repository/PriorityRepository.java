package com.r2s.notemanagementsystem.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;

import java.util.List;
import java.util.concurrent.Executors;

public class PriorityRepository {

    private LiveData<List<Priority>> mPriorities;
    private User mUser;

    /**
     * This method is used as constructor for PriorityRepository class
     * @param context Context
     */
    public PriorityRepository(Context context) {

    }

    /**
     * This method returns all priorities by current logged in user
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

    }

    /**
     * This method updates a priority by id
     * @param priority Priority
     */
    public void updatePriority(Priority priority) {

    }

    /**
     * This method deletes a priority
     * @param priority Priority
     */
    public void deletePriority(Priority priority) {

    }
}
