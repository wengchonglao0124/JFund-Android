package com.example.jacaranda.Activity;

import static com.example.jacaranda.Util.JsonToStringUtil.parseResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.jacaranda.Constants;
import com.example.jacaranda.HintPage.ChangePasswordSuccessfully;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.MyView.PwdEditText;
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

public class ChangePaymentPin extends AppCompatActivity {
    private JacarandaApplication app;
    private static final String TAG = "ChangePaymentPin";
    private static final String PATH_CHANGE_PWD = "/verify_pincode";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_payment_pin);
        app = (JacarandaApplication)getApplication();
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        initAll();
    }

    private void initAll() {
        initBack();
        initPin();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_changePaymentPin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    PwdEditText pin;
    private void initPin() {
        pin = (PwdEditText) findViewById(R.id.id_et_oldPin);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6){
                    Intent intent = new Intent(ChangePaymentPin.this, EnterNewPin.class);
                    intent.putExtra("name", "Change payment pin");
                    intent.putExtra("request", Constants.PIN_CHANGE);
                    intent.putExtra("oldPin", pin.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private JSONObject parseInfo(){

        //parse values in textbox and transfer to json
        JSONObject json_body = new JSONObject();

        try {
            json_body.put("code", pin.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json_body;
    }

    private void verifyPin(JSONObject json_body){
        new Thread(){
            @Override
            public void run() {

                Log.i(TAG, json_body.toString());
                //setup RequestBody
                final RequestBody requestBody = RequestBody.create(
                        json_body.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getURL() + PATH_CHANGE_PWD)
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

                                        String code, message, data, accessToken, refreshToken, userID;
                                        code = responseJson.optString("code");
                                        message = responseJson.optString("msg");
                                        data = responseJson.optString("data");
                                        Log.i(TAG, data);

                                        if (code.equals("200")){
                                            Intent intent = new Intent(ChangePaymentPin.this, EnterNewPin.class);
                                            intent.putExtra("name", "Change payment pin");
                                            intent.putExtra("request", Constants.PIN_CHANGE);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            showToast(message);
                                        }

                                    } catch (IOException | JSONException e) {
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