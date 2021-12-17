package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository mCateRepo;
    private LiveData<List<Category>> mCates;

    /**
     * Constructor with 1 param
     * @param application
     */
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        this.mCateRepo = new CategoryRepository(application);
        this.mCates = mCateRepo.getAllCate();
    }

    /**
     * return all category
     * @return
     */
    public LiveData<List<Category>> loadAllCate() {
        return mCates;
    }

    /**
     * insert new Category
     * @param category
     */
    public void insertCate(Category category) {
        mCateRepo.insert(category);
    }

    /**
     * update category
     * @param category
     */
    public void updateCate(Category category) {
        mCateRepo.update(category);
    }

    /**
     * delete category
     * @param category
     */
    public void deleteCate(Category category) {
        mCateRepo.delete(category);
    }
}
