package com.example.jacaranda.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jacaranda.Constants;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private JacarandaApplication app;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        app = (JacarandaApplication) getApplication();

        loadData();
    }

    private void loadData(){
        int request = getIntent().getIntExtra("request", 0);
        Log.i(TAG, String.valueOf(request));
        switch (request){
            case Constants.GET_BALANCE:
                getBalance();
                break;
            default:
                Intent intent=new Intent();
                intent.putExtra("data_return","EMPTY");
                setResult(RESULT_OK,intent);
                finish();
        }
    }

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

                            String balance = "UNKNOWN";

                            if (code.equals("200")){
                                try {
                                    balance = new JSONObject(data).optString("balanceof");
                                    Log.i(TAG, balance);
                                    if (balance.equals("null")){
                                        balance = "0.00";
                                    }


                                    //return balance to activity calling
                                    Intent intent=new Intent();
                                    intent.putExtra("data_return",balance);
                                    intent.putExtra("balance",balance);
                                    setResult(RESULT_OK,intent);
                                    finish();


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
}