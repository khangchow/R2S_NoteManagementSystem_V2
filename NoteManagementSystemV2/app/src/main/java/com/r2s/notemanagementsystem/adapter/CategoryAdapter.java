package com.r2s.notemanagementsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r2s.notemanagementsystem.R;
import com.r2s.notemanagementsystem.constant.CategoryConstant;
import com.r2s.notemanagementsystem.databinding.RowCategoryBinding;
import com.r2s.notemanagementsystem.model.Category;
import com.r2s.notemanagementsystem.model.BaseResponse;
import com.r2s.notemanagementsystem.ui.dialog.AddNewCategoryDialog;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private Context context;
    private List<Category> categoryList;

    /**
     * Constructor with 1 param
     * @param context
     */
    public CategoryAdapter(Context context) {
        this.context = context;
    }


    /**
     * Create new view
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category br = categoryList.get(position);
        holder.tvNameCate.setText(br.getNameCate());
        holder.tvCreatedDate.setText(br.getCreatedDate());
    }

    /**
     * Return size of Category list
     * @return
     */
    @Override
    public int getItemCount() {
        if (categoryList == null) {
            return 0;
        }

        return categoryList.size();
    }

    /**
     * This method updates the data list and notify the changes
     * @param cateResponse
     */
    public void setCateAdapter(List<Category> cateResponse) {
        categoryList = cateResponse;
        notifyDataSetChanged();
    }

    /**
     * return date list
     * @return
     */
    public List<Category> getCateAdapter() {
        return categoryList;
    }

    /**
     * This class is used to hold all information of a single RecyclerView item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCate, tvCreatedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameCate = itemView.findViewById(R.id.tvNameCategory);
            tvCreatedDate = itemView.findViewById(R.id.tvCreatedDate);
        }
    }

}
