package com.r2s.notemanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Note;
import com.r2s.notemanagementsystem.repository.NoteRepository;
import com.r2s.notemanagementsystem.utils.RefreshLiveData;

import java.util.List;

import retrofit2.Call;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository mNoteRepository;
    private final RefreshLiveData<List<Note>> mNotes;

    /**
     * Constructor with 1 parameter
     *
     * @param application Application
     */
    public NoteViewModel(@NonNull Application application) {
        super(application);
        this.mNoteRepository = new NoteRepository();
        this.mNotes = mNoteRepository.loadAllNotes();
    }

    /**
     * This method refresh data after changes
     */
    public void refreshData() {
        mNotes.refresh();
    }

    /**
     * This method will get data from note repository
     *
     * @return list of note
     */
    public LiveData<List<Note>> getAllNotes() {
        return mNotes;
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
        return mNoteRepository.addNote(name, priority, category, status, planDate);
    }

    /**
     * This method will edit note by parameters
     *
     * @param name     old name
     * @param nname    new name
     * @param priority new priority
     * @param category new category
     * @param status   new status
     * @param planDate new plan date
     * @return call
     */
    public Call<BaseResponse> editNote(String name, String nname, String priority, String category,
                                       String status, String planDate) {
        return mNoteRepository.editNote(name, nname, priority, category,
                status, planDate);
    }

    /**
     * This method will get a note service
     *
     * @return note service
     */
    public Call<BaseResponse> deleteNote(String name) {
        return mNoteRepository.deleteNote(name);
    }
}
