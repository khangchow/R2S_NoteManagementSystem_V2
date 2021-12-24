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
import com.r2s.notemanagementsystem.databinding.FragmentChangePasswordBinding;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.KeyboardUtils;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;


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
    }

    private void setOnClick() {
        binding.btnChangePassword.setOnClickListener(this);

        binding.btnHomeChangePassword.setOnClickListener(this);
    }

    private void updatePassword() {
        if(!isEmptyField() && isPasswordCorrect()
                && !mUser.getPassword()
                .equals(binding.fragmentChangePasswordEtNew.getText().toString())
                && binding.fragmentChangePasswordEtNew.getText().toString()
                .equals(binding.fragmentChangePasswordEtAgain.getText().toString())){

            mUser.setPassword(binding.fragmentChangePasswordEtNew.getText().toString());

            //Doi profile cua thong tin dang nhap
            AppPrefsUtils.putString(Constants.KEY_USER_DATA, new Gson().toJson(mUser));

            //Reset các field
            binding.fragmentChangePasswordEtCurrent.setText(null);

            binding.fragmentChangePasswordEtNew.setText(null);

            binding.fragmentChangePasswordEtAgain.setText(null);

            Toast.makeText(getActivity(),"Change password successfully",Toast.LENGTH_SHORT)
                    .show();
        }else{
            Boolean isFocused = false;

            if (TextUtils.isEmpty(binding.fragmentChangePasswordEtCurrent.toString())) {
                binding.tilCurrentPass.setError(getString(R.string.err_empty_current_pass));

                if (!isFocused) {
                    binding.fragmentChangePasswordEtCurrent.requestFocus();

                    KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtCurrent);

                    isFocused = true;
                }
            }else {
                binding.tilCurrentPass.setError(null);
            }

            if (TextUtils.isEmpty(binding.fragmentChangePasswordEtNew.toString())) {
                binding.tilNewPass.setError(getString(R.string.err_empty_new_pass));

                if (!isFocused) {
                    binding.fragmentChangePasswordEtNew.requestFocus();

                    KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtNew);

                    isFocused = true;
                }
            }else {
                binding.tilNewPass.setError(null);
            }

            if (TextUtils.isEmpty(binding.fragmentChangePasswordEtAgain.toString())) {
                binding.tilReNewPass.setError(getString(R.string.err_empty_repass));

                if (!isFocused) {
                    binding.fragmentChangePasswordEtAgain.requestFocus();

                    KeyboardUtils.openKeyboard(binding.fragmentChangePasswordEtAgain);
                }
            }else {
                binding.tilReNewPass.setError(null);
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
        return TextUtils.isEmpty(binding.fragmentChangePasswordEtCurrent.toString())
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