package com.example.jacaranda.Fragment;

import static com.example.jacaranda.Util.JsonToStringUtil.parseResponse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.jacaranda.Activity.ActivitiesDetail;
import com.example.jacaranda.Adapter.ActivitiesAdapter;
import com.example.jacaranda.Database.LoginDBHelper;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.Modle.Activity;
import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.MyView.NoScrollListView;
import com.example.jacaranda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ActivityFragment extends Fragment {
    View RootView;
    List<RecentActivity> activityList = new ArrayList<>();
    List<RecentActivity> activitySearched = new ArrayList<>();
    List<RecentActivity> activityBackup = new ArrayList<>();
    LinearLayout Activities;
    ActivitiesAdapter adapter;
    NoScrollListView listView;

    private JacarandaApplication app;

    private static final String TAG = "ActivityFragment";

    private static final String PATH_BEFORE = "/bill_before";
    private static final String PATH_AFTER = "/bill";

    private SharedPreferences preferences;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    public ActivityFragment() {
        // Required empty public constructor
    }

    public static ActivityFragment newInstance() {
        ActivityFragment fragment = new ActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (JacarandaApplication)getActivity().getApplication();
        preferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        if(RootView == null){
            RootView = inflater.inflate(R.layout.fragment_activity, container, false);
            initActivities();
            initSearchBar();
            initFilter();
        }
        return RootView;
    }

    Button button;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initActivities() {
//        initActivitiesData();
        Activities = RootView.findViewById(R.id.id_ll_activities);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.all_activities_more, null);
        Activities.addView(view);
        listView = (NoScrollListView) RootView.findViewById(R.id.id_activities);
        listView.setTextFilterEnabled(true);
        adapter = new ActivitiesAdapter(this.getActivity(),
                R.layout.home_activity_part_listview, R.layout.all_activities_listview, activityList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                intent.putExtra("image", activityList.get(position).getImageName());
                intent.putExtra("name", activityList.get(position).getName());
                intent.putExtra("amount", activityList.get(position).getBalance());
                startActivity(intent);
            }
        });

        button = RootView.findViewById(R.id.id_activityFrag_btn_showMore);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore();
            }
        });
    }

    private EditText text;
    private void initSearchBar() {
        text = (EditText) RootView.findViewById(R.id.id_et_search);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString() == ""){
                    ActivitiesAdapter adapterNew = new ActivitiesAdapter(getActivity(),
                            R.layout.home_activity_part_listview, R.layout.all_activities_listview, activityBackup);
                    listView.setAdapter(adapterNew);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                            intent.putExtra("image", activityList.get(position).getImageName());
                            intent.putExtra("name", activityList.get(position).getName());
                            intent.putExtra("amount", activityList.get(position).getBalance());
                            startActivity(intent);
                        }
                    });
                }else{
                    activitySearched.clear();
                    for(RecentActivity activity: activityBackup){
                        if(activity.getName().contains(s.toString())){
                            activitySearched.add(activity);
                        }
                    }
                    ActivitiesAdapter adapterNew = new ActivitiesAdapter(getActivity(),
                            R.layout.home_activity_part_listview, R.layout.all_activities_listview, activitySearched);
                    listView.setAdapter(adapterNew);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                            intent.putExtra("image", activitySearched.get(position).getImageName());
                            intent.putExtra("name", activitySearched.get(position).getName());
                            intent.putExtra("amount", activitySearched.get(position).getBalance());
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    Button filter;
    private void initFilter() {
        filter = RootView.findViewById(R.id.id_btn_activity_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<RecentActivity> transferToRecentActivity(List<Activity> activities) throws ParseException {

        List<RecentActivity> recentActivities = new ArrayList<>();

        String payUser, receiveUser, dateTime, id;
        id = preferences.getString("userID", null);
        for (Activity activity: activities){
            RecentActivity recentActivity = new RecentActivity();
//            Log.i(TAG, activity.getReceipt());
            recentActivity.setReceipt(activity.getReceipt());
            recentActivity.setImageName("payoneer");

            receiveUser = activity.getReceiveUser();
            payUser = activity.getPayUser();

            if (receiveUser.equals(id)){
                recentActivity.setAmount(activity.getAmount());
                if (payUser.equals(id)){
                    recentActivity.setName("topUp");
                    recentActivity.setType("topUp");
                    recentActivity.setProfilePic("topUp");
                }else{
                    recentActivity.setName(activity.getPayUsername());
                    recentActivity.setType("receive");
                    recentActivity.setProfilePic(activity.getPayColor());
                }
            }else {
                recentActivity.setAmount(-activity.getAmount());
                recentActivity.setName(activity.getReceiveUsername());
                recentActivity.setType("pay");
                recentActivity.setProfilePic(activity.getReceiveColor());
            }

            dateTime = activity.getDateString();
            recentActivity.setDateTime(dateTime);
            recentActivity.setDateString(sdf.format(Objects.requireNonNull(df.parse(dateTime))));

            recentActivities.add(recentActivity);
        }
        activities.clear();
        return recentActivities;
    }

    private void getActivitiesBefore(String timestamp){
        new Thread(){
            @Override
            public void run() {

                //parse values in textbox and transfer to json
                JSONObject time = new JSONObject();

                try {
                    time.put("time", timestamp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, time.toString());
                //setup RequestBody
                final RequestBody requestBody = RequestBody.create(
                        time.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getURL() + PATH_BEFORE)
                        .addHeader("token",preferences.getString("AccessToken", null))
                        .post(requestBody)
                        .build();

                new OkHttpClient()
                        .newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                showAlert("Failed to load data", "Error: " + e.toString());
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(
                                    @NonNull Call call,
                                    @NonNull Response response
                            ) throws IOException {
                                if (!response.isSuccessful()) {
                                    showAlert(
                                            "Failed to load page",
                                            "Error: " + response.toString()
                                    );
                                } else {
                                    final JSONObject responseJson = parseResponse(response.body());
                                    Log.i(TAG, responseJson.toString());

                                    String code, message, data;
                                    code = responseJson.optString("code");
                                    message = responseJson.optString("msg");
                                    data = responseJson.optString("data");
                                    Log.i(TAG, data);

                                    //判断是否成功
                                    if (code.equals("200")) {
//                                            showToast("Records retrieved");
                                        List<Activity> activities = JSON.parseArray(data, Activity.class);
                                        Log.i(TAG, activities.toString());
                                        if (!activities.isEmpty()){
                                            int len = activities.size();
                                            Log.i(TAG, String.valueOf(activities.size()));
                                            try {
                                                activityList.addAll(transferToRecentActivity(activities));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

//                                            if (activities.size() < 9){
//                                                getActivity().runOnUiThread(ActivityFragment.this::updateView);
//                                            }else{
//                                                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
//                                            }
                                            if (len < 10){
                                                getActivity().runOnUiThread(ActivityFragment.this::updateView);
                                            }else {
                                                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                                            }


                                        }

                                    }else{
                                        showToast(message);
                                    }

                                }
                            }
                        });

            }
        }.start();
    }

    private void updateView(){
        Activities.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.all_activities, null);
        Activities.addView(view);
        listView = (NoScrollListView) RootView.findViewById(R.id.id_activities);
        listView.setTextFilterEnabled(true);
        adapter = new ActivitiesAdapter(this.getActivity(),
                R.layout.home_activity_part_listview, R.layout.all_activities_listview, activityList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                intent.putExtra("image", activityList.get(position).getImageName());
                intent.putExtra("name", activityList.get(position).getName());
                intent.putExtra("amount", activityList.get(position).getBalance());
                startActivity(intent);
            }
        });
    }

    private void getActivitiesAfter(String timestamp){
        new Thread(){
            @Override
            public void run() {

                //parse values in textbox and transfer to json
                JSONObject time = new JSONObject();

                try {
                    time.put("time", timestamp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, time.toString());
                //setup RequestBody
                final RequestBody requestBody = RequestBody.create(
                        time.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getURL() + PATH_AFTER)
                        .addHeader("token",preferences.getString("AccessToken", null))
                        .post(requestBody)
                        .build();

                new OkHttpClient()
                        .newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                showAlert("Failed to load data", "Error: " + e.toString());
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(
                                    @NonNull Call call,
                                    @NonNull Response response
                            ) throws IOException {
                                if (!response.isSuccessful()) {
                                    showAlert(
                                            "Failed to load page",
                                            "Error: " + response.toString()
                                    );
                                } else {
                                    final JSONObject responseJson = parseResponse(response.body());
                                    Log.i(TAG, responseJson.toString());

                                    String code, message, data;
                                    code = responseJson.optString("code");
                                    message = responseJson.optString("msg");
                                    data = responseJson.optString("data");
                                    Log.i(TAG, data);

                                    //判断是否成功
                                    if (code.equals("200")) {
//                                            showToast("Records retrieved");
                                        List<Activity> activities = JSON.parseArray(data, Activity.class);
                                        Log.i(TAG, activities.toString());
                                        if (!activities.isEmpty()){
                                            try {
                                                activityList.addAll(0, transferToRecentActivity(activities));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

                                    }else{
                                        showToast(message);
                                    }

                                }
                            }
                        });

            }
        }.start();
    }

    private void showAlert(String title, @Nullable String message) {
        getActivity().runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create();
            dialog.show();
        });
    }

    private void showToast(String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show());
    }

    private JSONObject parseResponse(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new JSONObject(responseBody.string());
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error parsing response", e);
            }
        }
        return new JSONObject();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        if (activityList.isEmpty()){
            getActivitiesBefore(df.format(new Date()));
        }else{
            getActivitiesAfter(activityList.get(0).getDateTime());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showMore(){
        RecentActivity lastActivity = activityList.get(activityList.size()-1);
        activityList.remove(lastActivity);
        getActivitiesBefore(lastActivity.getDateTime());
    }
}