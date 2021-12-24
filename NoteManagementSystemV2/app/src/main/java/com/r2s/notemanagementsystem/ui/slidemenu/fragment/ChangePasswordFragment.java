package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.databinding.FragmentChangePasswordBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.KeyboardUtils;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private FragmentChangePasswordBinding binding;
    private UserViewModel mUserViewModel;
    private User mUser;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChangePasswordBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //Lay thong tin user tu phien dang nhap
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);

        setOnClick();

        onFocusChanges();
    }

    private void setOnClick() {
        binding.btnChangePassword.setOnClickListener(this);

        binding.btnHomeChangePassword.setOnClickListener(this);
    }

    private void onFocusChanges() {
        binding.fragmentChangePasswordEtNew.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                KeyboardUtils.hideKeyboard(view);
            }
        });

        binding.fragmentChangePasswordEtCurrent.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                KeyboardUtils.hideKeyboard(view);
            }
        });

        binding.fragmentChangePasswordEtAgain.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                KeyboardUtils.hideKeyboard(view);
            }
        });
    }

    private void updatePassword() {
        if(!isEmptyField() && isPasswordCorrect()
                && !mUser.getPassword()
                .equals(binding.fragmentChangePasswordEtNew.getText().toString())
                && binding.fragmentChangePasswordEtNew.getText().toString()
                .equals(binding.fragmentChangePasswordEtAgain.getText().toString())){

            mUser.setPassword(binding.fragmentChangePasswordEtNew.getText().toString());

            mUserViewModel.changePassword(UserConstant.TAB_PROFILE, mUser.getEmail()
                    , binding.fragmentChangePasswordEtCurrent.getText().toString()
                    , binding.fragmentChangePasswordEtNew.getText().toString())
                    .enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            //Doi profile cua thong tin dang nhap
                            AppPrefsUtils.putString(Constants.KEY_USER_DATA, new Gson().toJson(mUser));

                            //Reset các field
                            binding.fragmentChangePasswordEtCurrent.setText(null);

                            binding.fragmentChangePasswordEtNew.setText(null);

                            binding.fragmentChangePasswordEtAgain.setText(null);

                            binding.parent.requestFocus();

                            Toast.makeText(getActivity()
                                    , R.string.txt_success_change_pass,Toast.LENGTH_SHORT)
                                    .show();
                        }else if (response.body().getStatus() == -1) {
                            Toast.makeText(getActivity(),R.string.err_wrong_pass,Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });


        }else{
            Boolean isFocused = false;

            if (TextUtils.isEmpty(binding.fragmentChangePasswordEtCurrent.getText().toString())) {
                binding.tilCurrentPass.setError(getString(R.string.err_empty_current_pass));

                if (!isFocused) {
                    binding.fragmentChangePasswordEtCurrent.requestFocus();

                    KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtCurrent);

                    isFocused = true;
                }
            }else {
                binding.tilCurrentPass.setError(null);
            }

            if (TextUtils.isEmpty(binding.fragmentChangePasswordEtNew.getText().toString())) {
                binding.tilNewPass.setError(getString(R.string.err_empty_new_pass));

                if (!isFocused) {
                    binding.fragmentChangePasswordEtNew.requestFocus();

                    KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtNew);

                    isFocused = true;
                }
            }else {
                binding.tilNewPass.setError(null);
            }

            if (TextUtils.isEmpty(binding.fragmentChangePasswordEtAgain.getText().toString())) {
                binding.tilReNewPass.setError(getString(R.string.err_empty_repass));

                if (!isFocused) {
                    binding.fragmentChangePasswordEtAgain.requestFocus();

                    KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtAgain);
                }
            }else {
                binding.tilReNewPass.setError(null);
            }

            if (!isEmptyField() && !isPasswordCorrect()) {
                binding.tilCurrentPass.setError(getString(R.string.err_wrong_pass));

                binding.fragmentChangePasswordEtCurrent.requestFocus();

                KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtCurrent);
            }else if (mUser.getPassword()
                    .equals(binding.fragmentChangePasswordEtNew.getText().toString())) {
                binding.tilNewPass.setError(getString(R.string.err_same_pass));

                binding.fragmentChangePasswordEtNew.requestFocus();

                KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtNew);
            }else if (!binding.fragmentChangePasswordEtNew.getText().toString()
                    .equals(binding.fragmentChangePasswordEtAgain.getText().toString())) {
                binding.tilReNewPass.setError(getString(R.string.err_wrong_confirm_pass));

                binding.fragmentChangePasswordEtAgain.requestFocus();

                KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtAgain);
            }
        }
    }

    private boolean isPasswordCorrect(){
        return mUser.getPassword()
                .equals(binding.fragmentChangePasswordEtCurrent.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_password:
                updatePassword();
                break;

            case R.id.btn_home_change_password:
                navController.navigate(
                        R.id.action_slide_menu_nav_change_password_to_slide_menu_nav_home);
                break;

        }
    }

    private boolean isEmptyField() {
        return TextUtils.isEmpty(binding.fragmentChangePasswordEtCurrent.getText().toString())
                || TextUtils.isEmpty(binding.fragmentChangePasswordEtNew.getText().toString())
                || TextUtils.isEmpty(binding.fragmentChangePasswordEtAgain.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();

        navController = Navigation.findNavController(getActivity()
                , R.id.nav_host_fragment_content_home);
    }
}