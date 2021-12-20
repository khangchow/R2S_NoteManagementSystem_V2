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
        this.mPriorityRepository = new PriorityRepository();

//        this.mPriorities = mPriorityRepository.loadAllPriorities();
    }

    /**
     * This method returns all notes by current logged in user
     * @return LiveData List
     */
    public LiveData<List<Priority>> getAllPrioritiesByUserId() {
        return mPriorities;
    }

    /**
     * This method adds a new priority
     * @param tab String
     * @param email String
     * @param name String
     */
    public void addPriority(String tab, String email, String name) {
        mPriorityRepository.addPriority(tab, email, name);
    }

    /**
     * This method updates a priority
     * @param tab String
     * @param email String
     * @param name String
     * @param nname String
     */
    public void editPriority(String tab, String email, String name, String nname) {
        mPriorityRepository.editPriority(tab, email, name, nname);
    }


    /**
     * This method deletes a priority
     * @param tab String
     * @param email String
     * @param name String
     */
    public void deletePriority(String tab, String email, String name) {
        mPriorityRepository.deletePriority(tab, email, name);
    }
}
