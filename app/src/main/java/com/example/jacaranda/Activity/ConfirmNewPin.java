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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.Constants;
import com.example.jacaranda.HintPage.PaymentPinChanged;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.MyView.PwdEditText;
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

public class ConfirmNewPin extends AppCompatActivity {

    private JacarandaApplication app;
    private static final String TAG = "ConfirmNewPin";
    private static final String PATH_CHANGE_PWD = "/changePin";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (JacarandaApplication)getApplication();
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_confirm_new_pin);
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
        title = (TextView) findViewById(R.id.id_text_ConfirmNewPin);
        text = this.getIntent().getStringExtra("name");
        title.setText(text);
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_ConfirmNewPin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    PwdEditText pin;
    private void initPin() {
        pin = (PwdEditText) findViewById(R.id.id_et_confirmNewPin);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6){
                    String oldPin = getIntent().getStringExtra("newPin");
                    if (oldPin.equals(pin.getText().toString())){
                        try {
                            setNewPin(parseInfo());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        showToast("Pin does not match");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    String request_url;
    private JSONObject parseInfo() throws JSONException {
        //parse values in textbox and transfer to json
        JSONObject json_body = new JSONObject();

        Intent old_intent = (Intent) getIntent().getExtras().get("intent");
        int request_type = old_intent.getIntExtra("request", 0);

        if (request_type == Constants.PIN_FORGOT){
            request_url = app.getURL() + Constants.PATH_FORGOT_PIN;
            json_body.put("pin", pin.getText().toString());
        }else if (request_type == Constants.PIN_CHANGE){
            request_url = app.getURL() + Constants.PATH_CHANGE_Pin;
            json_body.put("newPin", pin.getText().toString());
            json_body.put("oldPin", old_intent.getStringExtra("oldPin"));
        }else{
            showAlert("ERROR! PLEASE RETRY AGAIN", "");
        }

        return json_body;
    }

    private void setNewPin(JSONObject json_body){
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
                        .url(request_url)
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
                                            Intent intent = new Intent(ConfirmNewPin.this, ForgotPinVerification.class);
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