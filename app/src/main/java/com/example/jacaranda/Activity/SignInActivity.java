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
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.Database.LoginDBHelper;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.Modle.UserCredential;
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

public class SignInActivity extends AppCompatActivity {

    private JacarandaApplication app;

    private static final String TAG = "SignInActivity";

    private static final String PATH = "/login";

    private SharedPreferences preferences;
    private LoginDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        app = (JacarandaApplication)getApplication();
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        initAll();
    }

    private void initAll() {
        // initAdvertising();
        initClickEye();
        intiForgotPassword();
        initSignUp();
        initSignIn();
        initRememberMe();
    }

    CheckBox rememberMe;
    private void initRememberMe(){
        rememberMe = (CheckBox) findViewById(R.id.id_ck_rememberMe);
    }

    Button signIn;
    EditText email;
    private void initSignIn() {
        signIn = (Button) findViewById(R.id.id_btn_signIn);
        email = (EditText) findViewById(R.id.id_et_Email);
        signIn.setEnabled(false);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject user_json = parseInfo();
                authenticateEmailPassword(user_json);
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && password.getText().toString().length()!=0 ){
                    signIn.setBackgroundResource(R.drawable.sign_in_button1);
                    signIn.setEnabled(true);
                }else{
                    signIn.setBackgroundResource(R.drawable.sign_in_button2);
                    signIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String pwd = mHelper.findPasswordByEmail(email.getText().toString());
                    Log.i(TAG, pwd);
                    password.setText(pwd);
                    rememberMe.setChecked(pwd != "");
                }
            }
        });
    }

    private void initAdvertising() {
        Intent intent = new Intent(SignInActivity.this, AdvertisingActivity.class);
        startActivity(intent);
    }

    Button eye;
    EditText password;
    private boolean isHidden = true;
    private void initClickEye() {
        eye = (Button) findViewById(R.id.id_btn_eye);
        password = (EditText) findViewById(R.id.id_et_Password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setSelected(true);
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setSelected(false);
                }
                isHidden = !isHidden;
                password.postInvalidate();
                CharSequence charSequence = password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && email.getText().toString().length()!=0 ){
                    signIn.setBackgroundResource(R.drawable.sign_in_button1);
                    signIn.setEnabled(true);
                }else{
                    signIn.setBackgroundResource(R.drawable.sign_in_button2);
                    signIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    TextView forgot;
    private void intiForgotPassword() {
        forgot = (TextView) findViewById(R.id.id_tv_forgotPassword);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    TextView signUp;
    private void initSignUp() {
        signUp = (TextView) findViewById(R.id.id_tv_signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private JSONObject parseInfo(){

        //parse values in textbox and transfer to json
        JSONObject userInfo = new JSONObject();

        try {
            userInfo.put("email", email.getText().toString());
            userInfo.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    private void authenticateEmailPassword(JSONObject user_json){
        new Thread(){
            @Override
            public void run() {

                Log.i(TAG, user_json.toString());
                //setup RequestBody
                final RequestBody requestBody = RequestBody.create(
                        user_json.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getURL() + PATH)
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
                                            showToast("Login succeeded!");

                                            rememberUser(user_json);

                                            try {
                                                final JSONObject responseBody = new JSONObject(data);
                                                accessToken = responseBody.optString("AccessToken");
                                                refreshToken = responseBody.optString("RefreshToken");
                                                userID = responseBody.optString("UserID");

                                                SharedPreferences.Editor  editor = preferences.edit();
                                                editor.putString("AccessToken", accessToken);
                                                editor.putString("RefreshToken", refreshToken);
                                                editor.putString("userID", userID);
                                                editor.apply();
//                                                editor.commit();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            Intent intent = new Intent();
                                            intent.setClass(SignInActivity.this, SelectAccount.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void rememberUser(JSONObject user_json){
        UserCredential userCredential = new UserCredential(user_json.optString("email"),
                user_json.optString("password"));
        if (rememberMe.isChecked()){
            mHelper.saveUser(userCredential);
        }
    }

    private void reload(){
        UserCredential userCredential = mHelper.queryUserOnTop();
        if (userCredential != null){
            email.setText(userCredential.email);
            password.setText(userCredential.password);
            rememberMe.setChecked(true);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = LoginDBHelper.getInstance(this);
        mHelper.openWriteLink();
        mHelper.openReadLink();
        reload();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }
}