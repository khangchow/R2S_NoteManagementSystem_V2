package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.NoteAdapter;
import com.r2s.notemanagementsystem.databinding.FragmentNoteBinding;
import com.r2s.notemanagementsystem.model.Note;
import com.r2s.notemanagementsystem.ui.dialog.FragmentDialogInsertNote;
import com.r2s.notemanagementsystem.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentNote extends Fragment implements View.OnClickListener {
    private FragmentNoteBinding binding;
    private NoteViewModel mNoteViewModel;
    private NoteAdapter mNoteAdapter;
    private List<Note> mNotes = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNote() {
        // Required empty constructor
    }

    /**
     * This fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
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
        return binding.getRoot();
    }

    /**
     * This method is called after the onCreateView() method
     *
     * @param view               View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        setUpRecyclerView();
        setOnClicks();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Implement swipe to delete
             * @param viewHolder RecyclerView.ViewHolder
             * @param direction int
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(binding.rcvNoteFragment);
    }

    /**
     * This method is called when the user resume the view
     */
    @Override
    public void onResume() {
        super.onResume();
        retrieveNotes();
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
     * This method sets on-click listener for views
     */
    private void setOnClicks() {
        binding.fab.setOnClickListener(this);
    }

    /**
     * This method initializes the Adapter and attach it to the RecyclerView
     */
    private void setUpRecyclerView() {
        mNoteAdapter = new NoteAdapter(mNotes, this.getContext());

        binding.rcvNoteFragment.setAdapter(mNoteAdapter);

        binding.rcvNoteFragment.setLayoutManager(new LinearLayoutManager(getContext()));
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
                DialogFragment dialogFragment = new FragmentDialogInsertNote();
                dialogFragment.show(getChildFragmentManager(), FragmentDialogInsertNote.TAG);
        }
    }

    /**
     * This method is used to update the RecyclerView
     */
    public void retrieveNotes() {

    }

}
