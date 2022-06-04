package com.example.archexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.archexample.models.Note;
import com.example.archexample.R;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    //    private List<Note> notes = new ArrayList<>();
    private setOnNoteItemListener setOnNoteItemListener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };

    public void setSetOnNoteItemListener(NoteAdapter.setOnNoteItemListener setOnNoteItemListener) {
        this.setOnNoteItemListener = setOnNoteItemListener;
    }

//public void setNotes(List<Note> notes){
//        this.notes=notes;
//        notifyDataSetChanged();
////        notifyItemChanged(position - 1);
//        }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note_recycler, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }
//
//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }


    class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPriority;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (setOnNoteItemListener != null && getLayoutPosition() != RecyclerView.NO_POSITION) {
                        setOnNoteItemListener.onItemClick(getItem(getLayoutPosition()));
                    }
                }
            });
        }
    }

    public interface setOnNoteItemListener {
        void onItemClick(Note note);
    }

}
