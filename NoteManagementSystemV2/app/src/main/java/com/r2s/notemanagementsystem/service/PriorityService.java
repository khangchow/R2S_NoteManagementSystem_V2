package com.r2s.notemanagementsystem.service;

import com.r2s.notemanagementsystem.constant.PriorityConstant;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Priority;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PriorityService {
    /* Doc
      Link: /get?tab= Priority&email=kylh84@gmail.com
      Method: GET
      Input: tab = Priority, email lúc đăng nhập
    */


    @GET(PriorityConstant.GET_PRIORITY)
    Call<BaseResponse> getAllPriorities(@Query("tab") String tab, @Query("email") String email);

    /*
    Ghi
     Link: /add?tab=Priority&email=kylh84@gmail.com&name=Medium
    * */
    @GET(PriorityConstant.ADD_PRIORITY)
    Call<BaseResponse> addPriority(@Query("tab") String tab, @Query("email") String email,
                                       @Query("name") Priority namePriority);

    /*
    Sửa
     Link: /update?tab=Priority&email=kylh84@gmail.com&name=Medium&nname=Low
    * */
    @GET(PriorityConstant.UPDATE_PRIORITY)
    Call<BaseResponse> editPriority(@Query("tab") String tab, @Query("email") String email,
                                    @Query("name") String name, @Query("nname") String nname);

    /**
     Xóa
      Link: del?tab=Priority&email=kylh84@gmail.com&name=Low
     */
    @GET(PriorityConstant.DELETE_PRIORITY)
    Call<BaseResponse> deletePriority(@Query("tab") String tab, @Query("email") String email,
                                      @Query("name") String name);
}
