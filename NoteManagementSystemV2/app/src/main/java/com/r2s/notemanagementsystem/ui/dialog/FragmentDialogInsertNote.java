package com.r2s.notemanagementsystem.ui.dialog;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.NoteAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.constant.NoteConstant;
import com.r2s.notemanagementsystem.databinding.DialogFragmentInsertNoteBinding;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.model.Note;
import com.r2s.notemanagementsystem.model.User;
import com.r2s.notemanagementsystem.utils.AppPrefsUtils;
import com.r2s.notemanagementsystem.utils.CommunicateViewModel;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;
import com.r2s.notemanagementsystem.viewmodel.NoteViewModel;
import com.r2s.notemanagementsystem.viewmodel.PriorityViewModel;
import com.r2s.notemanagementsystem.viewmodel.StatusViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDialogInsertNote extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "NoteDiaLog";
    private NoteViewModel mNoteViewModel;
    private DialogFragmentInsertNoteBinding binding;
    private NoteAdapter mNoteAdapter;
    private final List<Note> mNotes = new ArrayList<>();

    List<String> listStringCate = new ArrayList<>();
    List<String> listStringPri = new ArrayList<>();
    List<String> listStringSta = new ArrayList<>();

    private CategoryViewModel mCateViewModel;
    private PriorityViewModel mPriorityViewModel;
    private StatusViewModel mStatusViewModel;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private Context mContext;
    private CommunicateViewModel mCommunicateViewModel;

    String strCategoryName, strPriorityName, strStatusName;
    String strPlanDate = "";

    /**
     * This method will get instance of this fragment dialog
     *
     * @return this fragment dialog
     */
    public static FragmentDialogInsertNote newInstance() {
        return new FragmentDialogInsertNote();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);



    }

    /**
     * Method will be called when view created
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentInsertNoteBinding.inflate(inflater, container, false);

        setUserInfo();

        mCateViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        mPriorityViewModel = new ViewModelProvider(this).get(PriorityViewModel.class);
        mStatusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mPriorityViewModel.refreshData();
        mStatusViewModel.refreshData();
        mCateViewModel.refreshData();
        mNoteViewModel.refreshData();

        return binding.getRoot();
    }

    /**
     * This method is called  the onCreateView() method
     *
     * @param view               View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViewModel();

        initView(view);

        setOnClickListener();

        eventItemClick();
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClickListener() {
        binding.dialogInsert.setOnClickListener(this);
        binding.dialogClose.setOnClickListener(this);
        binding.showDatePicker.setOnClickListener(this);
    }

    /**
     * This method set the ViewModel
     */
    private void setViewModel() {

        mNoteAdapter = new NoteAdapter(mNotes, this.getContext());

        mNoteViewModel.getAllNotes()
                .observe(getViewLifecycleOwner(), notes -> {
                    mNoteAdapter.setNotes(notes);
                });
    }

    /**
     * This method sets on-click actions for views
     *
     * @param view current view of the activity/fragment
     */

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_insert:
                if (isFilled()) {

                    final String name = Objects.requireNonNull(Objects.requireNonNull(binding.tfNoteName.getEditText()).getText())
                            .toString();

                    mNoteViewModel.addNote(name, strPriorityName, strCategoryName, strStatusName, strPlanDate).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<BaseResponse> call,
                                               @NonNull Response<BaseResponse> response) {
                            if (response.isSuccessful()) {
                                BaseResponse baseResponse = response.body();
                                assert baseResponse != null;

                                if (baseResponse.getStatus() == 1) {
                                    mCommunicateViewModel.makeChanges();

                                    Toast.makeText(mContext, "Create Note Successful!",
                                            Toast.LENGTH_SHORT).show();

                                } else if (baseResponse.getStatus() == -1)
                                    if (baseResponse.getError() == 2) {

                                        Toast.makeText(mContext,
                                                "This name already exists",
                                                Toast.LENGTH_SHORT).show();
                                    }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(mContext, "Create Note Failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    return;
                }
                dismiss();
                break;
            case R.id.dialog_close:
                dismiss();
                break;
            case R.id.show_date_picker:
                setUpDatePicker();
                break;
        }
    }

    /**
     * this method set items for auto complete
     *
     * @param view
     */
    public void initView(View view) {
        //auto complete category
        mCateViewModel.getCateById().observe(getViewLifecycleOwner(), categories -> {
            for (int i = 0; i < categories.size(); i++) {
                listStringCate.add(categories.get(i).getNameCate());
                binding.autoCompleteCategory.setText(listStringCate.get(0), false);
            }
        });

        ArrayAdapter<String> adapterItemCategory = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringCate);
        binding.autoCompleteCategory.setAdapter(adapterItemCategory);

        // auto complete for priority
        mPriorityViewModel.getAllPriorities().observe(getViewLifecycleOwner(), priorities -> {
            for (int i = 0; i < priorities.size(); i++) {
                listStringPri.add(priorities.get(i).getName());
                binding.autoCompletePriority.setText(listStringPri.get(0), false);
            }
        });

        ArrayAdapter<String> adapterItemPriority = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringPri);
        binding.autoCompletePriority.setAdapter(adapterItemPriority);

        // auto complete for status
        mStatusViewModel.getAllStatuses().observe(getViewLifecycleOwner(), statuses -> {
            for (int i = 0; i < statuses.size(); i++) {
                listStringSta.add(statuses.get(i).getName());
                binding.autoCompleteStatus.setText(listStringSta.get(0), false);
            }
        });

        ArrayAdapter<String> adapterItemStatus = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringSta);
        binding.autoCompleteStatus.setAdapter(adapterItemStatus);

        // Show date when choose date inside date picker
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                Log.d(TAG, "onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);
                String date = year + "-" + month + "-" + day;

                binding.tvDatePlan.setText(date);

                strPlanDate = date;
            }
        };
    }

    /**
     * this method with get text when click on item autocomplete
     */
    public void eventItemClick() {

        binding.autoCompleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strCategoryName = parent.getItemAtPosition(position).toString();
            }
        });

        binding.autoCompletePriority.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strPriorityName = parent.getItemAtPosition(position).toString();
            }
        });

        binding.autoCompleteStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strStatusName = parent.getItemAtPosition(position).toString();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * set user
     */
    private void setUserInfo() {
        User mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }

    /**
     * check if these field is filled
     *
     * @return boolean
     */
    public boolean isFilled() {
        boolean result = true;
        if (binding.tfNoteName.getEditText().getText().toString().trim().length() <= 0) {
            binding.tfNoteName.setError(NoteConstant.NOTE_ERROR);
            result = false;
        }
        if (strPlanDate.trim().length() < 2) {
            binding.tvDatePlan.setError(NoteConstant.NOTE_ERROR);
            result = false;
        }
        return result;
    }

    /**
     * this method will set up for date picker
     */
    public void setUpDatePicker() {
        Calendar kal = Calendar.getInstance();

        int year = kal.get(Calendar.YEAR);
        int month = kal.get(Calendar.MONTH);
        int day = kal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                dateSetListener, year, month, day);

        dialog.show();
    }

}