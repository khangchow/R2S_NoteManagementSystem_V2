package com.r2s.notemanagementsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.databinding.ActivityLoginBinding;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.ui.slidemenu.HomeActivity;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppPrefsUtils.createAppPrefs(this);

        showMainActivity();
        if(isRememberUser(AppPrefsUtils.getString(UserConstant.KEY_REMEMBER_USER))) {
            showMainActivity();
        }else {
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
     * @param v View class
     * @return void
     */
    public void showRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method created to show main activity
     */
    public void showMainActivity(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public Boolean isRememberUser(String isRememberTxt){
        if(TextUtils.equals("\"true\"", isRememberTxt))
            return true;
        else
            return false;
    }

    /**
     * This method created to register events
     */
    public void initEvents(){
        binding.activityLoginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setEmail(binding.activityLoginEtEmail.getText().toString().trim());
                user.setPassword(binding.activityLoginEtPassword.getText().toString().trim());

//                userViewModel.login(user.getEmail(), user.getPassword()).observe(LoginActivity.this, new Observer<User>() {
//                    @Override
//                    public void onChanged(User mUser) {
//                        if(mUser==null) {
//                            Toast.makeText(getBaseContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
//                            binding.activityLoginEtPassword.setText(null);
//                            binding.activityLoginEtEmail.requestFocus();
//                        }
//                        else{
//                            AppPrefsUtils.putString(UserConstant.KEY_USER_DATA
//                                    , new Gson().toJson(mUser));
//
//                            if(binding.activityLoginChkRemember.isChecked())
//                                AppPrefsUtils.putString(UserConstant.KEY_REMEMBER_USER
//                                        , new Gson().toJson("true"));
//                            else
//                                AppPrefsUtils.putString(UserConstant.KEY_REMEMBER_USER
//                                        , new Gson().toJson("false"));
//
//
//                            Toast.makeText(getBaseContext(), getResources().getString(R.string.logged_in), Toast.LENGTH_SHORT).show();
//                            showMainActivity();
//                        }
//                    }
//                });
            }
        });
        binding.activityLoginBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}