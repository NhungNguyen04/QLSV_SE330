package com.example.qlsv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qlsv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
//
////                if (validate(username, password)) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                LoginActivity.this.startActivity(intent);
                login(username, password);
//                } else {
                // Handle failed login here
            }
//            }
        });
    }

    public void goToAnActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToAnotherActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean validate(String username, String password) {
        // Implement your validation logic here and return true if valid, else false
        return true;
    }
    public void login(String username, String password){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://qlsv-server-2.onrender.com/api/user/login";


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    Log.d("connect", "connected");

                    // below are the strings which we
                    // extract from our json object.
                    String username = respObj.getString("username");
                    String password = respObj.getString("password");
                    int id = respObj.getInt("id");
                    String email = respObj.getString("email");
                    String birthday = respObj.getString("birthday");
                    String address = respObj.getString("address");
                    int accumulatedCredits = respObj.getInt("accumulatedCredits");
                    double totalScore = respObj.getDouble("totalScore");
                    double accumulatedScore = respObj.getDouble("accumulatedScore");
                    int majorId = respObj.getInt("majorId");
                    String className = respObj.getString("class");
                    new User(id, username, password, email, birthday, address, accumulatedCredits, totalScore, accumulatedScore, className, majorId);

                    // on below line we are setting this string s to our text view.
                    Log.d("VOLLEY", "Login successfully");
                    Log.d("VOLLEY", String.valueOf(id));

                    if(username != null && password != null) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("VOLLEY", "error");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("VOLLEY", error.toString());

                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("username", username);
                params.put("password", password);

                // at last we are
                // returning our params.
                return params;
            }
        };
        requestQueue.add(request);
    }
}