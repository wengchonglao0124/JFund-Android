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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jacaranda.HintPage.ResetSuccessfully;
import com.example.jacaranda.HintPage.SignUpSuccessfullyActivity;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Verification2 extends AppCompatActivity {

    private JacarandaApplication app;
    private static final String TAG = "Verification2";
    private static final String PATH_CHANGE_PWD = "/verify_pswdcode";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (JacarandaApplication)getApplication();
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_verification2);
        initAll();
    }

    private void initAll() {
        initBack();
        initOTP();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_forgotPasswordVerification_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private EditText code1, code2, code3, code4, code5, code6;
    private void initOTP() {
        code1 = (EditText) findViewById(R.id.id_verification_code1);
        code2 = (EditText) findViewById(R.id.id_verification_code2);
        code3 = (EditText) findViewById(R.id.id_verification_code3);
        code4 = (EditText) findViewById(R.id.id_verification_code4);
        code5 = (EditText) findViewById(R.id.id_verification_code5);
        code6 = (EditText) findViewById(R.id.id_verification_code6);

        int length1 = code1.getText().toString().replace(" ", "").length();
        int length2 = code2.getText().toString().replace(" ", "").length();
        int length3 = code3.getText().toString().replace(" ", "").length();
        int length4 = code4.getText().toString().replace(" ", "").length();
        int length5 = code5.getText().toString().replace(" ", "").length();
        int length6 = code6.getText().toString().replace(" ", "").length();
        if(length1 == 0 && length2 == 0 && length3 == 0 && length4 == 0 && length5 == 0 && length6 == 0){
            code1.setFocusable(true);
            code2.setFocusable(false);
            code3.setFocusable(false);
            code4.setFocusable(false);
            code5.setFocusable(false);
            code6.setFocusable(false);
        }

        code1.addTextChangedListener(textWatcher);
        code2.addTextChangedListener(textWatcher);
        code3.addTextChangedListener(textWatcher);
        code4.addTextChangedListener(textWatcher);
        code5.addTextChangedListener(textWatcher);
        code6.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()==1){
                if(code1.isFocusable()){
                    code2.setFocusable(true);
                    code2.setFocusableInTouchMode(true);
                }else if(code2.isFocusable()){
                    code3.setFocusable(true);
                    code3.setFocusableInTouchMode(true);
                }else if(code3.isFocusable()){
                    code4.setFocusable(true);
                    code4.setFocusableInTouchMode(true);
                }else if(code4.isFocusable()){
                    code5.setFocusable(true);
                    code5.setFocusableInTouchMode(true);
                }else if(code5.isFocusable()){
                    code6.setFocusable(true);
                    code6.setFocusableInTouchMode(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() == 1) {
                if (code1.isFocused()) {
                    code1.setFocusable(false);
                    code2.requestFocus();
                }else if (code2.isFocused()) {
                    code2.setFocusable(false);
                    code3.requestFocus();
                }else if(code3.isFocused()){
                    code3.setFocusable(false);
                    code4.requestFocus();
                }else if(code4.isFocused()){
                    code4.setFocusable(false);
                    code5.requestFocus();
                }else if(code5.isFocused()){
                    code5.setFocusable(false);
                    code6.requestFocus();
                }else if(code6.isFocused()){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    // imm.hideSoftInputFromWindow(code6.getWindowToken(), 0);
                    isTrue();
                }
            }
        }
    };

    private void isTrue(){
        final String getCode = code1.getText().toString()+code2.getText().toString()+
                code3.getText().toString()+code4.getText().toString()+
                code5.getText().toString()+code6.getText().toString();
        if(getCode.equals("111111")){
            code1.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code2.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code3.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code4.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code5.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code6.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            Intent intent = new Intent(Verification2.this, ResetSuccessfully.class);
            startActivity(intent);
            finish();
        }else{
            code1.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code2.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code3.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code4.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code5.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code6.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    code1.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code2.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code3.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code4.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code5.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code6.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                }
            };
            timer.schedule(timerTask,1000);
        }
    }

    boolean flag = true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            if(code6.isFocused()){
                if (!code6.getText().toString().equals("")) {
                    code6.getText().clear();
                    code6.requestFocus();
                    flag = false;
                } else if (!flag) {
                    code6.clearFocus();
                    code6.setFocusable(false);
                    code5.setFocusableInTouchMode(true);
                    code5.getText().clear();
                    code5.requestFocus();
                    flag = true;
                } else {
                    code6.getText().clear();
                    code6.requestFocus();
                    flag = false;
                }
            } else if (code5.isFocused()) {
                code5.clearFocus();
                code5.setFocusable(false);
                code4.setFocusableInTouchMode(true);
                code4.getText().clear();
                code4.requestFocus();
            } else if (code4.isFocused()) {
                code4.clearFocus();
                code4.setFocusable(false);
                code3.setFocusableInTouchMode(true);
                code3.getText().clear();
                code3.requestFocus();
            } else if (code3.isFocused()) {
                code3.clearFocus();
                code3.setFocusable(false);
                code2.setFocusableInTouchMode(true);
                code2.getText().clear();
                code2.requestFocus();
            } else if (code2.isFocused()) {
                code2.clearFocus();
                code2.setFocusable(false);
                code1.setFocusableInTouchMode(true);
                code1.getText().clear();
                code1.requestFocus();
            }
            return true;
        }else{
            return super.onKeyUp(keyCode, event);
        }
    }

    private JSONObject parseInfo(){

        try {
            final String getCode = code1.getText().toString()+code2.getText().toString()+
                    code3.getText().toString()+code4.getText().toString()+
                    code5.getText().toString()+code6.getText().toString();

            //parse values in textbox and transfer to json
            JSONObject userInfo = new JSONObject();
            userInfo.put("email", getIntent().getStringExtra("email"));
            userInfo.put("code", getCode);
            Log.i(TAG, userInfo.toString());
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void verify(){
        new Thread(){
            @Override
            public void run() {
                JSONObject userInfo = parseInfo();

                //setup RequestBody
                final RequestBody requestBody = RequestBody.create(
                        userInfo.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getURL() + "/code")
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

                                        String code;
                                        code = responseJson.optString("code");

                                        if(code.equals("200")){
                                            code1.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
                                            code2.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
                                            code3.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
                                            code4.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
                                            code5.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
                                            code6.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
                                            Intent intent = new Intent(Verification2.this, ResetSuccessfully.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            code1.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
                                            code2.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
                                            code3.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
                                            code4.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
                                            code5.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
                                            code6.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
                                            Timer timer = new Timer();
                                            TimerTask timerTask = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    code1.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                                                    code2.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                                                    code3.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                                                    code4.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                                                    code5.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                                                    code6.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                                                }
                                            };
                                            timer.schedule(timerTask,1000);
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