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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
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
import com.example.jacaranda.Util.JsonToStringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                RecentActivity activity = activityList.get(position);
                intent.putExtra("image", activity.getImageName());
                intent.putExtra("name", activity.getName());
                intent.putExtra("amount", activity.getBalance());
                intent.putExtra("extra", activity.getExtra());
                try {
                    intent.putExtra("time", activity.getDetailTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent.putExtra("receipt", activity.getReceipt());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initActivitiesData() {
        String strByJson = JsonToStringUtil.getStringByJson(getActivity(), R.raw.activities);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement activity : jsonArray) {
            RecentActivity Activity = gson.fromJson(activity, RecentActivity.class);
            activityList.add(Activity);
        }
        activityList.sort(Comparator.comparing(RecentActivity::getDateInt));
        Collections.reverse(activityList);
        activityBackup.addAll(activityList);
    }

    private EditText text;
    private void initSearchBar() {
        activityTemp1.addAll(activityBackup);
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
                            RecentActivity activity = activityList.get(position);
                            intent.putExtra("image", activity.getImageName());
                            intent.putExtra("name", activity.getName());
                            intent.putExtra("amount", activity.getBalance());
                            intent.putExtra("extra", activity.getExtra());
                            try {
                                intent.putExtra("time", activity.getDetailTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra("receipt", activity.getReceipt());
                            startActivity(intent);
                        }
                    });
                }else{
                    activitySearched.clear();
                    for(RecentActivity activity: activityTemp1){
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
                            RecentActivity activity = activitySearched.get(position);
                            intent.putExtra("image", activity.getImageName());
                            intent.putExtra("name", activity.getName());
                            intent.putExtra("amount", activity.getBalance());
                            intent.putExtra("extra", activity.getExtra());
                            try {
                                intent.putExtra("time", activity.getDetailTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra("receipt", activity.getReceipt());
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

    PopupWindow popupWindow;
    Button filter,back;
    ConstraintLayout cl;
    private void initFilter() {
        filter = RootView.findViewById(R.id.id_btn_activity_filter);
        cl = RootView.findViewById(R.id.id_cl_activity);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View window = inflater.inflate(R.layout.filter_popupwindow, null);
                popupWindow = new PopupWindow(view ,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        cl.getHeight(), true);
                popupWindow.setContentView(window);

                initPopupWindow();

                popupWindow.showAtLocation(view, Gravity.TOP,0,0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                });
            }
        });
    }


    ImageView btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    ConstraintLayout filter1,filter2,filter3,filter4,filter5,filter6,filter7;
    Boolean flag1=true,flag2=false,flag3=false,flag4=true,flag5=false,flag6=false,flag7=false;
    private void initPopupWindow(){
        back = popupWindow.getContentView().findViewById(R.id.id_btn_filter_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        btn1 = popupWindow.getContentView().findViewById(R.id.id_btn_filter1);
        btn2 = popupWindow.getContentView().findViewById(R.id.id_btn_filter2);
        btn3 = popupWindow.getContentView().findViewById(R.id.id_btn_filter3);
        btn4 = popupWindow.getContentView().findViewById(R.id.id_btn_filter4);
        btn5 = popupWindow.getContentView().findViewById(R.id.id_btn_filter5);
        btn6 = popupWindow.getContentView().findViewById(R.id.id_btn_filter6);
        btn7 = popupWindow.getContentView().findViewById(R.id.id_btn_filter7);
        if(flag1){
            btn1.setSelected(true);
        }
        if(flag2){
            btn2.setSelected(true);
        }
        if(flag3){
            btn3.setSelected(true);
        }
        if(flag4){
            btn4.setSelected(true);
        }
        if(flag5){
            btn5.setSelected(true);
        }
        if(flag6){
            btn6.setSelected(true);
        }
        if(flag7){
            btn7.setSelected(true);
        }

        filter1 = popupWindow.getContentView().findViewById(R.id.id_cl_filter1);
        filter1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag1=true;flag2=false;flag3=false;
                btn1.setSelected(true);
                btn2.setSelected(false);
                btn3.setSelected(false);
                filterData();
            }
        });
        filter2 = popupWindow.getContentView().findViewById(R.id.id_cl_filter2);
        filter2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag1=false;flag2=true;flag3=false;
                btn1.setSelected(false);
                btn2.setSelected(true);
                btn3.setSelected(false);
                filterData();
            }
        });
        filter3 = popupWindow.getContentView().findViewById(R.id.id_cl_filter3);
        filter3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag1=false;flag2=false;flag3=true;
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(true);
                filterData();
            }
        });

        filter4 = popupWindow.getContentView().findViewById(R.id.id_cl_filter4);
        filter4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=true;flag5=false;flag6=false;flag7=false;
                btn4.setSelected(true);
                btn5.setSelected(false);
                btn6.setSelected(false);
                btn7.setSelected(false);
                filterData();
            }
        });
        filter5 = popupWindow.getContentView().findViewById(R.id.id_cl_filter5);
        filter5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=false;flag5=true;flag6=false;flag7=false;
                btn4.setSelected(false);
                btn5.setSelected(true);
                btn6.setSelected(false);
                btn7.setSelected(false);
                filterData();
            }
        });
        filter6 = popupWindow.getContentView().findViewById(R.id.id_cl_filter6);
        filter6.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=false;flag5=false;flag6=true;flag7=false;
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(true);
                btn7.setSelected(false);
                filterData();
            }
        });
        filter7 = popupWindow.getContentView().findViewById(R.id.id_cl_filter7);
        filter7.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=false;flag5=false;flag6=false;flag7=true;
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(false);
                btn7.setSelected(true);
                filterData();
            }
        });
    }

    List<RecentActivity> activityTemp = new ArrayList<>();
    List<RecentActivity> activityTemp1 = new ArrayList<>();
    int year,month,day,dateInt;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterData(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateInt = year*360 + month*30 + day;

        activityTemp.clear();
        activityTemp1.clear();
        if(flag1){
            activityTemp.addAll(activityBackup);
        }
        if(flag2){
            for(RecentActivity activity: activityBackup){
                if(activity.getBalance().contains("+")){
                    activityTemp.add(activity);
                }
            }
        }
        if(flag3){
            for(RecentActivity activity: activityBackup){
                if(activity.getBalance().contains("-")){
                    activityTemp.add(activity);
                }
            }
        }
        //----------------------------------
        if(flag4){
            activityTemp1.addAll(activityTemp);
        }
        if(flag5){
            for(RecentActivity activity: activityTemp){
                if(dateInt-activity.getDayInt()<=30){
                    activityTemp1.add(activity);
                }
            }
        }
        if(flag6){
            for(RecentActivity activity: activityTemp){
                if(dateInt-activity.getDayInt()<=60){
                    activityTemp1.add(activity);
                }
            }
        }
        if(flag7){
            for(RecentActivity activity: activityTemp){
                if(dateInt-activity.getDayInt()<=90){
                    activityTemp1.add(activity);
                }
            }
        }
        adapter = new ActivitiesAdapter(getActivity(),
                R.layout.home_activity_part_listview, R.layout.all_activities_listview, activityTemp1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                RecentActivity activity = activityTemp1.get(position);
                intent.putExtra("image", activity.getImageName());
                intent.putExtra("name", activity.getName());
                intent.putExtra("amount", activity.getBalance());
                intent.putExtra("extra", activity.getExtra());
                try {
                    intent.putExtra("time", activity.getDetailTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent.putExtra("receipt", activity.getReceipt());
                startActivity(intent);
            }
        });
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
                                                List<RecentActivity> moreActivites = transferToRecentActivity(activities);
                                                activityList.addAll(moreActivites);
                                                activityBackup.addAll(moreActivites);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            getActivity().runOnUiThread(() -> filterData());
                                            if (len < 10){
                                                getActivity().runOnUiThread(ActivityFragment.this::updateView);
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
                RecentActivity activity = activityList.get(position);
                intent.putExtra("image", activity.getImageName());
                intent.putExtra("name", activity.getName());
                intent.putExtra("amount", activity.getBalance());
                intent.putExtra("extra", activity.getExtra());
                try {
                    intent.putExtra("time", activity.getDetailTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent.putExtra("receipt", activity.getReceipt());
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
                                                Log.i(TAG, transferToRecentActivity(activities).toString());
                                                activityList.addAll(1, transferToRecentActivity(activities));

                                                Log.i(TAG, activityList.toString());
                                                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<RecentActivity> transferToRecentActivity(List<Activity> activities) throws ParseException {

        List<RecentActivity> recentActivities = new ArrayList<>();

        String payUser, receiveUser, dateTime, id;
        id = preferences.getString("userID", null);
        for (Activity activity: activities){
            RecentActivity recentActivity = new RecentActivity();

            recentActivity.setReceipt(activity.getReceipt());

            receiveUser = activity.getReceiveUser();
            payUser = activity.getPayUser();

            if (receiveUser.equals(id)){
                if (payUser.equals(id)){
                    recentActivity.setName("topUp");
                    recentActivity.setType("topUp");
                    recentActivity.setImageName("topUp");
                }else{
                    recentActivity.setName(activity.getPayUsername());
                    recentActivity.setType("receive");
                    recentActivity.setImageName(activity.getPayColor());
                }
                recentActivity.setAmount(activity.getAmount());
            }else {
                recentActivity.setName(activity.getReceiveUsername());
                recentActivity.setType("pay");
                recentActivity.setImageName(activity.getReceiveColor());
                recentActivity.setAmount(-activity.getAmount());
            }

            dateTime = activity.getDateString();
            recentActivity.setDateTime(dateTime);
            recentActivity.setDateString(sdf.format(Objects.requireNonNull(df.parse(dateTime))));

            recentActivities.add(recentActivity);
        }
        activities.clear();
        return recentActivities;
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
            Log.i(TAG, activityList.get(0).getDateTime());
            getActivitiesAfter(activityList.get(0).getDateTime());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showMore(){
        RecentActivity lastActivity = activityList.get(activityList.size()-1);
        activityList.remove(lastActivity);
        activityBackup.remove(lastActivity);
        getActivitiesBefore(lastActivity.getDateTime());
    }
}