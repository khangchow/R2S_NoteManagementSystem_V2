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

    public LiveData<List<Note>> getAllNotes() {
        return mNotes;
    }

    public Call<BaseResponse> addNote(String name, String priority, String category, String status, String planDate ) {
        return mNoteRepository.addNote(name, priority, category, status, planDate);
    }

    public Call<BaseResponse> editNote(String name, String nname, String priority, String category,
                                       String status, String planDate) {
        return mNoteRepository.editNote(name, nname, priority,category,
                status, planDate);
    }

    public Call<BaseResponse> deleteNote(String name) {
        return mNoteRepository.deleteNote(name);
    }
}
