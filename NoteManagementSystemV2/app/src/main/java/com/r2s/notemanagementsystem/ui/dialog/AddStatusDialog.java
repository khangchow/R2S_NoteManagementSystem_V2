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
import com.r2s.notemanagementsystem.databinding.DialogAddStatusBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.StatusViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStatusDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "StatusDialog";
    private StatusViewModel mStatusViewModel;
    private DialogAddStatusBinding binding;
    private StatusAdapter mStatusAdapter;
    private List<Status> mStatuses = new ArrayList<>();
    private Bundle bundle = new Bundle();
    private User mUser;
    private Context mContext;
    private CommunicateViewModel mCommunicateViewModel;

    public static AddStatusDialog newInstance() {
        return new AddStatusDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
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
        binding = DialogAddStatusBinding.inflate(inflater, container, false);

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

        setUpViewModel();
        setOnClicks();
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClicks() {
        binding.btnAddStatus.setOnClickListener(this);
        binding.btnCloseStatus.setOnClickListener(this);
    }

    /**
     * This method sets up the ViewModel
     */
    public void setUpViewModel() {
        mStatusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        mStatusAdapter = new StatusAdapter(mStatuses, this.getContext());
        mStatusViewModel.getAllStatuses()
                .observe(getViewLifecycleOwner(), statuses -> {
            mStatusAdapter.setStatuses(statuses);
        });
    }

    /**
     * This method sets on-click actions for views
     * @param view current view of the activity/fragment
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_status:
                if (isEmpty()) {
                    final String name =
                            Objects.requireNonNull(binding.etStatus.getText()).toString();
                    mStatusViewModel.addStatus(name).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                BaseResponse baseResponse = response.body();
                                if (baseResponse.getStatus() == 1) {
                                    mCommunicateViewModel.makeChanges();

                                    Toast.makeText(mContext, "Create Successful!",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("RESUME", "Add Success");
                                    dismiss();
                                } else if (baseResponse.getStatus() == -1) {
                                    if (baseResponse.getError() == 2) {
                                        binding.tilStatus.setError("This name is already taken!");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Create Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    binding.tilStatus.setError("This field can't be empty!");
                }
                break;
            case R.id.btn_close_status:
                dismiss();
                break;
        }
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
     * This method get the user data from the SharedPreference
     */
    private void setUserInfo() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }

    private Boolean isEmpty() {
        if (Objects.requireNonNull(binding.etStatus.getText()).toString().trim().length() <= 0) {
            return false;
        }
        return true;
    }
}