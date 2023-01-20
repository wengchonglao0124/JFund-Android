package com.example.jacaranda.Activity;

import static com.example.jacaranda.Util.JsonToStringUtil.parseResponse;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jacaranda.Constants;
import com.example.jacaranda.MainActivity;
import com.example.jacaranda.R;
import com.example.jacaranda.SelectAccount;


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

public class AdvertisingActivity extends AppCompatActivity {

    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private static final String TAG = "AdvertisingActivity";

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        initClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initClick() {
        initSkip();
    }

    private Button skip;
    private void initSkip() {
        skip = (Button) findViewById(R.id.id_btn_skipAd);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload();
            }
        });
    }

    private void reload(){
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        String token = preferences.getString("RefreshToken", null);

        if (token == null){
            toSignin();
        }else{
            validateToken(token);
        }
    }

    private void toSignin(){
        Intent intent = new Intent();
        intent.setClass(AdvertisingActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    ActivityResultLauncher requestBalanceLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.d("AdvertisingActivity",result.getData().getStringExtra("data_return"));
        String balance = result.getData().getStringExtra("balance");
        Intent intent = new Intent();
        intent.setClass(AdvertisingActivity.this, SelectAccount.class);
        intent.putExtra("balance", balance);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    });


    private void validateToken(String token){
        new Thread(){
            @Override
            public void run() {

                Log.i(TAG, token);

                //setup RequestBody
                final RequestBody requestBody = RequestBody.create(
                        "",
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(BACKEND_URL + "/getAccesstoken")
                        .addHeader("token", token)
                        .post(requestBody)
                        .build();


                new OkHttpClient()
                        .newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                showAlert("Failed to load data", "Error: " + e.toString());
                                toSignin();
                            }

                            @Override
                            public void onResponse(
                                    @NonNull Call call,
                                    @NonNull Response response
                            ) throws IOException {
                                if (!response.isSuccessful()) {
//                                    showAlert("Failed to load page",
//                                            "Error: " + response.toString());
                                    toSignin();
                                } else {

                                    try {
                                        final JSONObject responseJson = parseResponse(response.body());
                                        Log.i(TAG, responseJson.toString());

                                        String code, message, data, accessToken;
                                        code = responseJson.optString("code");
                                        message = responseJson.optString("msg");
                                        data = responseJson.optString("data");


                                        if (code.equals("200")){

                                            try {
                                                final JSONObject responseBody = new JSONObject(data);
                                                accessToken = responseBody.optString("AccessToken");

                                                SharedPreferences.Editor  editor = preferences.edit();
                                                editor.putString("AccessToken", accessToken);
                                                editor.apply();
//                                                editor.commit();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                            int info = preferences.getInt("info", -1);
                                            switch (info){
                                                case 0:
                                                    Intent intent = new Intent();
                                                    intent.setClass(AdvertisingActivity.this, SetUpPinActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    break;
                                                case 1:
                                                    Intent loadIntent =new Intent(AdvertisingActivity.this,LoadingActivity.class);
                                                    loadIntent.putExtra("request", Constants.GET_BALANCE);
                                                    requestBalanceLauncher.launch(loadIntent);
                                                    break;
                                                default:
                                                    Intent intent2 = new Intent();
                                                    intent2.setClass(AdvertisingActivity.this, SignInActivity.class);
                                                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent2);
                                            }


                                        }else{
//                                            showToast("Error! code:" + code);
                                            toSignin();
                                        }
                                    }catch (IOException | JSONException e) {
                                        Log.e(TAG, "Error parsing response", e);
                                    }
                                }
                            }
                        });
            }
        }.start();
    }

    private void showAlert(String title, @Nullable String message) {
        runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create();
            dialog.show();
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

}