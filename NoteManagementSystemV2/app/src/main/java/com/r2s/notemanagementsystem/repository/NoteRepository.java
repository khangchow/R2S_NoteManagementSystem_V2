package com.r2s.notemanagementsystem.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.NoteConstant;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Note;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.NoteService;
import com.r2s.notemanagementsystem.utils.ApiClient;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteRepository {

    private static NoteService mNoteService;
    private User mUser;

    /**
     * Constructor
     */
    public NoteRepository() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
        mNoteService = ApiClient.getClient().create(NoteService.class);
    }

    /**
     * This method will load all data from api
     *
     * @return livedata
     */
    public RefreshLiveData<List<Note>> loadAllNotes() {
        final RefreshLiveData<List<Note>> liveData = new RefreshLiveData<>((callback) -> {
            mNoteService.getAllNotes(NoteConstant.NOTE_TAB,
                    mUser.getEmail()).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse> call,
                                       @NonNull Response<BaseResponse> response) {
                    List<Note> notes = new ArrayList<>();
                    assert response.body() != null;

                    for (List<String> note : response.body().getData()) {
                        notes.add(new Note(note.get(0), note.get(1), note.get(2), note.get(3),
                                note.get(4), note.get(5)));
                    }

                    callback.onDataLoaded(notes);
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("NoteRepository", t.getMessage());
                }
            });
        });

        return liveData;
    }

    /**
     * This method will add note to api
     *
     * @param name     name of note
     * @param priority name of priority
     * @param category name of category
     * @param status   name of status
     * @param planDate plan date
     * @return call
     */
    public Call<BaseResponse> addNote(String name, String priority, String category, String status,
                                      String planDate) {
        return mNoteService.addNote(NoteConstant.NOTE_TAB, mUser.getEmail(), name, priority,
                category, status, planDate);
    }

    /**
     * This method will delete a note by name
     *
     * @param name of note to delete
     * @return call
     */
    public Call<BaseResponse> deleteNote(String name) {
        return mNoteService.deleteNote(NoteConstant.NOTE_TAB, mUser.getEmail(), name);
    }

    /**
     * This method will edit note by parameters
     *
     * @param name     old
     * @param nname    new name
     * @param priority new priority
     * @param category new category
     * @param status   new status
     * @param planDate new plan date
     * @return call
     */
    public Call<BaseResponse> editNote(String name, String nname, String priority, String category,
                                       String status, String planDate) {
        return mNoteService.editNote(NoteConstant.NOTE_TAB, mUser.getEmail(), name, nname, priority,
                category, status, planDate);
    }

    /**
     * This method will get a note service
     *
     * @return note service
     */
    public static NoteService getService() {
        mNoteService = ApiClient.getClient().create(NoteService.class);
        return mNoteService;
    }
}
