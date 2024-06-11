package com.example.qlsv.ui.Note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv.LoginActivity;
import com.example.qlsv.MainActivity;
import com.example.qlsv.R;
import com.example.qlsv.RecyclerViewInterface;
import com.example.qlsv.User;
import com.example.qlsv.databinding.FragmentNoteBinding;
import com.example.qlsv.ui.Deadline.Deadline;
import com.example.qlsv.ui.Deadline.DeadlineAdapter;
import com.example.qlsv.ui.Test.Test;
import com.example.qlsv.ui.Test.TestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment implements RecyclerViewInterface {

    private @NonNull FragmentNoteBinding binding;
    private RecyclerView rcv;
    private NoteAdapter noteAdapter;
    private Context mContext;
    private List<Note> list;

    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NoteViewModel noteViewModel =
                new ViewModelProvider(this).get(NoteViewModel.class);

        binding = FragmentNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppCompatButton button = root.findViewById(R.id.createbtn);
        ImageView refreshBtn = root.findViewById(R.id.note_refresh);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NoteActivity.class);
            getActivity().startActivity(intent);
        });
//        final TextView textView = binding.textNote;
//        noteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        list = new ArrayList<>();
        rcv = root.findViewById(R.id.rcv2);
        progressBar = root.findViewById(R.id.note_loading);
        getList();

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getList() {


        list.clear();
        progressBar.setVisibility(View.VISIBLE);
        rcv.setVisibility(View.GONE);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/note/getnotesbyuser/{0}", User.getId());

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0; i < response.length(); i++){
                        JSONObject responseObj = response.getJSONObject(i);
                        int id = responseObj.getInt("id");
                        String title = responseObj.getString("title");
                        String content = responseObj.getString("content");
                        String date = responseObj.getString("date");



                        list.add(new Note(id,title,date,content));
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
                Log.d("Note", error.toString());
            }
        });
        requestQueue.add(request);

    }
    private void buildRcv() {
        noteAdapter = new NoteAdapter(mContext, this);
        noteAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);


        rcv.setLayoutManager(linearLayoutManager);

        rcv.setAdapter(noteAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), NoteActivity.class);

        intent.putExtra("title", list.get(position).getTitle());
        intent.putExtra("content",list.get(position).getContent());
        intent.putExtra("id", list.get(position).getId());
        startActivity(intent);

    }
}