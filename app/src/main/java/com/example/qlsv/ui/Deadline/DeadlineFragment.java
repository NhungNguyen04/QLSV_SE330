package com.example.qlsv.ui.Deadline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv.R;
import com.example.qlsv.RecyclerViewInterface;
import com.example.qlsv.User;
import com.example.qlsv.databinding.FragmentDeadlineBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeadlineFragment extends Fragment implements RecyclerViewInterface {

    private @NonNull FragmentDeadlineBinding binding;
    private String[] list = {"Học kì 1", "Học kì 2", "Học kì hè"};
    private String[] schoolYear = {"2020-2021","2021-2022","2022-2023"};
    private List<String> completedList = new ArrayList<>();
    private List<String> unCompletedList = new ArrayList<>();
    private RecyclerView rcv;
    private DeadlineAdapter deadlineAdapter;
    private Context mContext;
    private List<Deadline> deadlineList;

    private ProgressBar progressBar;

    private HashMap<String,String> pdfList = new HashMap<>();


    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView1;

    private AutoCompleteTextView autoCompleteTextView2;
    private AutoCompleteTextView autoCompleteTextView3;

    private ArrayAdapter<String> adapterList;
    private ArrayAdapter<String> adapterList1;
    private ArrayAdapter<String> adapterList2;
    private ArrayAdapter<String> adapterList3;

    private ProgressDialog dialog;
    private PDFView pdfView;

    private Uri imageUri;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextView = getActivity().findViewById(R.id.deadline_auto_complete_tv);
        autoCompleteTextView1 = getActivity().findViewById(R.id.deadline_auto_complete_tv1);
        autoCompleteTextView2 = getActivity().findViewById(R.id.deadline_auto_complete_tv2);
        autoCompleteTextView3 = getActivity().findViewById(R.id.deadline_auto_complete_tv3);
        adapterList = new ArrayAdapter<String>(this.getContext(),R.layout.test_item,list);
        adapterList1 = new ArrayAdapter<String>(this.getContext(), R.layout.test_item, schoolYear);
        adapterList2 = new ArrayAdapter<String>(this.getContext(),R.layout.test_item,unCompletedList);
        adapterList3 = new ArrayAdapter<String>(this.getContext(), R.layout.test_item, completedList);
        autoCompleteTextView.setAdapter(adapterList);
        autoCompleteTextView1.setAdapter(adapterList1);
        autoCompleteTextView2.setAdapter(adapterList2);
        autoCompleteTextView3.setAdapter(adapterList3);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DeadlineViewModel deadlineViewModel =
                new ViewModelProvider(this).get(DeadlineViewModel.class);

        binding = FragmentDeadlineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        deadlineList = new ArrayList<>();
        rcv = root.findViewById(R.id.rcv3);
        progressBar = root.findViewById(R.id.deadline_loading);




        ImageView imageView = root.findViewById(R.id.pdf);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,1);
            }
        });
//        pdfView = root.findViewById(R.id.pdfView);
        AppCompatButton searchBtn = root.findViewById(R.id.deadline_searchbtn);
        AppCompatButton submitBtn = root.findViewById(R.id.deadline_submit);
        AppCompatButton viewBtn = root.findViewById(R.id.deadline_view);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getList(autoCompleteTextView.getText().toString(), autoCompleteTextView1.getText().toString());
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(mContext);
                dialog.setMessage("Uploading");
                dialog.show();

                final String timeStamp = "" + System.currentTimeMillis();
                final String messagePushID = timeStamp;
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
                Toast.makeText(mContext,imageUri.toString(),Toast.LENGTH_SHORT).show();
                final StorageReference filePath = storageReference.child(messagePushID + "." + "pdf");
                Toast.makeText(mContext, filePath.getName(), Toast.LENGTH_SHORT).show();
                final Object obj = new Object();
                filePath.putFile(imageUri).continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if(!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Uri uri = (Uri) task.getResult();
                            String myUrl;
                            myUrl = uri.toString();
                            HashMap<String,String> data = new HashMap<>();

                            int idDeadline = findIdByTitle(autoCompleteTextView2.getText().toString());
                            data.put("id", String.valueOf(idDeadline));
                            data.put("pdf", myUrl);
                            databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(data);
                            submitDeadline(idDeadline);
                            Toast.makeText(mContext, "uploaded succesfully", Toast.LENGTH_SHORT).show();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(mContext, "uploaded failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("uploads");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = "";
                String pdfUrl = "";
                for(DataSnapshot dsp : snapshot.getChildren()){
                    if (dsp.getValue() instanceof  HashMap){
                        HashMap<String,Object> datas = (HashMap<String, Object>) dsp.getValue();
                        id = Objects.requireNonNull(datas.get("id")).toString();
                        pdfUrl = Objects.requireNonNull(datas.get("pdf")).toString();
                        Log.d("HEOCON", pdfUrl);
                        pdfList.put(id, pdfUrl);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = autoCompleteTextView3.getText().toString();
                int id = findIdByTitle(text);
//                new viewPdf().execute(pdfList.get(String.valueOf(id)));
                Intent intent = new Intent(mContext,Deadline_pdfView.class);
                intent.putExtra("url", pdfList.get(String.valueOf(id)));
                startActivity(intent);

            }
        });
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
        }
    }

    private int findIdByTitle(String title) {
        for(int i = 0; i < deadlineList.size(); i++){
            if(Objects.equals(deadlineList.get(i).getTitle(), title)){
                return deadlineList.get(i).getId();
            }
        }
        return -1;
    }



    private void submitDeadline(int id){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/deadline/submitdeadline/{0}", id);

        StringRequest request = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject respObj = new JSONObject(response);





                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Deadline", "error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("Deadline", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<>();

                params.put("completed", "1");


                return params;
            }

        };
        requestQueue.add(request);
    }

    private void getList(String hocKi, String namHoc) {

        progressBar.setVisibility(View.VISIBLE);
        rcv.setVisibility(View.GONE);
        deadlineList.clear();
        adapterList2.clear();
        adapterList3.clear();

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
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/deadline/deadlinebysemester/{0}?hocKi={1}&namHoc={2}", User.getId(), hocKi, namHoc);

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0; i < response.length(); i++){
                        JSONObject responseObj = response.getJSONObject(i);
                        int id = responseObj.getInt("id");
                        String title = responseObj.getString("title");
                        String date = responseObj.getString("time");
                        int completed = responseObj.getInt("completed");
                        String state = responseObj.getString("state");
                        String maLop = responseObj.getString("classMaLop");

                        String completedText = completed == 0 ? "Chưa nộp bài" : "Đã nộp bài";



                        deadlineList.add(new Deadline(maLop,title,date,state,completedText,id));
                        if (completed == 0) {
                            unCompletedList.add(title);
                        } else completedList.add(title);
                    }
                    progressBar.setVisibility(View.GONE);
                    rcv.setVisibility(View.VISIBLE);
                    adapterList2.notifyDataSetChanged();
                    adapterList3.notifyDataSetChanged();
                    buildRcv();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("Deadline", error.toString());
            }
        });
        requestQueue.add(request);

    }
    private void buildRcv() {
        deadlineAdapter = new DeadlineAdapter(mContext, this);
        deadlineAdapter.setData(deadlineList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);


        rcv.setLayoutManager(linearLayoutManager);

        rcv.setAdapter(deadlineAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Log.d("Deadline", String.valueOf(position));

    }
}