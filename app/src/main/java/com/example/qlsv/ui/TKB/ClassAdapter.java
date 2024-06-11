package com.example.qlsv.ui.TKB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private Context mContext;
    private List<TKBClass> mListClass;


    public ClassAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<TKBClass> list){
        this.mListClass = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        TKBClass tkbClass = mListClass.get(position);
        if(tkbClass == null){
            return;
        }

        holder.lecturer.setText("Giảng viên: " + tkbClass.getLecturer());
        holder.room.setText("Phòng: " + tkbClass.getRoom());
        holder.courseName.setText(tkbClass.getCourseName());
        holder.begin.setText("Tiết bắt đầu: " + String.valueOf(tkbClass.getBegin()));
        holder.end.setText(String.valueOf("Tiết kết thúc: " + tkbClass.getEnd()));
    }

    @Override
    public int getItemCount() {
        if(mListClass != null) return mListClass.size();
        return 0;
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder{
        private TextView lecturer;
        private TextView courseName;
        private TextView room;
        private TextView begin;
        private TextView end;
        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.class_tv1);
            room = itemView.findViewById(R.id.class_tv2);
            begin = itemView.findViewById(R.id.class_tv3);
            end = itemView.findViewById(R.id.class_tv4);
            lecturer = itemView.findViewById(R.id.class_tv5);
        }
    }
}
