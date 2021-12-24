package com.r2s.notemanagementsystem.ui.dialog;

import static com.r2s.notemanagementsystem.ui.dialog.FragmentDialogInsertNote.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.NoteAdapter;
import com.r2s.notemanagementsystem.constant.Constants;
import com.r2s.notemanagementsystem.databinding.DialogEditNoteBinding;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditNoteDialog extends DialogFragment implements View.OnClickListener {
    private NoteViewModel mNoteViewModel;
    private DialogEditNoteBinding binding;
    private NoteAdapter mAdapter;
    private List<Note> mNotes = new ArrayList<>();
    private Bundle bundle;
    private User mUser;
    private Context context;

    List<String> listStringCate = new ArrayList<>();
    List<String> listStringPri = new ArrayList<>();
    List<String> listStringSta = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    String strCategoryName, strPriorityName, strStatusName;
    String strPlanDate = "";
    private CategoryViewModel mCateViewModel;
    private PriorityViewModel mPriorityViewModel;
    private StatusViewModel mStatusViewModel;


    private CommunicateViewModel mCommunicateViewModel;

    public static EditNoteDialog newInstance() {
        return new EditNoteDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * This method is called when a view is being created
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogEditNoteBinding.inflate(inflater, container, false);
        setUserInfo();
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        return binding.getRoot();
    }

    /**
     * This method is called after the onCreateView() method
     *
     * @param view               View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCateViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        mPriorityViewModel = new ViewModelProvider(this).get(PriorityViewModel.class);
        mStatusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mPriorityViewModel.refreshData();
        mStatusViewModel.refreshData();
        mCateViewModel.refreshData();
        mNoteViewModel.refreshData();

        mAdapter = new NoteAdapter(mNotes, this.getContext());


        eventItemClick();
        setOnClicks();

        bundle = getArguments();
        if (bundle != null) {

            binding.tfNoteName2.getEditText().setText(bundle.getString("note_name"));
            binding.autoCompletePriority2.setText(bundle.getString("priority_name"));
            binding.autoCompleteCategory2.setText(bundle.getString("category_name"));
            binding.autoCompleteStatus2.setText(bundle.getString("status_name"));
            binding.tvDatePlan2.setText(bundle.getString("plan_date"));
        }

        initView(view);
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClicks() {
        binding.btnUpdateNote.setOnClickListener(this);
        binding.btnCloseNote.setOnClickListener(this);
        binding.showDatePicker2.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_note:
                if (isEmpty()) {

                    mNoteViewModel.editNote(bundle.getString("note_name"),
                            binding.tfNoteName2.getEditText().getText().toString(), strPriorityName, strCategoryName, strStatusName, strPlanDate).enqueue(
                            new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call,
                                                       Response<BaseResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {

                                        BaseResponse baseResponse = response.body();

                                        if (baseResponse.getStatus() == 1) {
                                            mCommunicateViewModel.makeChanges();

                                            Toast.makeText(context, "Update Successful!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (baseResponse.getStatus() == -1) {

                                            Integer error = new Integer(baseResponse.getError());
                                            if (error.equals(null)) {
                                                Toast.makeText(context, "Update Note Failed!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    mCommunicateViewModel.makeChanges();

                                    Toast.makeText(getActivity(), "Update Failed!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                } else {
                    Toast.makeText(context, "Update Note Failed!!!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_close_note:
                dismiss();
                break;
            case R.id.show_date_picker2:
                setUpDatePicker();
                break;
        }
    }

    /**
     * Checking these field is filled
     *
     * @return boolean
     */
    public boolean isEmpty() {
        boolean result = true;
        if (binding.tfNoteName2.getEditText().getText().toString().length() <= 0) {
            result = false;
            binding.tfNoteName2.setError("This field can not empty!!!");
        }
        return result;
    }

    /**
     * Set view for auto complete
     *
     * @param view view
     */
    public void initView(View view) {

        //auto complete category
        mCateViewModel.getCateById().observe(getViewLifecycleOwner(), categories -> {
            for (int i = 0; i < categories.size(); i++) {
                listStringCate.add(categories.get(i).getNameCate());
                Log.d("TestAuto", categories.get(i).getNameCate());
            }
        });

        ArrayAdapter<String> adapterItemCategory = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringCate);
        binding.autoCompleteCategory2.setAdapter(adapterItemCategory);

        // auto complete for priority
        mPriorityViewModel.getAllPriorities().observe(getViewLifecycleOwner(), priorities -> {
            for (int i = 0; i < priorities.size(); i++) {
                listStringPri.add(priorities.get(i).getName());
                Log.d("TestAuto", priorities.get(i).getName());
            }
        });

        ArrayAdapter<String> adapterItemPriority = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringPri);
        binding.autoCompletePriority2.setAdapter(adapterItemPriority);

        // auto complete for status
        mStatusViewModel.getAllStatuses().observe(getViewLifecycleOwner(), statuses -> {
            for (int i = 0; i < statuses.size(); i++) {
                listStringSta.add(statuses.get(i).getName());
                Log.d("TestAuto", statuses.get(i).getName());
            }
        });

        ArrayAdapter<String> adapterItemStatus = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringSta);
        binding.autoCompleteStatus2.setAdapter(adapterItemStatus);

        // Show date when choose date inside date picker
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                Log.d(TAG, "onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);
                String date = year + "-" + month + "-" + day;

                binding.tvDatePlan2.setText(date);

                strPlanDate = date;
            }
        };
    }

    /**
     * Event click of auto complete
     */
    public void eventItemClick() {

        binding.autoCompleteCategory2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strCategoryName = parent.getItemAtPosition(position).toString();
            }
        });

        binding.autoCompletePriority2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strPriorityName = parent.getItemAtPosition(position).toString();
            }
        });

        binding.autoCompleteStatus2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strStatusName = parent.getItemAtPosition(position).toString();
            }
        });
    }

    /**
     * set date picker
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

    /**
     * This method get the user data from the SharedPreference
     */
    private void setUserInfo() {
        mUser = new Gson().fromJson(AppPrefsUtils.getString(Constants.KEY_USER_DATA), User.class);
    }

    /**
     * This method is called when the view is destroyed
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
