package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.os.Bundle;
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
        String strPassword = binding.fragmentChangePasswordEtNew.getText().toString().trim();

        if(binding.fragmentChangePasswordEtNew.getText().toString().trim()
                .equals(binding.fragmentChangePasswordEtAgain.getText().toString().trim())
        && isPasswordCorrect()
                && !mUser.equals(binding.fragmentChangePasswordEtNew.getText().toString())){

            mUser.setPassword(strPassword);

            //Doi profile tren database
//            mUserViewModel.updateUser(mUser);

            //Doi profile cua thong tin dang nhap
            AppPrefsUtils.putString(Constants.KEY_USER_DATA, new Gson().toJson(mUser));

            //Reset các field
            binding.fragmentChangePasswordEtCurrent.setText(null);

            binding.fragmentChangePasswordEtNew.setText(null);

            binding.fragmentChangePasswordEtAgain.setText(null);

            Toast.makeText(getActivity(),"Change password successfully",Toast.LENGTH_SHORT)
                    .show();
        }else{
            Toast.makeText(getActivity(),"Change password error",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isPasswordCorrect(){
        Log.d("PASS", mUser.getPassword()+" "+binding.fragmentChangePasswordEtCurrent.getText().toString());
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

    @Override
    public void onStart() {
        super.onStart();

        navController = Navigation.findNavController(getActivity()
                , R.id.nav_host_fragment_content_home);
    }
}