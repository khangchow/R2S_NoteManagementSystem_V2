package com.r2s.notemanagementsystem.ui.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.CategoryAdapter;
import com.r2s.notemanagementsystem.constant.CategoryConstant;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogAddNewCategoryBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;
import com.r2s.notemanagementsystem.viewmodel.CommunicateViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewCategoryDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "CateDialog";
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
        binding = DialogAddNewCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCateViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        mCateAdapter = new CategoryAdapter( this.getContext());

        setViewModel();
        setOnClick();

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
                                public void onResponse(Call<BaseResponse> call
                                                                , Response<BaseResponse> response) {
                                    if (response.body().getStatus() == 1) {
                                        Toast.makeText(getContext(), "Successful"
                                                , Toast.LENGTH_SHORT).show();
                                        mCommunicateViewModel.makeChanges();
                                        dismiss();

                                    } else if (response.body().getStatus() == -1){
                                        if (response.body().getError() == 2) {
                                            Toast.makeText(getContext()
                                                    , "Category name already exists"
                                                    , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    Toast.makeText(getContext(), "Failure!!!"
                                            , Toast.LENGTH_SHORT).show();
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

                        mCateViewModel.updateCate(updateId, binding.etNewCate.getText().toString())
                                .enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call
                                            , Response<BaseResponse> response) {
                                        if (response.body().getStatus() == 1) {
                                            Toast.makeText(getContext(), "Update successful"
                                                    , Toast.LENGTH_SHORT).show();

                                            mCommunicateViewModel.makeChanges();
                                            dismiss();

                                        } else if (response.body().getStatus() == -1) {
                                            String error =
                                                        String.valueOf(response.body().getError());

                                            if ((binding.etNewCate.getText().toString() == null)) {
                                                Toast.makeText(getContext(), "Update fail"
                                                        , Toast.LENGTH_SHORT).show();
                                            }
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
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
