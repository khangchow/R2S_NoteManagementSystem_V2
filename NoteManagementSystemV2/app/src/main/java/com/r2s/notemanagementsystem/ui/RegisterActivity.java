package com.r2s.notemanagementsystem.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.databinding.ActivityRegisterBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        initEvents();
    }

    /**
     * This method created to show login activity
     *
     * @param v is view
     */
    public void showLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method created to check inputs filed & display errors
     *
     * @param user is user
     * @return true or false
     */
    private Boolean validateInput(User user) {
        String passwordConfirm = "";
        if (binding.activityRegisterEtPasswordConfirm.getText() != null) {
            passwordConfirm = binding.activityRegisterEtPasswordConfirm
                    .getText().toString().trim();
        }

        if (TextUtils.isEmpty(user.getFirstName())) {
            binding.activityRegisterEtFName.setError(getResources().getString(R.string.et_fName_empty));
            binding.activityRegisterEtFName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(user.getLastName())) {
            binding.activityRegisterEtLName.setError(getResources().getString(R.string.et_lName_empty));
            binding.activityRegisterEtLName.requestFocus();
            return false;
        }
        if (!user.getEmail().matches(Constants.emailPattern)) {
            binding.activityRegisterEtEmail.setError(getResources().getString(R.string.et_email_invalid));
            binding.activityRegisterEtEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            binding.activityRegisterEtPassword.setError(getResources().getString(R.string.et_pwd_invalid));
            binding.activityRegisterEtPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(passwordConfirm)
                || !TextUtils.equals(user.getPassword(), passwordConfirm)) {
            binding.activityRegisterEtPasswordConfirm.setError(getResources().getString(R.string.et_pwd_confirm_invalid));
            binding.activityRegisterEtPasswordConfirm.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * This method created to register events
     */
    public void initEvents() {
        EditText[] etArr = {
                binding.activityRegisterEtFName,
                binding.activityRegisterEtLName,
                binding.activityRegisterEtEmail,
                binding.activityRegisterEtPassword,
                binding.activityRegisterEtPasswordConfirm
        };
        hideKeyboardOnFocusChange(etArr);

        binding.activityRegisterBtnRegister.setOnClickListener(v -> {
            User user = new User();

            if (binding.activityRegisterEtFName.getText() != null)
                user.setFirstName(binding.activityRegisterEtFName.getText().toString().trim());

            if (binding.activityRegisterEtLName.getText() != null)
                user.setLastName(binding.activityRegisterEtLName.getText().toString().trim());

            if (binding.activityRegisterEtEmail.getText() != null)
                user.setEmail(binding.activityRegisterEtEmail.getText().toString().trim());

            if (binding.activityRegisterEtPassword.getText() != null)
                user.setPassword(binding.activityRegisterEtPassword.getText().toString().trim());

            if (validateInput(user)) {
                userViewModel.signUp(user).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@Nullable Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                        if (response.body() != null)
                            registerUI(response.body(), user);
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {

                    }
                });
            }
        });
    }

    /**
     * This method created to display ui
     *
     * @param res  is response
     * @param user is user
     */
    public void registerUI(BaseResponse res, User user) {
        if (res.getStatus() == Constants.REGISTER_ERR) {
            if (res.getError() == Constants.REGISTER_EMAIL_EXISTS_ERR) {
                binding.activityRegisterEtEmail.setError(getResources().getString(R.string.et_email_exists));
                binding.activityRegisterEtEmail.requestFocus();
            }
        } else if (res.getStatus() == Constants.REGISTER_SUCCESS) {
            Toast.makeText(this, getResources().getString(R.string.user_created), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getBaseContext(), LoginActivity.class);

            intent.putExtra(UserConstant.KEY_GMAIL
                    , user.getEmail());

            intent.putExtra(UserConstant.KEY_PASS
                    , user.getPassword());

            startActivity(intent);

            finish();
        }
    }

    /**
     * This method created to clear focus & hide keyboard on outside tap
     */
    public void hideKeyboardOnFocusChange(EditText[] etArr) {
        for (EditText et : etArr) {
            et.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            });
        }
    }
}