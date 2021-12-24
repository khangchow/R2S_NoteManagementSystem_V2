package com.r2s.notemanagementsystem.ui.dialog;

import static com.r2s.notemanagementsystem.ui.dialog.FragmentDialogInsertNote.TAG;

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
import com.r2s.notemanagementsystem.viewmodel.NoteViewModel;

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
     * @param inflater LayoutInflater
     * @param container ViewGroup
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
     * @param view View
     * @param savedInstanceState Bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        mAdapter = new NoteAdapter(mNotes, this.getContext());

        initView(view);
        eventItemClick();
        setOnClicks();

        bundle = getArguments();
        if (bundle != null) {
            binding.tfNoteName2.getEditText().setText(bundle.getString("note_name"));
        }
    }

    /**
     * This method sets on-click listener for views
     */
    public void setOnClicks() {
        binding.btnUpdateNote.setOnClickListener(this);
        binding.btnCloseNote.setOnClickListener(this);
        binding.showDatePicker2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_note:
//                mNoteViewModel.editNote(bundle.getString("note_name"),
//                        binding.tfNoteName2.getEditText().getText().toString()).enqueue(
//                        new Callback<BaseResponse>() {
//                            @Override
//                            public void onResponse(Call<BaseResponse> call,
//                                                   Response<BaseResponse> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    BaseResponse baseResponse = response.body();
//                                    if (baseResponse.getStatus() == 1) {
//                                        mCommunicateViewModel.makeChanges();
//
//                                        Toast.makeText(context, "Update Successful!",
//                                                Toast.LENGTH_SHORT).show();
//                                        Log.d("RESUME", "Edit Success");
//                                    } else if (baseResponse.getStatus() == -1) {
//                                        if (response.body().getError() == Integer.getInteger(null)) {
//                                            Toast.makeText(context, "Update Failed!",
//                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                                mCommunicateViewModel.makeChanges();
//
//                                Toast.makeText(getActivity(), "Update Failed!",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                dismiss();
//                break;
            case R.id.btn_close_note:
                dismiss();
                break;
            case R.id.show_date_picker2:
                setUpDatePicker();
                break;
        }
    }

    public void initView(View view) {

        //auto complete category
//        mCateViewModel.loadAllCate().observe(getViewLifecycleOwner(), categories -> {
//            for (int i = 0; i < categories.size(); i++) {
//                listStringCate.add(categories.get(i).getNameCate());
//            }
//        });

        listStringCate.add("Working");
        listStringCate.add("Study");
        listStringCate.add("Relax");

        ArrayAdapter<String> adapterItemCategory = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringCate);
        binding.autoCompleteCategory2.setAdapter(adapterItemCategory);

        // auto complete for priority
//        mPriorityViewModel.getAllPriorities().observe(getViewLifecycleOwner(), priorities -> {
//            for (int i = 0; i < priorities.size(); i++) {
//                listStringPri.add(priorities.get(i).getName());
//            }
//        });

        listStringPri.add("High");
        listStringPri.add("Medium");
        listStringPri.add("Slow");
        ArrayAdapter<String> adapterItemPriority = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, listStringPri);
        binding.autoCompletePriority2.setAdapter(adapterItemPriority);

        // auto complete for status
//        mStatusViewModel.getAllStatusesByUserId().observe(getViewLifecycleOwner(), statuses -> {
//            for (int i = 0; i < statuses.size(); i++) {
//                listStringSta.add(statuses.get(i).getName());
//            }
//        });

        listStringSta.add("Done");
        listStringSta.add("Processing");


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
