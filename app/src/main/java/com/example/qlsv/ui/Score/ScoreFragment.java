package com.example.qlsv.ui.Score;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.qlsv.databinding.FragmentScoreBinding;
import com.example.qlsv.ui.Test.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ScoreFragment extends Fragment {
    private String[] list = {"Học kì 1", "Học kì 2", "Học kì hè"};
    private String[] schoolYear = {"2020-2021","2021-2022","2022-2023"};
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView1;

    private ArrayAdapter<String> adapterList;
    private ArrayAdapter<String> adapterList1;
    private TextView totalCredits;
    private TextView accumulatedCredits;
    private TextView totalScore;
    private TextView accumulatedScore;

    private AppCompatButton searchBtn;
    private TableLayout tbLayout;
    private FragmentScoreBinding binding;

    private ProgressBar progressBar;

    private int majorTotalCredits = 0;

    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextView = getActivity().findViewById(R.id.score_auto_complete_tv);
        autoCompleteTextView1 = getActivity().findViewById(R.id.score_auto_complete_tv1);
        adapterList = new ArrayAdapter<String>(this.getContext(),R.layout.test_item,list);
        adapterList1 = new ArrayAdapter<String>(this.getContext(), R.layout.test_item, schoolYear);
        autoCompleteTextView.setAdapter(adapterList);
        autoCompleteTextView1.setAdapter(adapterList1);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item  = parent.getItemAtPosition(position).toString();
                Log.d("Score", item);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScoreViewModel scoreViewModel =
                new ViewModelProvider(this).get(ScoreViewModel.class);

        binding = FragmentScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getMajor();
        totalCredits = root.findViewById(R.id.totalcredits);
        accumulatedCredits = root.findViewById(R.id.accumulatedcredits);
        totalScore = root.findViewById(R.id.totalscore);
        accumulatedScore = root.findViewById(R.id.accumulatedscore);

        progressBar = root.findViewById(R.id.score_loading);
        accumulatedCredits.setText("Tổng số tín chỉ tích lũy: " + String.valueOf(User.getAccumulatedCredits()));
        totalScore.setText("Điểm trung bình: " + String.valueOf(User.getTotalScore()));
        accumulatedScore.setText("Điểm trung bình tích lũy: " + String.valueOf(User.getAccumulatedScore()));
        LinearLayout layout = root.findViewById(R.id.layout);
        searchBtn = root.findViewById(R.id.score_searchbtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setAdapter(adapterList);
                autoCompleteTextView1.setAdapter(adapterList1);
                tbLayout = new TableLayout(getActivity());
                layout.removeAllViews();
                layout.addView(tbLayout);

                String label = "";
                TextView labelTv = root.findViewById(R.id.label);
                switch(autoCompleteTextView.getText().toString()){
                    case "Học kì 1":
                        label = "HK1";
                        break;
                    case "Học kì 2":
                        label = "HK2";
                        break;
                    default:
                        label = "HKHe";
                        break;
                }
                labelTv.setText(label + ",NH" + autoCompleteTextView1.getText().toString());
                labelTv.setVisibility(TextView.VISIBLE);
                getList(autoCompleteTextView.getText().toString(), autoCompleteTextView1.getText().toString());

            }
        });


//        final TextView textView = binding.textScore;
//        scoreViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void getList(String hocKi, String namHoc) {
        progressBar.setVisibility(View.VISIBLE);
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
        String URL = MessageFormat.format("https://qlsv-server-2.onrender.com/api/class/getscorebysemester/{0}?hocKi={1}&namHoc={2}", User.getId(), hocKi, namHoc);

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    progressBar.setVisibility(View.GONE);
                    init();
                    for(int i = 0; i < response.length(); i++){
                        JSONObject responseObj = response.getJSONObject(i);
                        String maMh = responseObj.getString("maMh");
                        String maLop = responseObj.getString("maLop");
                        double qt = responseObj.getDouble("s1");
                        double th = responseObj.getDouble("s2");
                        double gk = responseObj.getDouble("s3");
                        double ck = responseObj.getDouble("s4");
                        double average = responseObj.getDouble("total");
                        int tc = responseObj.getInt("credits");

                        build(maMh, maLop, tc, qt, th, gk, ck, average);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("Score", error.toString());
            }
        });
        requestQueue.add(request);

    }

    private void init(){
        tbLayout.setColumnStretchable(1, true);
        tbLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(16,16,16,16);
//        tbLayout.setLayoutParams(lp);
        tbLayout.setPadding(16,16,16,16);

        TableRow tbRow = new TableRow(getActivity());
        tbRow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_bottom));
        tbRow.setPadding(0,0,0,16);
        TextView maMH = styleTVHeader();
        maMH.setText("MÃ MH");

        TextView maLop = styleTVHeader();
        maLop.setText("LỚP");

        TextView tc = styleTVHeader();
        tc.setText("TC");

        TextView qt = styleTVHeader();
        qt.setText("QT");

        TextView th = styleTVHeader();
        th.setText("TH");

        TextView gk = styleTVHeader();
        gk.setText("GK");

        TextView ck = styleTVHeader();
        ck.setText("CK");

        TextView tb = styleTVHeader();
        tb.setText("TB");

        tbRow.addView(maMH);
        tbRow.addView(maLop);
        tbRow.addView(tc);
        tbRow.addView(qt);
        tbRow.addView(th);
        tbRow.addView(gk);
        tbRow.addView(ck);
        tbRow.addView(tb);

        tbLayout.addView(tbRow);

    }

    private TextView styleTVHeader() {
        TextView newTv = new TextView(getActivity());
        newTv.setPadding(3,3,3,3);
        newTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        newTv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        newTv.setTextColor(Color.parseColor("#47CAF4"));
        newTv.setTypeface(Typeface.DEFAULT_BOLD);

        return newTv;
    }

    private void build(String maMH, String maLop, int tc, double qt, double th, double gk, double ck, double average){
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setPadding(0,0,0,16);


        TextView maMHTV = new TextView(getActivity());
        TextView maLopTV = new TextView(getActivity());
        TextView tcTV = new TextView(getActivity());
        TextView qtTV = new TextView(getActivity());
        TextView thTV = new TextView(getActivity());
        TextView gkTV = new TextView(getActivity());
        TextView ckTV = new TextView(getActivity());
        TextView tbTV = new TextView(getActivity());


        maMHTV = styleTV();
        maMHTV.setText(maMH);

        maLopTV = styleTV();
        maLopTV.setText(maLop);

        tcTV = styleTV();
        tcTV.setText(String.valueOf(tc));

        qtTV = styleTV();
        qtTV.setText(String.valueOf(qt));

        thTV = styleTV();
        thTV.setText(String.valueOf(th));

        gkTV = styleTV();
        gkTV.setText(String.valueOf(gk));

        ckTV = styleTV();
        ckTV.setText(String.valueOf(ck));

        tbTV = styleTV();
        tbTV.setText(String.valueOf(average));

        tableRow.addView(maMHTV);
        tableRow.addView(maLopTV);
        tableRow.addView(tcTV);
        tableRow.addView(qtTV);
        tableRow.addView(thTV);
        tableRow.addView(gkTV);
        tableRow.addView(ckTV);
        tableRow.addView(tbTV);

        tbLayout.addView(tableRow);

    }
    private TextView styleTV() {
        TextView newTv = new TextView(getActivity());
        newTv.setTextColor(Color.parseColor("#000000"));
        newTv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        newTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        newTv.setPadding(3,3,3,3);

        return newTv;
    }

    public void getMajor(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "https://qlsv-server-2.onrender.com/api/major/detailmajor/" + User.getMajorId();

        Log.d("SCORE", URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    majorTotalCredits = respObj.getInt("totalCredits");
                    totalCredits.setText("Tổng số tín chỉ tích lũy toàn khóa: " + String.valueOf(majorTotalCredits));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors
                Log.d("Score", error.toString());
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