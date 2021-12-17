package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.CategoryAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.ui.dialog.AddNewCategoryDialog;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {
    private CategoryViewModel mCateViewModel;
    private CategoryAdapter mAdapter;
    private int uId = 1;
    private User mUser;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        RecyclerView rvCate = view.findViewById(R.id.rvCategory);

        // Handler floating action button
        FloatingActionButton fabAddNewCategory = view.findViewById(R.id.fabAddCate);
        fabAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment cateDialog = AddNewCategoryDialog.newInstance();
                cateDialog.show(getChildFragmentManager(), "Category");
            }
        });

        rvCate.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new CategoryAdapter(getContext());
        rvCate.setAdapter(mAdapter);

        mCateViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        mCateViewModel.loadAllCate().observe(getActivity(), categories -> {
            mAdapter.setTasks(categories);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<Category> tasks = mAdapter.getTasks();
                mCateViewModel.deleteCate(tasks.get(position));
            }
        }).attachToRecyclerView(rvCate);

        return view;
    }
}