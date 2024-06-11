package com.example.qlsv.ui.Self;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.qlsv.LoginActivity;
import com.example.qlsv.R;
import com.example.qlsv.User;
import com.example.qlsv.databinding.FragmentSelfBinding;
import com.example.qlsv.ui.Test.Test;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SelfFragment extends Fragment {
    TextView birthdayTv;
    TextView majorTv;
    TextView addressTv;
    TextView classNameTv;
    TextView nameTv;
    TextView emailTv;
    ProgressBar progressBar;
    LinearLayout info;
    LinearLayout name;

    CardView cardViewAvatar;

    ActivityResultLauncher<Intent> imagePickLauncher;

    AppCompatButton signOutBtn;
    Uri selectedImageUri;

    ImageView avatar;
    private @NonNull FragmentSelfBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                    Glide.with(getContext()).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(avatar);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference idRef = storageReference.child("profile_picture").child(String.valueOf(User.getId()));
                    if (idRef != null) {
                        idRef.delete();
                    }
                    if (selectedImageUri != null) {
                        FirebaseStorage.getInstance().getReference().child("profile_picture").child(String.valueOf(User.getId())).putFile(selectedImageUri)
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }
                });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference idRef = storageReference.child("profile_picture").child(String.valueOf(User.getId()));

        idRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(avatar);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SelfViewModel studentSelfViewModel =
                new ViewModelProvider(this).get(SelfViewModel.class);

        binding = FragmentSelfBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        nameTv = root.findViewById(R.id.user_name);
        emailTv = root.findViewById(R.id.user_email);
        birthdayTv = root.findViewById(R.id.user_birthday);
        majorTv = root.findViewById(R.id.user_major);
        addressTv = root.findViewById(R.id.user_address);
        classNameTv = root.findViewById(R.id.user_class);
        progressBar = root.findViewById(R.id.progressBar);
        info = root.findViewById(R.id.info);
        name = root.findViewById(R.id.self_name);
        signOutBtn = root.findViewById(R.id.signout);
        cardViewAvatar = root.findViewById(R.id.cardViewAvatar);
        avatar = root.findViewById(R.id.avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(getActivity()).cropSquare().compress(512).maxResultSize(512, 512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        getUser();

        return root;
    }

    private void getUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/user/detailuser/{0}", User.getId());

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for(int i = 0; i < response.length(); i++){
                        JSONObject responseObj = response.getJSONObject(i);
                        String name = responseObj.getString("name");
                        String email = responseObj.getString("email");
                        String birthday = responseObj.getString("birthday");
                        String address = responseObj.getString("address");
                        String major = responseObj.getString("majorName");
                        String className = responseObj.getString("class");

                        nameTv.setText(name);
                        emailTv.setText(email);
                        birthdayTv.setText(birthday);
                        addressTv.setText(address);
                        majorTv.setText(major);
                        classNameTv.setText(className);

                    }
                    progressBar.setVisibility(View.GONE);
                    info.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    cardViewAvatar.setVisibility((View.VISIBLE));


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}