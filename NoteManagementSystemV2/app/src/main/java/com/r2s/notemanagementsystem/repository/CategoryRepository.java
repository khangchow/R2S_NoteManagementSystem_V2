package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import com.r2s.notemanagementsystem.api.APIClient;
import com.r2s.notemanagementsystem.api.CategoryService;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private CategoryService mCateService;

    public static CategoryService getService() {
        return APIClient.getCate().create(CategoryService.class);
    }

    /**
     * This method is used as constructor for CategoryRepository class
     */
    public CategoryRepository() {
        mCateService = APIClient.getCate().create(CategoryService.class);
    }

    /**
     * this method returns all category
     * @return liveData
     */
    public RefreshLiveData<List<BaseResponse>> getAllCate() {
        final RefreshLiveData<List<BaseResponse>> liveData = new RefreshLiveData<>(callback -> {
           mCateService.getAllCate("cate", "email").enqueue(new Callback<BaseResponse>() {
               @Override
               public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                   callback.onDataLoaded((List<BaseResponse>) response.body());
               }

               @Override
               public void onFailure(Call<BaseResponse> call, Throwable t) {
                   Log.e("CateRepo", t.getMessage());
               }
           });
        });

        return liveData;
    }

    /**
     * Load category by id
     * @param id
     * @return category by id
     */
    public Call<BaseResponse> loadCateById(int id) {
        Call<BaseResponse> call = mCateService.getCateByID("category", "email", id);

        return call;
    }

    /**
     * This method inserts a new category
     * @param nameCategory
     * @return new Cate
     */
    public Call<BaseResponse> postCate (String nameCategory) {
        return mCateService.addCate("Category", "a", nameCategory);
    }

    /**
     * This method update category
     * @param nameCate
     * @param newCate
     * @return cate's information updated
     */
    public Call<BaseResponse> updateCate (String nameCate, String newCate) {
        return mCateService.updateCate("category", "email", nameCate, newCate);
    }

    /**
     * This method delete category
     * @param nameCategory
     * @return void
     */
    public Call<BaseResponse> deleteCate (String nameCategory) {
        return mCateService.deleteCate("category", "email", nameCategory);
    }

}
