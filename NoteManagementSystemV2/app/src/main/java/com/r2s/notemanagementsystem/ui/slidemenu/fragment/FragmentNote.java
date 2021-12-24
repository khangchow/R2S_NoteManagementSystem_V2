package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.NoteAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.NoteConstant;
import com.r2s.notemanagementsystem.databinding.FragmentNoteBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Note;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.repository.NoteRepository;
import com.r2s.notemanagementsystem.service.NoteService;
import com.r2s.notemanagementsystem.ui.dialog.EditNoteDialog;
import com.r2s.notemanagementsystem.ui.dialog.FragmentDialogInsertNote;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNote extends Fragment implements View.OnClickListener {
    private FragmentNoteBinding binding;
    private NoteViewModel mNoteViewModel;
    private NoteAdapter mNoteAdapter;
    private List<Note> mNotes = new ArrayList<>();
    private User mUser;
    private Context mContext;
    private CommunicateViewModel mCommunicateViewModel;
    private NoteService mNoteService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNote() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriorityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNote newInstance(String param1, String param2) {
        FragmentNote fragment = new FragmentNote();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method is used for non-graphical initialisations
     *
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * This method is used for graphical initialisations
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(getLayoutInflater());

        setUserInfo();

        mNoteService = NoteRepository.getService();

        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        mCommunicateViewModel.needReloading().observe(getViewLifecycleOwner(), needReloading -> {
            if (needReloading) {
                onResume();
            }
        });

        binding.fab.setOnClickListener(this);

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        mNoteViewModel.refreshData();
        mNoteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            mNoteAdapter.setNotes(notes);
        });

        mNoteAdapter = new NoteAdapter(mNotes, getContext());

        binding.rcvNoteFragment.setAdapter(mNoteAdapter);
        binding.rcvNoteFragment.setHasFixedSize(true);
        binding.rcvNoteFragment.setLayoutManager(new LinearLayoutManager(getContext()));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Implement swipe to delete
             * @param viewHolder RecyclerView.ViewHolder
             * @param direction int
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    // Swipe right to delete
                    case ItemTouchHelper.RIGHT:
                        int position = viewHolder.getAdapterPosition();
                        mNotes = mNoteAdapter.getNotes();
                        String noteName = mNotes.get(position).getName();
                        String notePri = mNotes.get(position).getPriority();
                        String noteCate = mNotes.get(position).getCategory();
                        String noteSta = mNotes.get(position).getStatus();
                        String planDate = mNotes.get(position).getPlanDate();

                        new AlertDialog.Builder(requireContext())
                                .setTitle("Title")
                                .setMessage("Do you really want to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mNoteViewModel.deleteNote(noteName).enqueue(
                                                new Callback<BaseResponse>() {
                                                    @Override
                                                    public void onResponse(Call<BaseResponse> call,
                                                                           Response<BaseResponse> response) {

                                                        assert response.body() != null;
                                                        if (response.body().getStatus() == 1) {
                                                            Toast.makeText(getContext(),
                                                                    "Deleted",
                                                                    Toast.LENGTH_SHORT).show();
                                                            mNoteViewModel.refreshData();

                                                        } else if (response.body()
                                                                .getStatus() == -1) {
                                                            if (response.body().getError() == 2) {

                                                                Toast.makeText(getContext(),
                                                                        "Can't delete, " +
                                                                                "because it's in use",
                                                                        Toast.LENGTH_SHORT).show();
                                                                mNoteViewModel.refreshData();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<BaseResponse> call,
                                                                          Throwable t) {

                                                    }
                                                }
                                        );
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mNoteViewModel.refreshData();
                                    }
                                }).create().show();
                        onResume();
                        break;

                    // Swipe left to update
                    case ItemTouchHelper.LEFT:
                        try {
                            position = viewHolder.getAdapterPosition();
                            mNotes = mNoteAdapter.getNotes();

                            noteName = mNotes.get(position).getName();
                            notePri = mNotes.get(position).getPriority();
                            noteCate = mNotes.get(position).getCategory();
                            noteSta = mNotes.get(position).getStatus();
                            planDate = mNotes.get(position).getPlanDate();

                            Bundle bundle = new Bundle();
                            bundle.putString("note_name", noteName);
                            bundle.putString("priority_name", notePri);
                            bundle.putString("category_name", noteCate);
                            bundle.putString("status_name", noteSta);
                            bundle.putString("plan_date", planDate);


                            final EditNoteDialog editNoteDialog = EditNoteDialog.newInstance();
                            editNoteDialog.setArguments(bundle);

                            FragmentManager fm = ((AppCompatActivity) requireContext())
                                    .getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            editNoteDialog.show(ft, "EditNoteDialog");
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                        }
                        mNoteViewModel.refreshData();
                        onResume();
                        break;
                }
            }
        }).attachToRecyclerView(binding.rcvNoteFragment);

        return binding.getRoot();
    }

    /**
     * This method is called when the user resume the view
     */
    @Override
    public void onResume() {
        super.onResume();

        mNoteService.getAllNotes(NoteConstant.NOTE_TAB, mUser.getEmail())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Note> mNote = new ArrayList<>();
                            for (List<String> note : response.body().getData()) {
                                mNote.add(new Note(note.get(0), note.get(1), note.get(2), note.get(3), note.get(4), note.get(5)));
                            }
                            mNoteAdapter.setNotes(mNote);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });
    }

    /**
     * This method is called when the view is destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * This method sets on-click actions for views
     *
     * @param view View
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                DialogFragment noteDialog = FragmentDialogInsertNote.newInstance();
                noteDialog.show(getChildFragmentManager(), FragmentDialogInsertNote.TAG);
        }
    }

    /**
     * This method get the user data from the SharedPreference
     */
    private void setUserInfo() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }

}
