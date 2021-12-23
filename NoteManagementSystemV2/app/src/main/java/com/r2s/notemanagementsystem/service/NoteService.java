package com.r2s.notemanagementsystem.service;

import com.r2s.notemanagementsystem.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NoteService {
    String GET_NOTE = "get";
    String ADD_NOTE = "add";
    String UPDATE_NOTE = "update";
    String DELETE_NOTE = "del";

    @GET(GET_NOTE)
    Call<BaseResponse> getAllNotes(@Query("tab") String tab, @Query("email") String email);

    @GET(ADD_NOTE)
    Call<BaseResponse> addNote(@Query("tab") String tab, @Query("email") String email,
                                   @Query("name") String name,@Query("Priority") String priority,
                               @Query("Category") String category,@Query("Status") String status,
                               @Query("PlanDate") String planDate);

    @GET(UPDATE_NOTE)
    Call<BaseResponse> editNote(@Query("tab") String tab, @Query("email") String email,
                                    @Query("name") String name, @Query("nname") String nname,
                                @Query("Priority") String priority,  @Query("Category") String category,
                                @Query("Status") String status,  @Query("PlanDate") String planDate);

    @GET(DELETE_NOTE)
    Call<BaseResponse> deleteNote(@Query("tab") String tab, @Query("email") String email,
                                      @Query("name") String name);
}
