package com.example.qlsv.ui.Test;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv.R;
import com.example.qlsv.User;
import com.example.qlsv.databinding.FragmentTestBinding;
import com.example.qlsv.ui.TKB.ClassAdapter;
import com.example.qlsv.ui.TKB.TKBClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {

    private @NonNull FragmentTestBinding binding;
    private String[] list = {"Học kì 1", "Học kì 2", "Học kì hè"};
    private String[] schoolYear = {"2020-2021","2021-2022","2022-2023"};
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView1;

    private ArrayAdapter<String> adapterList;
    private ArrayAdapter<String> adapterList1;

    private RecyclerView rcv1;
    private TestAdapter testAdapter;
    private Context mContext;
    private List<Test> testList;
    private AppCompatButton searchBtn;
    private ProgressBar progressBar;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextView = getActivity().findViewById(R.id.auto_complete_tv);
        autoCompleteTextView1 = getActivity().findViewById(R.id.auto_complete_tv1);
        adapterList = new ArrayAdapter<String>(this.getContext(),R.layout.test_item,list);
        adapterList1 = new ArrayAdapter<String>(this.getContext(), R.layout.test_item, schoolYear);
        autoCompleteTextView.setAdapter(adapterList);
        autoCompleteTextView1.setAdapter(adapterList1);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item  = parent.getItemAtPosition(position).toString();
                Log.d("Test", item);
            }
        });

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item  = parent.getItemAtPosition(position).toString();
                Log.d("Test", item);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TestScheduleViewModel testViewModel =
                new ViewModelProvider(this).get(TestScheduleViewModel.class);

        binding = FragmentTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        testList = new ArrayList<>();
        rcv1 = root.findViewById(R.id.rcv1);
        searchBtn = root.findViewById(R.id.searchbtn);
        progressBar = root.findViewById(R.id.test_loading);

        searchBtn.setOnClickListener(v -> {

            autoCompleteTextView.setAdapter(adapterList);
            autoCompleteTextView1.setAdapter(adapterList1);
            getList(autoCompleteTextView.getText().toString(), autoCompleteTextView1.getText().toString());
        });

//        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item  = parent.getItemAtPosition(position).toString();
//                Log.d("Test", item);
//            }
//        });



        return root;
    }


    private void getList(String hocKi, String namHoc) {
        rcv1.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        User user = new User();
        testList.clear();

        switch(hocKi) {
            case "Học kì 1":
                hocKi = "1";
                break;
            case "Học kì 2":
                hocKi = "2";
                break;
            default:
                break;

        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/class/gettestbyid/{0}?hocKi={1}&namHoc={2}", user.getId(), hocKi, namHoc);

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0; i < response.length(); i++){
                        JSONObject responseObj = response.getJSONObject(i);
                        String date = responseObj.getString("date");
                        String room = responseObj.getString("room");
                        String time = responseObj.getString("time");
                        String courseName = responseObj.getString("courseName");
                        date = "Ngày " + date;
                        time = "Thời gian " + time;
                        room = "Phòng: " + room;

                        courseName = "Môn học " + courseName;

                        testList.add(new Test(date,time,courseName,room));
                    }
                    buildRcv();
                    rcv1.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("Test", error.toString());
            }
        });
        requestQueue.add(request);

    }
    private void buildRcv() {
        testAdapter = new TestAdapter(mContext);
        testAdapter.setData(testList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);


        rcv1.setLayoutManager(linearLayoutManager);

        rcv1.setAdapter(testAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}