package com.example.jacaranda.Activity;

import static com.example.jacaranda.Util.JsonToStringUtil.parseResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.HintPage.PaymentPinChanged;
import com.example.jacaranda.HintPage.ResetSuccessfully;
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

public class ResetPassword extends AppCompatActivity {

    private JacarandaApplication app;
    private static final String TAG = "ConfirmNewPin";
    private static final String PATH_CHANGE_PWD = "/changePin";
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (JacarandaApplication)getApplication();
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_reset_password);
        initAll();
    }

    private void initAll() {
        initBack();
        initClickEye1();
        initClickEye2();
        initChange();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_resetPassword_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Button eye1;
    EditText password1;
    TextView hint1,hint2;
    View view1,view2;
    boolean numberFlag = false;
    boolean upperCaseFlag = false;
    boolean lowerCaseFlag = false;
    private boolean isHidden1 = true;
    private void initClickEye1() {
        eye1 = (Button) findViewById(R.id.id_btn_resetPassword);
        password1 = (EditText) findViewById(R.id.id_et_resetPassword);
        hint1 = (TextView) findViewById(R.id.id_tv_resetPassword_hint1);
        hint2 = (TextView) findViewById(R.id.id_tv_resetPassword_hint2);
        view1 = (View) findViewById(R.id.id_resetPassword_case1);
        view2 = (View) findViewById(R.id.id_resetPassword_case2);
        password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden1) {
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye1.setSelected(true);
                } else {
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye1.setSelected(false);
                }
                isHidden1 = !isHidden1;
                password1.postInvalidate();
                CharSequence charSequence = password1.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=6){
                    hint1.setTextColor(Color.parseColor("#25c26e"));
                    view1.setBackgroundResource(R.drawable.subtract2);
                    if(step1&&step2){
                        //change.setBackgroundResource(R.drawable.save_button1);
                        change.setEnabled(true);
                    }
                }else{
                    hint1.setTextColor(Color.parseColor("#c4c4c4"));
                    view1.setBackgroundResource(R.drawable.subtract);
                    //change.setBackgroundResource(R.drawable.save_button);
                    change.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                numberFlag = false;
                upperCaseFlag = false;
                lowerCaseFlag = false;
                String password = s.toString();
                for(int i=0;i < password.length();i++) {
                    if(Character.isDigit(password.charAt(i))){
                        numberFlag = true;
                    }else if(Character.isUpperCase(password.charAt(i))){
                        upperCaseFlag = true;
                    }else if(Character.isLowerCase((password.charAt(i)))){
                        lowerCaseFlag = true;
                    }
                }
                if(numberFlag&&upperCaseFlag&&lowerCaseFlag){
                    hint2.setTextColor(Color.parseColor("#25c26e"));
                    view2.setBackgroundResource(R.drawable.subtract2);
                    step1 = true;
                }else{
                    hint2.setTextColor(Color.parseColor("#c4c4c4"));
                    view2.setBackgroundResource(R.drawable.subtract);
                    step1 = false;
                }
            }
        });
    }

    Button eye2;
    EditText password2;
    private boolean isHidden2 = true;
    private void initClickEye2() {
        eye2 = (Button) findViewById(R.id.id_btn_resetPassword2);
        password2 = (EditText) findViewById(R.id.id_et_resetPassword2);
        password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden2) {
                    password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye2.setSelected(true);
                } else {
                    password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye2.setSelected(false);
                }
                isHidden2 = !isHidden2;
                password2.postInvalidate();
                CharSequence charSequence = password2.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(password1.getText().toString())){
                    step2 = true;
                }else{
                    step2 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(step1&&step2){
                    change.setBackgroundResource(R.drawable.change_password);
                    change.setEnabled(true);
                }else{
                    change.setBackgroundResource(R.drawable.change_password1);
                    change.setEnabled(false);
                }
            }
        });
    }

    Button change;
    boolean step1 = false;
    boolean step2 = false;
    private void initChange() {
        change = (Button) findViewById(R.id.id_btn_resetPassword_change);
        change.setEnabled(false);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewPwd(parseInfo());
            }
        });
    }

    private JSONObject parseInfo(){

        //parse values in textbox and transfer to json
        JSONObject json_body = new JSONObject();

        try {
            json_body.put("email", getIntent().getStringExtra("email"));
            json_body.put("password", password2.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json_body;
    }

    private void setNewPwd(JSONObject json_body){
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

                                        String code, message, data;
                                        code = responseJson.optString("code");
                                        message = responseJson.optString("msg");
                                        data = responseJson.optString("data");
                                        Log.i(TAG, data);

                                        if (code.equals("200")){
                                            Intent intent = new Intent(ResetPassword.this, Verification2.class);
                                            startActivity(intent);
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