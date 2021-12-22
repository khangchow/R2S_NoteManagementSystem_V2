package com.r2s.notemanagementsystem.ui.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.PriorityAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogEditPriorityBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.service.PriorityService;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.viewmodel.PriorityViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPriorityDialog extends DialogFragment implements View.OnClickListener {

    private PriorityViewModel mPriorityViewModel;
    private DialogEditPriorityBinding binding;
    private PriorityAdapter mAdapter;
    private List<Priority> mPriorities = new ArrayList<>();
    private Bundle bundle;
    private User mUser;

    public static EditPriorityDialog newInstance() {
        return new EditPriorityDialog();
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
        binding = DialogEditPriorityBinding.inflate(inflater, container, false);
        setUserInfo();
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

        mPriorityViewModel = new ViewModelProvider(this).get(PriorityViewModel.class);
        mAdapter = new PriorityAdapter(mPriorities, this.getContext());

        setOnClicks();

        bundle = getArguments();
        if (bundle != null) {
            binding.etPriority.setText(bundle.getString("priority_name"));
        }
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClicks() {
        binding.btnUpdatePriority.setOnClickListener(this);
        binding.btnClosePriority.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_priority:
                mPriorityViewModel.editPriority(bundle.getString("priority_name"),
                        binding.etPriority.getText().toString()).enqueue(
                        new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call,
                                           Response<BaseResponse> response) {
                        if (response.isSuccessful() && isAdded()) {
                            BaseResponse baseResponse = response.body();
                            assert baseResponse != null;
                            if (baseResponse.getStatus() == 1) {
                                Toast.makeText(requireActivity(), "Update Successful!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (baseResponse.getStatus() == -1) {
                                Toast.makeText(requireActivity(), "Update Failed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Update Failed!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                dismiss();
                break;
            case R.id.btn_close_priority:
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