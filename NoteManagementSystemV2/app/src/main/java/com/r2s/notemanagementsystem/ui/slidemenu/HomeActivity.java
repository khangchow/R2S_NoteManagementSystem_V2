package com.r2s.notemanagementsystem.ui.slidemenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.UserConstant;
import com.r2s.notemanagementsystem.databinding.ActivityHomeBinding;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.ui.LoginActivity;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.viewmodel.UserViewModel;

public class HomeActivity extends AppCompatActivity {
    private User mUser;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private View header;
    private TextView tvName, tvGmail;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        //Lay thong tin user tu phien dang nhap
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);

        setUpSlideMenu();

        initHeaderViews();

        setUserDataToHeader();

        setUpViewModel();
    }

    private void setUpSlideMenu() {
        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.slide_menu_nav_home, R.id.slide_menu_nav_category
                , R.id.slide_menu_nav_priority, R.id.slide_menu_nav_status
                , R.id.slide_menu_nav_note, R.id.slide_menu_nav_edit_profile
                , R.id.slide_menu_nav_change_password)
                .setOpenableLayout(drawer)
                .build();

        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_home);

        NavigationUI.setupActionBarWithNavController(this
                , navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mUserViewModel.isUserUpdated().observe(this, isUserUpdated -> {
            if (isUserUpdated) {
                mUser = new Gson()
                        .fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);

                setUserDataToHeader();
            }
        });
    }

    private void setUserDataToHeader() {
        tvName.setText(mUser.getLastName() + " " + mUser.getFirstName());

        tvGmail.setText(mUser.getEmail());
    }

    private void initHeaderViews() {
        header = binding.navView.getHeaderView(0);

        tvName = header.findViewById(R.id.nav_header_tv_name);

        tvGmail = header.findViewById(R.id.nav_header_tv_gmail);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this
                , R.id.nav_host_fragment_content_home);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    private void logout() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);

        dialog.setMessage(R.string.txt_confirm_logout)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    AppPrefsUtils.putString(UserConstant.KEY_USER_DATA, null);

                    AppPrefsUtils.putString(UserConstant.KEY_REMEMBER_USER
                            , new Gson().toJson("false"));

                    startActivity(new Intent(this, LoginActivity.class));

                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}