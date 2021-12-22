package com.r2s.notemanagementsystem.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.CategoryAdapter;
import com.r2s.notemanagementsystem.api.CategoryService;
import com.r2s.notemanagementsystem.constant.CategoryConstant;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogAddNewCategoryBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewCategoryDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "CateDialog";
    private CategoryViewModel mCateViewModel;
    private DialogAddNewCategoryBinding binding;
    private final List<Category> mCates = new ArrayList<>();
    private Bundle bundle = new Bundle();
    CategoryAdapter mCateAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogAddNewCategoryBinding.inflate(inflater, container, false);

        setUserInfo();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCateViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        mCateAdapter = new CategoryAdapter( this.getContext());

        setViewModel();
        setOnClick();

        bundle = getArguments();
        if (bundle != null) {
            binding.btnAdd.setText("Update");
            binding.etNewCate.setText(bundle.getString("cate_name"));
        }
    }

    private void setOnClick() {
        binding.btnAdd.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
    }

    private void setViewModel() {
        mCateViewModel.getCateById().observe(getViewLifecycleOwner(), categories -> {
            mCateAdapter.setCateAdapter(mCates);
        });
    }

    private void setUserInfo() {
        User mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                if (binding.btnAdd.getText().toString()
                        .equalsIgnoreCase("add")) {

                    mCateViewModel.insertCate(binding.etNewCate.getText().toString())
                            .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                            }

                            if (response.body().getStatus() == -1){
                                if (response.body().getError() == 2) {
                                    Toast.makeText(getContext(), "cate name already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Failure!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dismiss();

                } else if (binding.btnAdd.getText().toString()
                        .equalsIgnoreCase("update")) {
                    int updateId = bundle.getInt("status_id");

                    final String category = mCates.get(updateId).getNameCate();

                    int nope = Integer.parseInt(String.valueOf(0));

                    mCateViewModel.updateCate(category, binding.etNewCate.getText().toString())
                            .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(getContext(), "Update successful"
                                        , Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus() == -1){
                                if (response.body().getError() == Integer.getInteger(null)) {
                                    Toast.makeText(getContext(), "Failure!!!"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                        }
                    });

                    dismiss();
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

    /**
     * get current time
     * @return
     */
    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
