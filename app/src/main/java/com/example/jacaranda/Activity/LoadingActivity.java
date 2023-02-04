package com.example.jacaranda.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.jacaranda.Constants;
import com.example.jacaranda.HintPage.SuccessfullyPay;
import com.example.jacaranda.HintPage.SuccessfullyTransferActivity;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.Modle.Activity;
import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoadingActivity extends AppCompatActivity {
    private final String TAG = "LoadingActivity";
    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private static final String PATH_BEFORE = "/bill_before";
    private JacarandaApplication app;
    private SharedPreferences preferences;


    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    private CountDownLatch countDownLatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        overridePendingTransition( 0,0);
        setContentView(R.layout.activity_loading);

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        app = (JacarandaApplication) getApplication();

        try {
            loadData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws InterruptedException {
        int request = getIntent().getIntExtra("request", 0);
        Log.i(TAG, String.valueOf(request));

        Intent intent = new Intent();
        Timer timer = new Timer();

        switch (request){
            case Constants.GET_BALANCE:
                countDownLatch = new CountDownLatch(1);
                getBalance();

                countDownLatch.await();

                //return balance to activity calling
                intent.putExtra("data_return",balance);
                intent.putExtra("balance",balance);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case Constants.HOMEFRAG:
                countDownLatch = new CountDownLatch(2);
                getBalance();
                getActivitiesBefore(df.format(new Date()));

                countDownLatch.await();

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        //return balance to activity calling
                        intent.putExtra("data_return",balance);
                        intent.putExtra("balance",balance);
                        intent.putExtra("activities", (Serializable) activityList);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                };
                timer.schedule(timerTask,600);

                break;
            default:
                intent.putExtra("data_return","EMPTY");
                setResult(RESULT_OK,intent);
                finish();
        }
    }

    String balance;
    private void getBalance(){

        //setup RequestBody
        final RequestBody requestBody = RequestBody.create(
                "",
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BACKEND_URL + "/balanceOf")
                .post(requestBody)
                .addHeader("token", preferences.getString("AccessToken", null))
                .build();


        new OkHttpClient()
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    }

                    @Override
                    public void onResponse(
                            @NonNull Call call,
                            @NonNull Response response
                    ) throws IOException {
                        if (!response.isSuccessful()) {
                        } else {
                            final JSONObject responseJson = parseResponse(response.body());
                            Log.i(TAG, responseJson.toString());

                            String code, message, data;
                            code = responseJson.optString("code");
                            message = responseJson.optString("msg");
                            data = responseJson.optString("data");
                            data = data.replace("\\\"", "'");

                            Log.i(TAG, data);

                            if (code.equals("200")){
                                try {
                                    balance = new JSONObject(data).optString("balanceof");
                                    Log.i(TAG, balance);
                                    if (balance.equals("null")){
                                        balance = "0.00";
                                    }

                                    countDownLatch.countDown();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Log.i(TAG, message);

                            }

                        }
                    }
                });
    }

    List<RecentActivity> activityList = new ArrayList<>();
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
                                            try {
                                                activityList = transferToRecentActivity(activities);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        countDownLatch.countDown();
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

        Log.i(TAG, recentActivities.toString());
        return recentActivities;
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

    private void showAlert(String title, @Nullable String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create();
        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition( 0,0);
    }
}