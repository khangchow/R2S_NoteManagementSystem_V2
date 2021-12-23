package com.r2s.notemanagementsystem.repository;

import android.app.Application;

import com.r2s.notemanagementsystem.utils.APIClient;
import com.r2s.notemanagementsystem.service.CategoryService;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.CategoryService;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;

import java.util.List;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private CategoryService mCateService;

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
