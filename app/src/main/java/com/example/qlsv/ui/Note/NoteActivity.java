package com.example.qlsv.ui.Note;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv.LoginActivity;
import com.example.qlsv.MainActivity;
import com.example.qlsv.User;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.qlsv.databinding.ActivityNoteBinding;

import com.example.qlsv.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {
    TextView noteContent;
    TextView noteTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note);
        ImageView imgView = findViewById(R.id.backbtn);
        ImageView createBtn = findViewById(R.id.note_createbtn);
        imgView.setOnClickListener(v -> {
            this.finish();
        });

        noteTitle = findViewById(R.id.note_title);
        noteContent = findViewById(R.id.note_content);
        if(getIntent().getExtras() != null){
            noteTitle.setText((String) getIntent().getExtras().get("title"));
            noteContent.setText((String) getIntent().getExtras().get("content"));
        }

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getExtras() != null){
                    updateNote((Integer) getIntent().getExtras().get("id"));
                } else {
                    createNote();
                }
                finish();
            }
        });
    }

    private void createNote(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://qlsv-server-2.onrender.com/api/note/createnote";


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NOTE", "error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("NOTE", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                String today = day + "/" + month + "/" + year;

                params.put("title", noteTitle.getText().toString() );
                params.put("content", noteContent.getText().toString());
                params.put("date", today );
                params.put("userId", String.valueOf(User.getId()));

                return params;
            }
        };
        requestQueue.add(request);
    }
    private void updateNote(int idNote){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://qlsv-server-2.onrender.com/api/note/editnote/" + idNote;


        StringRequest request = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("NOTE", "error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("NOTE", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                String today = day + "/" + month + "/" + year;

                params.put("title", noteTitle.getText().toString() );
                params.put("content", noteContent.getText().toString());

                return params;
            }
        };
        requestQueue.add(request);
    }
}