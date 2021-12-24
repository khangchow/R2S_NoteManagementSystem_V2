package com.r2s.notemanagementsystem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r2s.notemanagementsystem.databinding.RowNoteBinding;
import com.r2s.notemanagementsystem.model.Note;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> mNotes;
    private final Context mContext;

    /**
     * @param mNotes  list note to set recyclerview
     * @param context context
     */
    public NoteAdapter(List<Note> mNotes, Context context) {
        this.mNotes = mNotes;
        this.mContext = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return a new holder instance
        return new NoteViewHolder(RowNoteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(mNotes.get(position));
    }

    /**
     * Return the size of your dataset
     *
     * @return size of your dataset
     */
    @Override
    public int getItemCount() {
        if (mNotes == null) {
            return 0;
        }
        return mNotes.size();
    }


    public void setNotes(List<Note> mNotes) {
        this.mNotes = mNotes;
        notifyDataSetChanged();
    }

    /**
     * This method return the datalist
     *
     * @return List
     */
    public List<Note> getNotes() {
        return mNotes;
    }

    /**
     * This class is used to hold all information of a single RecyclerView item
     */
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final RowNoteBinding binding;

        public NoteViewHolder(@NonNull RowNoteBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Note note) {
            String noteName = note.getName();
            String priorityName = note.getPriority();
            String categoryName = note.getCategory();
            String statusName = note.getStatus();
            String planDate = note.getPlanDate();
            String createdDate = note.getCreatedDate();
            
            binding.tvNoteName.setText(noteName);
            binding.tvNotePriorityName.setText(priorityName);
            binding.tvNoteCategoryName.setText(categoryName);
            binding.tvNoteStatusName.setText(statusName);
            binding.tvNotePlanDate.setText(planDate);
            binding.tvNoteCreatedDate.setText(createdDate);
        }
    }
}
