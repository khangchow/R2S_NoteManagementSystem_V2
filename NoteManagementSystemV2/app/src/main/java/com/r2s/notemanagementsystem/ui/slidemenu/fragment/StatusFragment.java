package com.r2s.notemanagementsystem.ui.slidemenu.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.StatusAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.StatusConstant;
import com.r2s.notemanagementsystem.databinding.FragmentStatusBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Status;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.repository.StatusRepository;
import com.r2s.notemanagementsystem.service.StatusService;
import com.r2s.notemanagementsystem.ui.dialog.EditStatusDialog;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.ui.dialog.AddStatusDialog;
import com.r2s.notemanagementsystem.utils.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.StatusViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment implements View.OnClickListener {
    private FragmentStatusBinding binding;
    private StatusViewModel mStatusViewModel;
    private StatusAdapter mStatusAdapter;
    private List<Status> mStatuses = new ArrayList<>();
    private User mUser;
    private Context mContext;
    private CommunicateViewModel mCommunicateViewModel;
    private StatusService mStatusService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method is used for non-graphical initialisations
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setUserInfo();
    }

    /**
     * This method is used for graphical initialisations
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusBinding.inflate(getLayoutInflater());

        setUserInfo();

        mStatusService = StatusRepository.getService();

        mCommunicateViewModel =
                new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needReloading -> {
            Log.d("RESUME_FRAGMENT", needReloading.toString());
            if (needReloading) {
                onResume();
            }
        });

        binding.fabOpenStatus.setOnClickListener(this);

        mStatusAdapter = new StatusAdapter(mStatuses, getContext());
        binding.rvStatus.setAdapter(mStatusAdapter);
        binding.rvStatus.setHasFixedSize(true);
        binding.rvStatus.setLayoutManager(new LinearLayoutManager(getContext()));

        mStatusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        mStatusViewModel.getAllStatuses().observe(getViewLifecycleOwner(), statuses -> {
            mStatusAdapter.setStatuses(statuses);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    // Swipe right to delete
                    case ItemTouchHelper.RIGHT:
                        int position = viewHolder.getAdapterPosition();
                        mStatuses = mStatusAdapter.getStatuses();
                        String statusName = mStatuses.get(position).getName();

                        new AlertDialog.Builder(requireContext()).setTitle("Confirm").setMessage(
                                "Do you really want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mStatusViewModel.deleteStatus(statusName).enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                        assert(response.body() != null);
                                        if (response.body().getStatus() == 1) {
                                            Toast.makeText(getContext(),
                                                    "Deleted",
                                                    Toast.LENGTH_SHORT).show();
                                            mStatusViewModel.refreshData();
                                        } else if (response.body().getStatus() == -1) {
                                            if (response.body().getError() == 2) {
                                                Toast.makeText(getContext(),
                                                        "Can't delete, " +
                                                                "because it's in use",
                                                        Toast.LENGTH_SHORT).show();
                                                mStatusViewModel.refreshData();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mStatusViewModel.refreshData();
                            }
                        }).create().show();
                        onResume();
                        break;

                    // Swipe left to update
                    case ItemTouchHelper.LEFT:
                        try {
                            position = viewHolder.getAdapterPosition();
                            mStatuses = mStatusAdapter.getStatuses();
                            statusName = mStatuses.get(position).getName();

                            Bundle bundle = new Bundle();
                            bundle.putString("status_name", statusName);

                            final EditStatusDialog editStatusDialog =
                                    EditStatusDialog.newInstance();
                            editStatusDialog.setArguments(bundle);

                            FragmentManager fm =
                                    ((AppCompatActivity) requireContext()).getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            editStatusDialog.show(ft, "EditStatusDialog");
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                        }
                        mStatusViewModel.refreshData();
                        break;
                }
            }
        }).attachToRecyclerView(binding.rvStatus);

        return binding.getRoot();
    }

    /**
     * This method is called when the user resume the view
     */
    @Override
    public void onResume() {
        super.onResume();

        mStatusService.getAllStatuses(StatusConstant.STATUS_TAB, mUser.getEmail()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Status> mStatuses = new ArrayList<>();
                    for (List<String> status : response.body().getData()) {
                        mStatuses.add(new Status(status.get(0), status.get(1), status.get(2)));
                    }
                    mStatusAdapter.setStatuses(mStatuses);
                    Log.d("RESUME_RELOAD", "Reload");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
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
     * This method sets on-click actions for views
     * @param view View
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_open_status:
                DialogFragment dialogFragment = AddStatusDialog.newInstance();
                dialogFragment.show(getChildFragmentManager(), AddStatusDialog.TAG);
        }
    }

    /**
     * This method get the user data from the SharedPreference
     */
    private void setUserInfo() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }
}