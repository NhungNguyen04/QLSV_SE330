package com.example.qlsv.ui.TKB;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv.LoginActivity;
import com.example.qlsv.MainActivity;
import com.example.qlsv.R;
import com.example.qlsv.User;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;


public class fragment1 extends Fragment {
    private RecyclerView rcv;
    private ClassAdapter classAdapter;
    private Context mContext;
    private List<TKBClass> list;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tkb_fragment1, container, false);
        rcv = view.findViewById(R.id.rcv);
        list = new ArrayList<>();
        getList();

        return view;
    }
    private List<TKBClass> getList() {

        Calendar cal = Calendar.getInstance();
        int date = cal.get(Calendar.DAY_OF_WEEK);
        Log.d("date", String.valueOf(date));
        Log.d("TKB", String.valueOf(date));
        User user = new User();
        Log.d("TKB", String.valueOf(user.getId()));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/class/allclassesbyid/{0}?dow={1}", user.getId(), date);

        Log.d("TKB", URL);
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
        return list;
    }

    private void buildRcv() {
        classAdapter = new ClassAdapter(mContext);
        classAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);


        rcv.setLayoutManager(linearLayoutManager);

        rcv.setAdapter(classAdapter);
    }

}