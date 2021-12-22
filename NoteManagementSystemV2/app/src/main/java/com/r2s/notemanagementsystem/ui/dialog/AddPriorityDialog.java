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
import com.r2s.notemanagementsystem.adapter.PriorityAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogAddPriorityBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.PriorityViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPriorityDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "PriorityDialog";
    private PriorityViewModel mPriorityViewModel;
    private DialogAddPriorityBinding binding;
    private PriorityAdapter mPriorityAdapter;
    private List<Priority> mPriorities = new ArrayList<>();
    private User mUser;
    private Context context;
    private CommunicateViewModel mCommunicateViewModel;

    public static AddPriorityDialog newInstance() {
        return new AddPriorityDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        binding = DialogAddPriorityBinding.inflate(inflater, container, false);

        setUserInfo();

        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);

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
        mPriorityAdapter = new PriorityAdapter(mPriorities, this.getContext());

        setUpViewModel();
        setOnClicks();
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClicks() {
        binding.btnAddPriority.setOnClickListener(this);
        binding.btnClosePriority.setOnClickListener(this);
    }

    /**
     * This method sets up the ViewModel
     */
    private void setUpViewModel() {
        mPriorityViewModel = new ViewModelProvider(this).get(PriorityViewModel.class);
        mPriorityViewModel.getAllPriorities()
                .observe(getViewLifecycleOwner(), priorities -> {
            mPriorityAdapter.setPriorities(priorities);
        });
    }

    /**
     * This method sets on-click actions for views
     * @param view current view of the activity/fragment
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_priority:
                if(isEmpty()) {
                    final String name = Objects.requireNonNull(binding.etPriority.getText())
                            .toString();

                    mPriorityViewModel.addPriority(name).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call,
                                               Response<BaseResponse> response) {
                            if (response.isSuccessful()) {
                                BaseResponse baseResponse = response.body();
                                assert baseResponse != null;
                                if (baseResponse.getStatus() == 1) {
                                    mCommunicateViewModel.makeChanges();

                                    Toast.makeText(context, "Create Successful!",
                                            Toast.LENGTH_SHORT).show();
                                    mPriorityViewModel.refreshData();
                                    Log.d("RESUME", "Add Success");
                                } else if (baseResponse.getStatus() == -1)
                                        if(baseResponse.getError() == 2) {
                                            mCommunicateViewModel.makeChanges();

                                            Toast.makeText(context,
                                            "This name is already taken",
                                            Toast.LENGTH_SHORT).show();
                                    mPriorityViewModel.refreshData();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Create Failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    dismiss();
                } else {
                    binding.etPriority.setError("This information can't be empty!");
                    return;
                }
                break;
            case R.id.btn_close_priority:
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
        if (Objects.requireNonNull(binding.etPriority.getText()).toString().trim().length() <= 0) {
            return false;
        }
        return true;
    }
}