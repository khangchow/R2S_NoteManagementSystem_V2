package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.CategoryAdapter;
import com.r2s.notemanagementsystem.adapter.StatusAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.FragmentCategoryBinding;
import com.r2s.notemanagementsystem.databinding.FragmentStatusBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.ui.dialog.StatusDialog;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.ui.dialog.AddNewCategoryDialog;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;
import com.r2s.notemanagementsystem.viewmodel.StatusViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment{
    private CategoryAdapter mCateAdapter;
    private CategoryViewModel mCateViewModel;
    private List<BaseResponse> mCateList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method is used for non-graphical initialisations
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        setUserInfo();
    }

    /**
     * This method is used for graphical initialisations
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        binding = FragmentCategoryBinding.inflate(getLayoutInflater());
//        return binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        return view;
    }

    /**
     * This method is called after the onCreateView() method
     * @param view View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCateViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        RecyclerView rvCate = view.findViewById(R.id.rvCategory);
        FloatingActionButton fabAddCate = view.findViewById(R.id.fabAddCate);

        mCateAdapter = new CategoryAdapter(mCateList, this.getContext());

        rvCate.setAdapter(mCateAdapter);

        rvCate.setLayoutManager(new LinearLayoutManager(getContext()));

        // Handling floating button
        fabAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddNewCategoryDialog().show(getChildFragmentManager(), AddNewCategoryDialog.TAG);
            }
        });

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
                int position = viewHolder.getAdapterPosition();
                List<BaseResponse> tasks = mCateAdapter.getCateAdapter();
                String category = tasks.get(position).getCategory().getNameCate();

                mCateViewModel.deleteCate(category).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        } else if (response.body().getStatus() == -1){
                            if (response.body().getError() == 2) {
                                Toast.makeText(getContext(), "Can't delete, cause using",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });
            }
        }).attachToRecyclerView(rvCate);
    }

    /**
     * This method is called when the user resume the view
     */
    @Override
    public void onResume() {
        super.onResume();
        mCateViewModel.refreshData();
    }

    /**
     * This method is called when the view is destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * This method get the user data from the SharedPreference
     */
    private void setUserInfo() {
        User mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }
}