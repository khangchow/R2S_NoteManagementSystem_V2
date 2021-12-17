package com.r2s.notemanagementsystem.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddNewCategoryDialog extends DialogFragment implements View.OnClickListener {
    private TextInputEditText etNewCate;
    private AppCompatButton btnAdd, btnCancel;
    private CategoryViewModel mCateViewModel;

    public static AddNewCategoryDialog newInstance() {
        return new AddNewCategoryDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_new_category, container, false);


        //Reference
        etNewCate = view.findViewById(R.id.etNewCate);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);

        // Add event
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        // Setting modal dialog
        setCancelable(false);

        mCateViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String currentTime = getCurrentTime();
                Category category = new Category(0, etNewCate.getText().toString(), currentTime);
                if (etNewCate.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                } else {
                    mCateViewModel.insertCate(category);
                }
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    /**
     * get current time
     * @return
     */
    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
