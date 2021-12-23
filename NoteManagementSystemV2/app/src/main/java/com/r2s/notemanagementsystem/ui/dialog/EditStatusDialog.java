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
import com.r2s.notemanagementsystem.adapter.StatusAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogEditStatusBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.StatusViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStatusDialog extends DialogFragment implements View.OnClickListener {
    private StatusViewModel mStatusViewModel;
    private DialogEditStatusBinding binding;
    private StatusAdapter mStatusAdapter;
    private List<Status> mStatuses = new ArrayList<>();
    private Bundle bundle;
    private User mUser;
    private Context mContext;
    private CommunicateViewModel mCommunicateViewModel;

    public static EditStatusDialog newInstance() {
        return new EditStatusDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditStatusBinding.inflate(inflater, container, false);
        setUserInfo();
        mCommunicateViewModel =
                new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
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

        mStatusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        mStatusAdapter = new StatusAdapter(mStatuses, this.getContext());

        setOnClicks();

        bundle = getArguments();
        if (bundle != null) {
            binding.etStatus.setText(bundle.getString("status_name"));
        }
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClicks() {
        binding.btnUpdateStatus.setOnClickListener(this);
        binding.btnCloseStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_update_status:
                if(isEmpty()) {
                    mStatusViewModel.editStatus(bundle.getString("status_name"),
                            binding.etStatus.getText().toString()).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                BaseResponse baseResponse = response.body();
                                if (baseResponse.getStatus() == 1) {
                                    mCommunicateViewModel.makeChanges();

                                    Toast.makeText(mContext, "Update Successful!",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("RESUME", "Edit Success");

                                } else if (baseResponse.getStatus() == -1) {
                                    Integer error = new Integer(baseResponse.getError());
                                    Log.d("TTT", error.toString());
                                    if (error.equals(null)) {
                                        Toast.makeText(mContext, "Update Failed!",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d("RESUME", "Edit Failed");
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
                } else {
                    binding.tilStatus.setError("This field can't be empty!");
                    return;
                }
                dismiss();
                break;
            case R.id.btn_close_status:
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

    private Boolean isEmpty() {
        if (Objects.requireNonNull(binding.etStatus.getText()).toString().trim().length() <= 0) {
            return false;
        }
        return true;
    }
}
