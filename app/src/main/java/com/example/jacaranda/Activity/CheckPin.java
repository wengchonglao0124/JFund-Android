package com.example.jacaranda.Activity;


import static com.example.jacaranda.Util.JsonToStringUtil.parseResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.HintPage.SuccessfullyTransferActivity;
import com.example.jacaranda.MyView.PwdEditText;
import com.example.jacaranda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class CheckPin extends AppCompatActivity {
    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private static final String PATH = "/checkTransferTo";
    private static final String TAG = "CheckPin";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pin);
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        initAll();
    }

    private void initAll() {
        initTitle();
        initBack();
        initPin();
    }

    TextView title;
    String text;
    private void initTitle() {
        title = (TextView) findViewById(R.id.id_text_EnterPin);
        text = this.getIntent().getStringExtra("name");
        title.setText(text);
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_CheckPin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    PwdEditText pin;
    private void initPin() {
        pin = (PwdEditText) findViewById(R.id.id_et_Pin);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6){
                    checkPin();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private JSONObject parseInfo(){
        try {
            //parse values in textbox and transfer to json
            JSONObject transferInfo = new JSONObject();
            transferInfo.put("pin", Objects.requireNonNull(pin.getText()).toString());
            transferInfo.put("fid", getIntent().getStringExtra("fid"));
            Log.i(TAG, transferInfo.toString());
            return transferInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkPin(){
        new Thread(){
            @Override
            public void run() {

                JSONObject transferInfo = parseInfo();
                if (transferInfo == null){
                    showAlert("Error", "Error collecting user information");
                }else{
                    //setup RequestBody
                    final RequestBody requestBody = RequestBody.create(
                            transferInfo.toString(),
                            MediaType.get("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                            .url(BACKEND_URL + PATH)
                            .post(requestBody)
                            .addHeader("token",preferences.getString("AccessToken", null))
                            .build();


                    new OkHttpClient()
                            .newCall(request)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    showAlert("Failed to load data", "Error: " + e.toString());
                                }

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
                                        try {
                                            final JSONObject responseJson = parseResponse(response.body());
                                            Log.i(TAG, responseJson.toString());

                                            String code;
                                            code = responseJson.optString("code");


                                            Timer timer = new Timer();
                                            TimerTask timerTask = new TimerTask() {
                                                @Override
                                                public void run() {
//                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    if (code.equals("200")){
                                                        Intent intent = new Intent(CheckPin.this, SuccessfullyTransferActivity.class);
                                                        intent.putExtra("amount", getIntent().getStringExtra("amount"));
                                                        Log.i(TAG, getIntent().getStringExtra("username"));
                                                        intent.putExtra("username", getIntent().getStringExtra("username"));
                                                        startActivity(intent);
                                                        finish();
                                                    }else{
                                                        showToast("Error! code:" + code);
                                                    }
                                                }
                                            };
                                            timer.schedule(timerTask,1000);


                                        }catch (IOException | JSONException e) {
                                            Log.e(TAG, "Error parsing response", e);
                                        }
                                    }
                                }
                            });
                }

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