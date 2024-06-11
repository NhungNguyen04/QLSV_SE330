package com.example.qlsv.ui.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv.R;

import org.w3c.dom.Text;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private Context mContext;
    private List<Test> mListTest;

    public TestAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Test> list){
        this.mListTest = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test Test = mListTest.get(position);
        if(Test == null){
            return;
        }
        holder.date.setText(Test.getDate());
        holder.time.setText(Test.getTime());
        holder.course.setText(Test.getCourse());
        holder.room.setText(Test.getRoomName());
    }

    @Override
    public int getItemCount() {
        if(mListTest != null) return mListTest.size();
        return 0;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder{
        private TextView date;
        private TextView time;
        private TextView course;
        private TextView room;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            course = itemView.findViewById(R.id.course);
            room = itemView.findViewById(R.id.room);
        }
    }
}
