package com.example.qlsv.ui.Deadline;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv.R;
import com.example.qlsv.RecyclerViewInterface;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class DeadlineAdapter extends RecyclerView.Adapter<DeadlineAdapter.DeadlineViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private Context mContext;
    private List<Deadline> mListDeadline;
    Uri imageUri = null;
    public DeadlineAdapter(Context mContext, RecyclerViewInterface recyclerViewInterface) {
        this.mContext = mContext;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setData(List<Deadline> list){
        this.mListDeadline = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deadline, parent, false);
        return new DeadlineViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DeadlineViewHolder holder, int position) {
        Deadline deadline = mListDeadline.get(position);
        if(deadline == null){
            return;
        }
        holder.title.setText(deadline.getTitle());
        holder.date.setText(deadline.getDate());
        holder.className.setText(deadline.getClassName());
        holder.complete.setText(deadline.getComplete());
        holder.status.setText(deadline.getStatus());

        String completedText = deadline.getComplete();
        if(Objects.equals(completedText, "Chưa nộp bài")) {
            holder.linearLayout1.setVisibility(View.VISIBLE);
        } else {
            holder.linearLayout2.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        if(mListDeadline != null) return mListDeadline.size();
        return 0;
    }

    public class DeadlineViewHolder extends RecyclerView.ViewHolder{
        private TextView className;
        private TextView title;
        private TextView date;
        private TextView status;
        private TextView complete;

        private LinearLayout linearLayout1;
        private LinearLayout linearLayout2;


        public DeadlineViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            className = itemView.findViewById(R.id.deadline_tv1);
            title = itemView.findViewById(R.id.deadline_tv2);
            date = itemView.findViewById(R.id.deadline_tv3);
            status = itemView.findViewById(R.id.deadline_tv4);
            complete = itemView.findViewById(R.id.deadline_tv5);

            linearLayout1 = itemView.findViewById(R.id.uncompleted);
            linearLayout2 = itemView.findViewById(R.id.completed);

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
