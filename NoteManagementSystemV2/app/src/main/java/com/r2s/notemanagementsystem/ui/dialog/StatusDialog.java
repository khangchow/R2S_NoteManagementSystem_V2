package com.r2s.notemanagementsystem.ui.dialog;

import android.os.Bundle;
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
import com.r2s.notemanagementsystem.databinding.DialogStatusBinding;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.viewmodel.StatusViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatusDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "StatusDialog";
    private StatusViewModel mStatusViewModel;
    private DialogStatusBinding binding;
    private StatusAdapter mStatusAdapter;
    private List<Status> mStatuses = new ArrayList<>();
    private Bundle bundle = new Bundle();
    private User mUser;

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
        binding = DialogStatusBinding.inflate(inflater, container, false);

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

        mStatusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        mStatusAdapter = new StatusAdapter(mStatuses, this.getContext());

        setUpViewModel();
        setOnClicks();

        bundle = getArguments();
        if (bundle != null) {
            binding.btnAddStatus.setText("Update");
            binding.btnCloseStatus.setText(bundle.getString("priority_name" ));
        }
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
        mStatusViewModel.getAllStatusesByUserId()
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
                if (binding.btnAddStatus.getText().toString()
                        .equalsIgnoreCase("add")) {
                    final Status status = new Status(0, binding.etStatus.getText().toString(),
                            getCurrentLocalDateTimeStamp(), mUser.getUid());
                    mStatusViewModel.insertStatus(status);

                    Toast.makeText(getActivity(), "Create " +
                            binding.etStatus.getText().toString(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }

                if (binding.btnAddStatus.getText().toString()
                        .equalsIgnoreCase("update")) {
                    int updateId = bundle.getInt("status_id");
                    final Status status = new Status(updateId,
                            binding.etStatus.getText().toString(),
                            getCurrentLocalDateTimeStamp(), mUser.getUid());
                    mStatusViewModel.insertStatus(status);

                    mStatusViewModel.updateStatus(status);

                    Toast.makeText(getActivity(), "Update to " +
                            binding.etStatus.getText().toString(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                break;
            case R.id.btn_close_status:
                dismiss();
                break;
        }
    }

    /**
     * This method returns the current date with custom format
     * @return String
     */
    public String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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
}
