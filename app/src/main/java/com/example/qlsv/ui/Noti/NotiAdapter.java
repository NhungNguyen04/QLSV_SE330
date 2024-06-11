package com.example.qlsv.ui.Noti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv.R;
import com.example.qlsv.ui.Test.Test;

import org.w3c.dom.Text;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<com.example.qlsv.ui.Noti.NotiAdapter.NotiViewHolder> {
    private Context mContext;
    private List<Noti> mListNoti;

    public NotiAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Noti> list){
        this.mListNoti = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public com.example.qlsv.ui.Noti.NotiAdapter.NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        return new com.example.qlsv.ui.Noti.NotiAdapter.NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.qlsv.ui.Noti.NotiAdapter.NotiViewHolder holder, int position) {
        Noti Noti = mListNoti.get(position);
        if(Noti == null){
            return;
        }
        holder.title.setText(Noti.getTitle());
        holder.date.setText(Noti.getDate());
        holder.time.setText(Noti.getTime());
        holder.room.setText(Noti.getRoom());
    }

    @Override
    public int getItemCount() {
        if(mListNoti != null) return mListNoti.size();
        return 0;
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        private TextView time;
        private TextView room;
        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noti_tv1);
            date = itemView.findViewById(R.id.noti_tv2);
            time = itemView.findViewById(R.id.noti_tv3);
            room = itemView.findViewById(R.id.noti_tv4);

        }
    }
}

