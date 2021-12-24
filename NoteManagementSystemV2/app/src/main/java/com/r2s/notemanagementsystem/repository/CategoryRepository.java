package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.CategoryService;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.utils.ApiClient;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private CategoryService mCateService;
    private User mUser  = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA)
            , User.class);

    /**
     * This method is used as constructor for CategoryRepository class
     */
    public CategoryRepository() {
        mCateService = ApiClient.getClient().create(CategoryService.class);
    }

    /**
     * this method returns all category
     * @return liveData
     */
    public RefreshLiveData<List<Category>> getAllCate() {
        final RefreshLiveData<List<Category>> liveData = new RefreshLiveData<>(callback -> {
            mCateService.getAllCate("Category", mUser.getEmail())
                    .enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    List<Category> categories = new ArrayList<>();
                    for (List<String> category: response.body().getData()) {
                        categories.add(new Category(category.get(0), category.get(1)));
                    }
                    callback.onDataLoaded(categories);
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
        Call<BaseResponse> call = mCateService.getCateByID("Category", mUser.getEmail(), id);

        return call;
    }

    /**
     * This method inserts a new category
     * @param nameCategory
     * @return new Cate
     */
    public Call<BaseResponse> postCate (String nameCategory) {
        return mCateService.addCate("Category", mUser.getEmail(), nameCategory);
    }

    /**
     * This method update category
     * @param nameCate
     * @param newCate
     * @return cate's information updated
     */
    public Call<BaseResponse> updateCate (String nameCate, String newCate) {
        return mCateService.updateCate("Category", mUser.getEmail(), nameCate, newCate);
    }

    /**
     * This method delete category
     * @param nameCategory
     * @return void
     */
    public Call<BaseResponse> deleteCate (String nameCategory) {
        return mCateService.deleteCate("Category", mUser.getEmail(), nameCategory);
    }
}
