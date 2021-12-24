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

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.databinding.ActivityLoginBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.ui.slidemenu.HomeActivity;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppPrefsUtils.createAppPrefs(this);

        if (isRememberUser(AppPrefsUtils.getString(UserConstant.KEY_REMEMBER_USER))) {
            showMainActivity();
        } else {
            userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

            initEvents();

            if (getIntent().hasExtra(UserConstant.KEY_GMAIL)
                    && getIntent().hasExtra(UserConstant.KEY_PASS)) {
                Intent intent = getIntent();

                binding.activityLoginEtEmail.setText(intent.getStringExtra(UserConstant.KEY_GMAIL));

                binding.activityLoginEtPassword.setText(intent.getStringExtra(UserConstant.KEY_PASS));
            }
        }
    }

    /**
     * This method created to show register activity
     *
     * @param v View class
     */
    public void showRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method created to show main activity
     */
    public void showMainActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * @param isRememberTxt is true or false string
     * @return true if isRememberTxt = "true"
     */
    public Boolean isRememberUser(String isRememberTxt) {
        return TextUtils.equals("\"true\"", isRememberTxt);
    }

    /**
     * This method created to register events
     */
    public void initEvents() {
        EditText[] etArr = {
                binding.activityLoginEtEmail,
                binding.activityLoginEtPassword
        };
        hideKeyboardOnFocusChange(etArr);

        binding.activityLoginBtnLogin.setOnClickListener(v -> {
            User user = new User();
            if (binding.activityLoginEtEmail.getText() != null)
                user.setEmail(binding.activityLoginEtEmail.getText().toString().trim());
            if (binding.activityLoginEtPassword.getText() != null)
                user.setPassword(binding.activityLoginEtPassword.getText().toString().trim());

            if (validateInput(user))

                userViewModel.login(user).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@Nullable Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                        if (response.body() != null)
                            loginUI(response.body(), user);
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {

                    }
                });
        });

        binding.activityLoginBtnExit.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
    }

    /**
     * This method created to validate input
     *
     * @param user is user
     * @return is true if all fields valid
     */
    public Boolean validateInput(User user) {
        if (!user.getEmail().matches(Constants.emailPattern)) {
            binding.activityLoginEtEmail.setError(getResources().getString(R.string.et_email_invalid));
            binding.activityLoginEtEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            Toast.makeText(this, getResources().getString(R.string.et_pwd_invalid), Toast.LENGTH_LONG).show();
            binding.activityLoginEtPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * @param res  is api response
     * @param user is this user
     */
    public void loginUI(BaseResponse res, User user) {
        if (res.getStatus() == Constants.LOGIN_ERR) {
            if (res.getError() == Constants.LOGIN_PWD_INCORRECT_ERR) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.login_pwd_incorrect), Toast.LENGTH_LONG).show();
                binding.activityLoginEtPassword.setText("");
                binding.activityLoginEtPassword.requestFocus();
            }else if (res.getError() == Constants.LOGIN_USER_NOT_FOUND_ERR) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                binding.activityLoginEtEmail.requestFocus();
                binding.activityLoginEtPassword.setText("");
            }
        }else if (res.getStatus() == Constants.LOGIN_SUCCESS) {
            user.setFirstName(res.getInfo().getFirstName());
            user.setLastName(res.getInfo().getLastName());

            AppPrefsUtils.putString(UserConstant.KEY_USER_DATA, new Gson().toJson(user));

            if (binding.activityLoginChkRemember.isChecked())
                AppPrefsUtils.putString(UserConstant.KEY_REMEMBER_USER
                        , new Gson().toJson("true"));
            else
                AppPrefsUtils.putString(UserConstant.KEY_REMEMBER_USER
                        , new Gson().toJson("false"));

            showMainActivity();
            Toast.makeText(getBaseContext(), getResources().getString(R.string.logged_in), Toast.LENGTH_LONG).show();
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