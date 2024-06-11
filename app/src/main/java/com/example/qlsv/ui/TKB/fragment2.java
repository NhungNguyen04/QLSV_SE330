package com.example.qlsv.ui.TKB;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv.R;
import com.example.qlsv.User;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class fragment2 extends Fragment {

    private RecyclerView rcv;
    private ClassAdapter classAdapter;
    private Context mContext;
    private List<TKBClass> list;

    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        int date = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;

        int startDayOfWeek = date % 7;
        int nextMonth = month;
        int endDayOfWeek = 0;

        if (date < 7){
            endDayOfWeek = date;
            switch(month - 1) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    date = 31 - (6 - date);
                    --month;

                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    date = 30 - (6 - date);
                    --month;
                    break;
            }
            startDayOfWeek = 0;
        } else {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    endDayOfWeek = (date - startDayOfWeek + 5) % 31;
                    if (date - startDayOfWeek + 5 > 31) {
                        ++nextMonth;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    endDayOfWeek = (date - startDayOfWeek + 5) % 30;
                    if (date - startDayOfWeek + 5 > 30) {
                        ++nextMonth;
                    }
                    break;
            }


        }


        View view = inflater.inflate(R.layout.tkb_fragment2, container, false);
        rcv = view.findViewById(R.id.frag2_rcv);
        list = new ArrayList<>();
        progressBar = view.findViewById(R.id.tkb_fragment2_loading);


        DayScrollDatePicker mPicker = (DayScrollDatePicker) view.findViewById(R.id.day_date_picker);
        mPicker.setStartDate(date - startDayOfWeek - 1,month,2024);
        mPicker.setEndDate(endDayOfWeek,nextMonth,2024);
        Log.d("FRAGMENT1", String.valueOf(nextMonth));
        mPicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int dow = c.get(Calendar.DAY_OF_WEEK);
                getList(dow);
                Log.d("FRAGMENT1", String.valueOf(dow));
            }
        });
        return view;
    }

    private void getList(int dow) {

        User user = new User();
        list.clear();
        rcv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/class/allclassesbyid/{0}?dow={1}", user.getId(), dow);

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0; i < response.length(); i++){
                        JSONObject responseObj = response.getJSONObject(i);
                        String lecturer = responseObj.getString("lecturer");
                        String room = responseObj.getString("room");
                        int begin = responseObj.getInt("begin");
                        int end = responseObj.getInt("end");
                        String courseName = responseObj.getString("courseName");

                        list.add(new TKBClass(lecturer,courseName,room,begin,end));
                    }
                    buildRcv();
                    rcv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("TKB", error.toString());
            }
        });
        requestQueue.add(request);
    }
    private void buildRcv() {
        classAdapter = new ClassAdapter(mContext);
        classAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);


        rcv.setLayoutManager(linearLayoutManager);

        rcv.setAdapter(classAdapter);
    }
}