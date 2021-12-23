package com.r2s.notemanagementsystem.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.r2s.notemanagementsystem.databinding.RowCategoryBinding;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.ui.dialog.EditCategoryDialog;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context context;
    ArrayList<Category> categoryArrayList;

    /**
     * Constructor with 1 param
     * @param context
     */
    public CategoryAdapter(Context context) {
        this.context = context;
    }

//    /**
//     * Constructor with 2 param
//     * @param categoryList
//     * @param context
//     */
//    public CategoryAdapter(List<Category> categoryList, Context context) {
//        this.categoryList = categoryList;
//        this.context = context;
//    }

    /**
     * Create new view
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(categoryArrayList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("cate_id", categoryArrayList.
                        get(holder.getAdapterPosition()).getCateId());
                bundle.putString("cate_name", categoryArrayList.
                        get(holder.getAdapterPosition()).getNameCate());

                final EditCategoryDialog categoryDialog = new EditCategoryDialog();
                categoryDialog.setArguments(bundle);

                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                categoryDialog.show(fm, "cate");
            }
        });
    }

    /**
     * Return size of Category list
     * @return
     */
    @Override
    public int getItemCount() {
        if (categoryArrayList == null) {
            return 0;
        }
        return categoryArrayList.size();
    }

    /**
     * This method updates the data list and notify the changes
     * @param information
     */
    public void setTasks(List<Category> information) {
        categoryArrayList = (ArrayList<Category>) information;
        notifyDataSetChanged();
    }

    /**
     * return date list
     * @return
     */
    public ArrayList<Category> getTasks() {
        return categoryArrayList;
    }

    /**
     * This class is used to hold all information of a single RecyclerView item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCate, tvCreatedDate;
        AppCompatButton btnEdit;

        RowCategoryBinding binding;

        public ViewHolder(@NonNull RowCategoryBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }

        public void bind(Category category) {
            String cateName = "Name: " + category.getNameCate();
            String cateDate = "Created Date: " + category.getCreatedDate();

            binding.tvNameCategory.setText(cateName);
            binding.tvCreatedDate.setText(cateDate);
        }
    }

}
