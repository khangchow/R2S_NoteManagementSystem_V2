package com.r2s.notemanagementsystem.service;

import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {
    String GET_CATE = "get";
    String ADD_CATE = "add";
    String UPDATE_CATE = "update";
    String DELETE_CATE = "del";

    @GET(GET_CATE)
    Call<BaseResponse> getAllCate(@Query("tab") String category, @Query("email") String email);

    @GET(GET_CATE)
    Call<BaseResponse> getCateByID(@Query("tab") String category, @Query("email") String email
                                    ,@Query("id") int id);

    @GET(ADD_CATE)
    Call<BaseResponse> addCate(@Query("tab") String category, @Query("email") String email,
                               @Query("name") String nameCate);

    @GET(UPDATE_CATE)
    Call<BaseResponse> updateCate(@Query("tab") String category, @Query("email") String email,
                                  @Query("name") String nameCate, @Query("nname") String nNameCate);

    @GET(DELETE_CATE)
    Call<BaseResponse> deleteCate(@Query("tab") String category, @Query("email") String email,
                                  @Query("name") String nameCate);
}
