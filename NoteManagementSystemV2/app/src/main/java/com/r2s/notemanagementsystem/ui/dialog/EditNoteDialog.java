package com.r2s.notemanagementsystem.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.NoteAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogEditNoteBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Note;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditNoteDialog extends DialogFragment implements View.OnClickListener {
    private NoteViewModel mNoteViewModel;
    private DialogEditNoteBinding binding;
    private NoteAdapter mAdapter;
    private List<Note> mNotes = new ArrayList<>();
    private Bundle bundle;
    private User mUser;
    private Context context;

    private CommunicateViewModel mCommunicateViewModel;

    public static EditNoteDialog newInstance() {
        return new EditNoteDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * This method is called when a view is being created
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       binding = DialogEditNoteBinding.inflate(inflater, container, false);
        setUserInfo();
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        return binding.getRoot();
    }

    /**
     * This method is called after the onCreateView() method
     * @param view View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        mAdapter = new NoteAdapter(mNotes, this.getContext());

        setOnClicks();

        bundle = getArguments();
        if (bundle != null) {
            binding.etNote.setText(bundle.getString("note_name"));
        }
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClicks() {
        binding.btnUpdateNote.setOnClickListener(this);
        binding.btnCloseNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_note:
                mNoteViewModel.editNote(bundle.getString("note_name"),
                        binding.etNote.getText().toString()).enqueue(
                        new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call,
                                                   Response<BaseResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    BaseResponse baseResponse = response.body();
                                    if (baseResponse.getStatus() == 1) {
                                        mCommunicateViewModel.makeChanges();

                                        Toast.makeText(context, "Update Successful!",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d("RESUME", "Edit Success");
                                    } else if (baseResponse.getStatus() == -1) {
                                        if (response.body().getError() == Integer.getInteger(null)) {
                                            Toast.makeText(context, "Update Failed!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                mCommunicateViewModel.makeChanges();

                                Toast.makeText(getActivity(), "Update Failed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dismiss();
                break;
            case R.id.btn_close_note:
                dismiss();
                break;
        }
    }

    /**
     * This method get the user data from the SharedPreference
     */
    private void setUserInfo() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }

    /**
     * This method is called when the view is destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
