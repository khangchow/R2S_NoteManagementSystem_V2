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
import com.r2s.notemanagementsystem.adapter.PriorityAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogPriorityBinding;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.viewmodel.PriorityViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PriorityDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "PriorityDialog";
    private PriorityViewModel mPriorityViewModel;
    private DialogPriorityBinding binding;
    private PriorityAdapter mPriorityAdapter;
    private List<Priority> mPriorities = new ArrayList<>();
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
        binding = DialogPriorityBinding.inflate(inflater, container, false);

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
        mPriorityAdapter = new PriorityAdapter(mPriorities, this.getContext());

        setUpViewModel();
        setOnClicks();

        bundle = getArguments();
        if (bundle != null) {
            binding.btnAddPriority.setText("Update");
            binding.etPriority.setText(bundle.getString("priority_name" ));
        }
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
        mPriorityViewModel.getAllPrioritiesByUserId()
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
                if (binding.btnAddPriority.getText().toString()
                        .equalsIgnoreCase("add")) {
                    final Priority priority = new Priority(0,
                            binding.etPriority.getText().toString(),
                            getCurrentLocalDateTimeStamp(), mUser.getUid());
                    mPriorityViewModel.insertPriority(priority);

                    Toast.makeText(getActivity(), "Create " +
                            binding.etPriority.getText().toString(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }

                if (binding.btnAddPriority.getText().toString()
                        .equalsIgnoreCase("update")) {
                    int updateId = bundle.getInt("priority_id");

                    final Priority priority = new Priority(updateId,
                            binding.etPriority.getText().toString(),
                            getCurrentLocalDateTimeStamp(), mUser.getUid());

                    mPriorityViewModel.updatePriority(priority);

                    Toast.makeText(getActivity(), "Update to " +
                            binding.etPriority.getText().toString(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                break;
            case R.id.btn_close_priority:
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