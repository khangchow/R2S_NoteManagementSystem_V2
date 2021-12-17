package com.r2s.notemanagementsystem.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;

import java.util.List;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private LiveData<List<Category>> mCates;
    private User mUser;

    /**
     * This method is used as constructor for CategoryRepository class
     * @param application
     */
    public CategoryRepository(Application application) {

    }

    /**
     * his method returns all category
     * @return
     */
    public LiveData<List<Category>> getAllCate() {
        return mCates;
    }

    /**
     * This method inserts a new category
     * @param category
     */
    public void insert (Category category) {

    }

    /**
     * This method update category
     * @param category
     */
    public void update (Category category) {

    }

    /**
     * This method delete category
     * @param category
     */
    public void delete (Category category) {

    }

}
