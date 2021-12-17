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

import com.r2s.notemanagementsystem.databinding.RowNoteBinding;
import com.r2s.notemanagementsystem.model.Note;
import com.r2s.notemanagementsystem.ui.dialog.FragmentDialogInsertNote;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> mNotes;
    private Context mContext;

    /**
     * Constructor with 2 parameters
     * @param mNotes List<Note>
     * @param context Context
     */
    public NoteAdapter(List<Note> mNotes, Context context) {
        this.mNotes = mNotes;
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
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return a new holder instance
        return new NoteViewHolder(RowNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    /**
     * Replace the contents of a view
     * @param holder the viewHolder
     * @param position the view position in the viewHolder list
     */
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(mNotes.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("note_id", mNotes.get(holder.getAdapterPosition()).getUserId());
                bundle.putString("note_name", mNotes.get(holder.getAdapterPosition()).getName());
                bundle.putString("category_name", mNotes.get(holder.getAdapterPosition()).getCategory());
                bundle.putString("priority_name", mNotes.get(holder.getAdapterPosition()).getPriority());
                bundle.putString("status_name", mNotes.get(holder.getAdapterPosition()).getStatus());
                bundle.putString("plan_date", mNotes.get(holder.getAdapterPosition()).getPlanDate());
                bundle.putString("created_date", mNotes.get(holder.getAdapterPosition()).getCreatedDate());

                final FragmentDialogInsertNote insertNote = new FragmentDialogInsertNote();
                insertNote.setArguments(bundle);

                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                insertNote.show(fm, "NoteDialog");
            }
        });
    }

    /**
     * Return the size of your dataset
     * @return size of your dataset
     */
    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    /**
     * This method updates the data list and notify the changes
     * @param mNotes List<Note>
     */
    public void setNotes(List<Note> mNotes) {
        this.mNotes = mNotes;
        notifyDataSetChanged();
    }

    /**
     * This method return the datalist
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

        public void bind(Note note) {
            String noteName = "Name: " + note.getName();
            String categoryName = "Category: " + note.getCategory();
            String priorityName = "Priority: " + note.getPriority();
            String statusName = "Status: " + note.getStatus();
            String planDate = "Plan Date: " + note.getPlanDate();
            String createdDate = "Created Date: " + note.getCreatedDate();

            binding.tvNoteName.setText(noteName);
            binding.tvNoteCategoryName.setText(categoryName);
            binding.tvNotePriorityName.setText(priorityName);
            binding.tvNoteStatusName.setText(statusName);
            binding.tvNotePlanDate.setText(planDate);
            binding.tvNoteCreatedDate.setText(createdDate);

        }
    }
}
