package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.databinding.FragmentEditProfileBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.KeyboardUtils;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentEditProfileBinding binding;
    private UserViewModel mUserViewModel;
    private User mUser;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnChange.setOnClickListener(this);

        binding.btnHome.setOnClickListener(this);

        mUserViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        //Lay thong tin user tu phien dang nhap
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);

        onFocusChanges();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change:
                editProfile();
                break;

            case R.id.btn_home:
                navController.navigate(
                        R.id.action_slide_menu_nav_edit_profile_to_slide_menu_nav_home);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        navController = Navigation.findNavController(getActivity()
                , R.id.nav_host_fragment_content_home);
    }

    private void onFocusChanges() {
        binding.etFirstName.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                KeyboardUtils.hideKeyboard(view);
            }
        });

        binding.etLastName.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                KeyboardUtils.hideKeyboard(view);
            }
        });

        binding.etEmail.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                KeyboardUtils.hideKeyboard(view);
            }
        });
    }


    private void editProfile(){
        if (!isEmptyField() && mUser.getEmail().equals(binding.etEmail.getText().toString())
                && !isRepeatedData()){
            mUser.setFirstName(binding.etFirstName.getText().toString());

            mUser.setLastName(binding.etLastName.getText().toString());

            mUserViewModel.updateUser(UserConstant.TAB_PROFILE
                    , binding.etEmail.getText().toString()
                    , binding.etEmail.getText().toString()
                    , binding.etFirstName.getText().toString()
                    , binding.etLastName.getText().toString()
            ).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful() && response.body().getStatus() == 1) {
                        //Doi profile ở header
                        mUserViewModel.updatedUserData();

                        //Doi profile cua thong tin dang nhap
                        AppPrefsUtils.putString(Constants.KEY_USER_DATA, new Gson().toJson(mUser));

                        //Reset các fields
                        binding.etFirstName.setText(null);

                        binding.etLastName.setText(null);

                        binding.etEmail.setText(null);

                        binding.tilEmail.setError(null);

                        binding.tilFirstname.setError(null);

                        binding.tilLastname.setError(null);

                        binding.parent.requestFocus();

                        Toast.makeText(getContext(), R.string.txt_update_successful
                                ,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });
        }else {
            Boolean isFocused = false;

            if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
                binding.tilEmail.setError(getString(R.string.txt_enter_email));

                if (!isFocused) {
                    binding.etEmail.requestFocus();

                    KeyboardUtils.openKeyboard(binding.etEmail);

                    isFocused = true;
                }
            }else {
                binding.tilEmail.setError(null);
            }

            if (TextUtils.isEmpty(binding.etFirstName.getText().toString())) {
                binding.tilFirstname.setError(getString(R.string.app_etfirstname));

                if (!isFocused) {
                    binding.etFirstName.requestFocus();

                    KeyboardUtils.openKeyboard(binding.etFirstName);

                    isFocused = true;
                }
            }else {
                binding.tilFirstname.setError(null);
            }

            if (TextUtils.isEmpty(binding.etLastName.getText().toString())) {
                binding.tilLastname.setError(getString(R.string.app_etlastname));

                if (!isFocused) {
                    binding.etLastName.requestFocus();

                    KeyboardUtils.openKeyboard(binding.etLastName);
                }
            }else {
                binding.tilLastname.setError(null);
            }

            if (!isEmptyField() && !mUser.getEmail().equals(binding.etEmail.getText().toString())) {
                binding.tilEmail.setError(getString(R.string.err_wrong_email));

                binding.etEmail.requestFocus();

                KeyboardUtils.openKeyboard(binding.etEmail);
            }else if (isRepeatedData()) {
                binding.tilEmail.setError(null);

                binding.tilFirstname.setError(getString(R.string.err_repeat_name));

                binding.tilLastname.setError(getString(R.string.err_repeat_name));

                binding.etFirstName.requestFocus();

                KeyboardUtils.openKeyboard(binding.etFirstName);
            }
        }
    }

    private boolean isRepeatedData() {
        return mUser.getFirstName().equals(binding.etFirstName.getText().toString())
                && mUser.getLastName().equals(binding.etLastName.getText().toString());
    }

    private boolean isEmptyField() {
        return TextUtils.isEmpty(binding.etEmail.getText().toString())
                || TextUtils.isEmpty(binding.etFirstName.getText().toString())
                || TextUtils.isEmpty(binding.etLastName.getText().toString());
    }
}
