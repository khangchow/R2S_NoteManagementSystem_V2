package com.r2s.notemanagementsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r2s.notemanagementsystem.databinding.RowStatusBinding;
import com.r2s.notemanagementsystem.model.Status;

import java.util.List;
import java.lang.String;

public class StatusAdapter extends  RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private List<Status> mStatuses;
    private Context mContext;

    /**
     * Constructor with 1 parameter
     * @param mContext Context
     */
    public StatusAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Constructor with 2 parameters
     * @param mStatuses List
     * @param context Context
     */
    public StatusAdapter(List<Status> mStatuses, Context context) {
        this.mStatuses = mStatuses;
        this.mContext = context;
    }

    /**
     * Create new views
     * @param parent
     * @param viewType
     * @return new view
     */
    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return a new holder instance
        return new StatusViewHolder(RowStatusBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    /**
     * Replace the contents of a view
     * @param holder the viewHolder
     * @param position the view position in the viewHolder list
     */
    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        holder.bind(mStatuses.get(position));
    }

    /**
     * Return the size of your dataset
     * @return size of your dataset
     */
    @Override
    public int getItemCount() {
        return mStatuses.size();
    }

    public void setStatuses(List<Status> mStatuses) {
        this.mStatuses = mStatuses;
        notifyDataSetChanged();
    }

    public List<Status> getStatuses() {
        return mStatuses;
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {
        private RowStatusBinding binding;

        public StatusViewHolder(@NonNull RowStatusBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Status status) {
            String statusName = status.getName();
            String statusCreatedDate = status.getCreatedDate();
            String statusEmail = status.getUserEmail();

            binding.tvStatusName.setText(statusName);
            binding.tvStatusCreatedDate.setText(statusCreatedDate);
        }
    }
}
