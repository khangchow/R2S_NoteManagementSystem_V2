package com.r2s.notemanagementsystem.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;
import com.r2s.notemanagementsystem.viewmodel.CommunicateViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddNewCategoryDialog extends DialogFragment implements View.OnClickListener {
    private TextInputEditText etNewCate;
    private AppCompatButton btnAdd, btnCancel;
    private CategoryViewModel mCateViewModel;
    private DialogAddNewCategoryBinding binding;
    private Bundle bundle = new Bundle();
    CategoryAdapter mCateAdapter;
    private CommunicateViewModel mCommunicateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_new_category, container, false);


        //Reference
        etNewCate = view.findViewById(R.id.etNewCate);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);

        mCateViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        mCateAdapter = new CategoryAdapter( this.getContext());

        // Setting modal dialog
        setCancelable(false);

        bundle = getArguments();
        if (bundle != null) {
            binding.btnAdd.setText("Update");
            binding.etNewCate.setText(bundle.getString(CategoryConstant.CATEGORY_KEY));
        }
    }

    private void setOnClick() {
        binding.btnAdd.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
    }

    private void setViewModel() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /**
             * Handling button add new category
             */
            case R.id.btnAdd:

                /**
                 * Add new category
                 */
                if (binding.btnAdd.getText().toString()
                        .equalsIgnoreCase("add")) {

                    mCateViewModel.insertCate(binding.etNewCate.getText().toString())
                            .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                                mCommunicateViewModel.makeChanges();
                                dismiss();
                            } else if (response.body().getStatus() == -1){
                                if (response.body().getError() == 2) {
                                    Toast.makeText(getContext(), "Category name already exists"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Failure!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                /**
                 * Update category
                 */
                else if (binding.btnAdd.getText().toString()
                        .equalsIgnoreCase("update")) {

                    try {
                        String updateId = bundle.getString(CategoryConstant.CATEGORY_KEY);

                        Log.d("TTT", updateId);

                        mCateViewModel.updateCate(updateId, binding.etNewCate.getText().toString())
                                .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                if (response.body().getStatus() == 1) {
                                    Toast.makeText(getContext(), "Update successful"
                                            , Toast.LENGTH_SHORT).show();

                                    mCommunicateViewModel.makeChanges();
                                    dismiss();

                                }
                            }
                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Log.e("NNN", t.getMessage());

                            }
                        });
                    } catch (Exception e) {
                        Log.e("GGG", e.getMessage());

                    }
                }
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    private void setUserInfo() {
        User mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }
}
