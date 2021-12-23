package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.repository.CategoryRepository;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.List;

import retrofit2.Call;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository mCateRepo;
    private RefreshLiveData<List<Category>> mCateList;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        init();
    }

    /**
     * init view
     */
    private void init() {
        this.mCateRepo = new CategoryRepository();
        this.mCateList = mCateRepo.getAllCate();
    }

    /**
     * refresh data
     */
    public void refreshData() {
        mCateList.refresh();
    }

    /**
     * return all category
     * @return list of category
     */
    public LiveData<List<Category>> getCateById() {
        return mCateList;
    }

    /**
     * insert new Category
     * @param category
     */
    public Call<BaseResponse> insertCate(String category) {
        return mCateRepo.postCate(category);
    }

    /**
     * update category
     * @param category
     */
    public Call<BaseResponse> updateCate(String category, String newCate) {
        return mCateRepo.updateCate(category, newCate);
    }

    /**
     * delete category
     * @param category
     */
    public Call<BaseResponse> deleteCate(String category) {
        return mCateRepo.deleteCate(category );
    }
}
