package com.r2s.notemanagementsystem.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.adapter.CategoryAdapter;
import com.r2s.notemanagementsystem.databinding.DialogEditCategoryBinding;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.viewmodel.CategoryViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditCategoryDialog extends DialogFragment implements View.OnClickListener {
    private CategoryViewModel mCateViewModel;
    private DialogEditCategoryBinding binding;
    private CategoryAdapter mCateAdapter;
    private final ArrayList<Category> categoryArrayList = new ArrayList<>();
    private Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DialogEditCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCateViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        mCateAdapter = new CategoryAdapter(categoryArrayList, this.getContext());

        bundle = getArguments();
        if (bundle != null) {
            binding.btnUpdate.setText("Update");
            binding.etEditCate.setText(bundle.getString("priority_name" ));
        }

        mCateViewModel.loadAllCate().observe(getViewLifecycleOwner(), categories -> {
            mCateAdapter.setTasks(categories);
        });

        binding.btnUpdate.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);

        setCancelable(false);

    }

    /**
     * Handling the button
     * @param v
     */
    @Override
    public void onClick(View v) {
        int cateId = 1;
        switch (v.getId()){
            case R.id.btnUpdate:
                if (binding.btnUpdate.getText().toString().equalsIgnoreCase("update")) {
                    int updateId = bundle.getInt("cate_id");
                    String currentTime = getCurrentTime();
                    Category category = new Category(updateId, binding.etEditCate.getText().toString(), currentTime);

                    mCateViewModel.updateCate(category);
                    dismiss();
                }
                break;
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
