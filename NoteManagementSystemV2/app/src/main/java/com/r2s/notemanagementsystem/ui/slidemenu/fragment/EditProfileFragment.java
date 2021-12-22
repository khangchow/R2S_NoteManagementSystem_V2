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
import com.r2s.notemanagementsystem.databinding.FragmentEditProfileBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;

import retrofit2.Call;

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

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //Lay thong tin user tu phien dang nhap
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
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

    private void editProfile(){
        String strFirstName = binding.etFirstName.getText().toString();

        String strLastName = binding.etLastName.getText().toString();

        if (TextUtils.isEmpty(strFirstName) || TextUtils.isEmpty(strLastName)){
            return;
        }

        mUser.setFirstName(strFirstName);

        mUser.setLastName(strLastName);

        //Doi profile tren database
//        mUserViewModel.updateUser(mUser);
//        Call<BaseResponse> call = mUserViewModel();

        //Doi profile cua thong tin dang nhap
        AppPrefsUtils.putString(Constants.KEY_USER_DATA, new Gson().toJson(mUser));

        //Reset các fields
        binding.etFirstName.setText(null);

        binding.etLastName.setText(null);

        Toast.makeText(getContext(),"Update user successfully",Toast.LENGTH_SHORT).show();
    }
}
