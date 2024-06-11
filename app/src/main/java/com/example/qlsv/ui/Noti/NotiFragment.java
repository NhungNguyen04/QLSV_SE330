package com.example.qlsv.ui.Noti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.qlsv.databinding.FragmentNotiBinding;
import com.example.qlsv.ui.Test.Test;
import com.example.qlsv.ui.Test.TestAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class NotiFragment extends Fragment {

    private FragmentNotiBinding binding;
    private RecyclerView rcv;
    private NotiAdapter notiAdapter;
    private Context mContext;


    private ProgressBar progressBar;
    List<Noti> list;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotiViewModel notificationViewModel =
                new ViewModelProvider(this).get(NotiViewModel.class);

        binding = FragmentNotiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rcv = root.findViewById(R.id.noti_rcv);
        list  = new ArrayList<>();
        progressBar = root.findViewById(R.id.noti_loading);
        getList();

        return root;
    }



    private void getList() {
//        list.clear();
        Log.d("ASDASDASD", "List Clear");

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/noti/getnotibyid/{0}", User.getId());
        Log.d("ASDASDASD", URL);
        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0; i < response.length(); i++){
                        JSONObject responseObj = response.getJSONObject(i);
                        String type = responseObj.getString("type");
                        String date = responseObj.getString("date");
                        int begin = responseObj.getInt("begin");
                        int end = responseObj.getInt("end");
                        String room = responseObj.getString("room");
                        String maLop = responseObj.getString("maLop");

                        String title = "[" + type + "]" + " " + maLop;
                        String time = "Tiết: " + begin + " đến " + end;
                        room = "Phòng " + room;

                        list.add(new Noti(title, date, time, room));

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
                Log.d("ASDASDASD", error.toString());
            }
        });
        requestQueue.add(request);

    }
    private void buildRcv() {
        notiAdapter = new NotiAdapter(mContext);
        notiAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);


        rcv.setLayoutManager(linearLayoutManager);

        rcv.setAdapter(notiAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}