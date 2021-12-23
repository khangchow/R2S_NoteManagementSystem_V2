package com.r2s.notemanagementsystem.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.r2s.notemanagementsystem.databinding.RowPriorityBinding;
import com.r2s.notemanagementsystem.model.Priority;
import com.r2s.notemanagementsystem.ui.dialog.EditPriorityDialog;

import java.util.List;

public class PriorityAdapter extends RecyclerView.Adapter<PriorityAdapter.PriorityViewHolder> {

    private List<Priority> mPriorities;
    private Context mContext;

    /**
     * Constructor with 1 parameter
     * @param context Context
     */
    public PriorityAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Constructor with 2 parameters
     * @param mPriorities List<Priority>
     * @param context Context
     */
    public PriorityAdapter(List<Priority> mPriorities, Context context) {
        this.mPriorities = mPriorities;
        this.mContext = context;
    }

    /**
     * Create new view
     * @param parent
     * @param viewType
     * @return new view
     */
    @NonNull
    @Override
    public PriorityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return a new holder instance
        return new PriorityViewHolder(RowPriorityBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    /**
     * Replace the contents of a view
     * @param holder the viewHolder
     * @param position the view position in the viewHolder list
     */
    @Override
    public void onBindViewHolder(@NonNull PriorityViewHolder holder, int position) {
        holder.bind(mPriorities.get(position));
    }

    /**
     * Return the size of your dataset
     * @return size of your dataset
     */
    @Override
    public int getItemCount() {
        if (mPriorities == null) {
            return 0;
        }
        return mPriorities.size();
    }

    /**
     * This method updates the data list and notify the changes
     * @param mPriorities List<Priority>
     */
    public void setPriorities(List<Priority> mPriorities) {
        this.mPriorities = mPriorities;
        notifyDataSetChanged();
    }

    /**
     * This method return the datalist
     * @return List
     */
    public List<Priority> getPriorities() {
        return mPriorities;
    }

    /**
     * This class is used to hold all information of a single RecyclerView item
     */
    public class PriorityViewHolder extends RecyclerView.ViewHolder {
        private final RowPriorityBinding binding;

        public PriorityViewHolder(@NonNull RowPriorityBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Priority priority) {
            String priorityName = priority.getName();
            String priorityCreatedDate = priority.getCreatedDate();
            String priorityEmail = priority.getUserEmail();

            binding.tvPriorityName.setText(priorityName);
            binding.tvPriorityCreatedDate.setText(priorityCreatedDate);
        }
    }
}