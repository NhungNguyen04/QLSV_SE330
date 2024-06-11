package com.example.qlsv.ui.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv.R;
import com.example.qlsv.RecyclerViewInterface;

import org.w3c.dom.Text;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context mContext;
    private List<Note> mListNote;

    public NoteAdapter(Context mContext, RecyclerViewInterface recyclerViewInterface) {
        this.mContext = mContext;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<Note> list){
        this.mListNote = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note Test = mListNote.get(position);
        if(Test == null){
            return;
        }
        holder.title.setText(Test.getTitle());
        holder.date.setText(Test.getDate());
    }

    @Override
    public int getItemCount() {
        if(mListNote != null) return mListNote.size();
        return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        public NoteViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.note_tv1);
            date = itemView.findViewById(R.id.note_tv2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }

    }
}
