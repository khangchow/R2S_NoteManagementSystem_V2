package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
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
import com.r2s.notemanagementsystem.constant.CategoryConstant;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.ui.dialog.AddNewCategoryDialog;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;
import com.r2s.notemanagementsystem.viewmodel.CommunicateViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {
    private CategoryViewModel mCateViewModel;
    private CommunicateViewModel mCommunicateViewModel;
    private User mUser;

    
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
        FloatingActionButton fabAddCate = view.findViewById(R.id.fabAddCate);

        mCateAdapter = new CategoryAdapter( this.getContext());

        mCateViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        mCateViewModel.getCateById().observe(getViewLifecycleOwner(), categories -> {
            mCateAdapter.setCateAdapter(categories);
        });

        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        mCommunicateViewModel.needReloading().observe(getViewLifecycleOwner(), needReloading ->{
            Log.d("RESUME", needReloading.toString());
            if (needReloading) {
                onResume();
            }
        });

        rvCate.setLayoutManager(new LinearLayoutManager(getContext()));

        // Handling floating button
        fabAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddNewCategoryDialog().show(getChildFragmentManager(), AddNewCategoryDialog.TAG);
                mCommunicateViewModel.openDialog();
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

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction) {

                    /**
                     * Swiped to delete
                     */
                    case ItemTouchHelper.RIGHT:
                        int position = viewHolder.getAdapterPosition();
                        List<Category> tasks = mCateAdapter.getCateAdapter();
                        String category = tasks.get(position).getNameCate();

                        /**
                         * showing confirm dialog
                         */
                        new AlertDialog.Builder(getContext())
                                .setTitle("Confirm!!")
                                .setMessage("Do you really want to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mCateViewModel.deleteCate(category).enqueue(new Callback<BaseResponse>() {
                                            @Override
                                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                                if (response.body().getStatus() == 1) {
                                                    Toast.makeText(getContext(), "Deleted",
                                                            Toast.LENGTH_SHORT).show();

                                                    mCateViewModel.refreshData();
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
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mCateViewModel.refreshData();
                                    }
                                }).show();
                        onResume();
                        break;

                    /**
                     * Swiped to update
                     */
                    case ItemTouchHelper.LEFT:
                        try {
                            int position1 = viewHolder.getAdapterPosition();
                            List<Category> list = mCateAdapter.getCateAdapter();
                            String cate = list.get(position1).getNameCate();

                            Bundle bundle = new Bundle();
                            bundle.putString(CategoryConstant.CATEGORY_KEY, cate);

                            Log.d("RRR", cate);

                            final AddNewCategoryDialog addNewCategoryDialog = new AddNewCategoryDialog();
                            addNewCategoryDialog.setArguments(bundle);

                            FragmentManager fm = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            addNewCategoryDialog.show(fm, "CateDialog");

                            mCommunicateViewModel.openDialog();
                            onResume();
                        } catch (Exception e){
                            Log.e("FFF", e.getMessage());
                        }

                        break;
                }
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
        Log.d("RESUME", "load");
    }

    /**
     * This method get the user data from the SharedPreference
     */
    private void setUserInfo() {
       mUser  = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }
}