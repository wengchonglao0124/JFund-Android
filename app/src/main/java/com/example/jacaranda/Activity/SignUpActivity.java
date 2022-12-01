package com.example.jacaranda.Activity;

import static com.example.jacaranda.Util.JsonToStringUtil.parseResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.example.jacaranda.MainActivity;
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

public class SignUpActivity extends AppCompatActivity {
    public static SignUpActivity intance = null;
    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intance = this;
        setContentView(R.layout.activity_sign_up);
        initAll();
    }

    private void initAll() {
        initClickEye1();
        initClickEye2();
        initSignIn();
        initSignUp();
        initBack();
    }

    Button eye;
    EditText password;
    private boolean isHidden = true;
    private void initClickEye1() {
        eye = (Button) findViewById(R.id.id_btn_signUp_eye1);
        password = (EditText) findViewById(R.id.id_et_signUp_password1);
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
                if(s.length()!=0 && email.getText().toString().length()!=0
                        && name.getText().toString().length()!=0 && password2.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    Button eye2;
    EditText password2;
    private boolean isHidden2 = true;
    private void initClickEye2() {
        eye2 = (Button) findViewById(R.id.id_btn_signUp_eye2);
        password2 = (EditText) findViewById(R.id.id_et_signUp_password2);
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
                if(s.length()!=0 && email.getText().toString().length()!=0
                        && password.getText().toString().length()!=0 && name.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    TextView signIn;
    private void initSignIn() {
        signIn = (TextView) findViewById(R.id.id_tv_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Button signUp;
    EditText name, email;
    private void initSignUp() {
        signUp = (Button) findViewById(R.id.id_btn_signUp);
        name = (EditText) findViewById(R.id.id_et_signUp_name);
        email = (EditText) findViewById(R.id.id_et_signUp_email);
        signUp.setEnabled(false);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && email.getText().toString().length()!=0
                        && password.getText().toString().length()!=0 && password2.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && name.getText().toString().length()!=0
                        && password.getText().toString().length()!=0 && password2.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_signUp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private JSONObject parseInfo(){
        try {
            //parse values in textbox and transfer to json
            JSONObject userInfo = new JSONObject();
            userInfo.put("email", email.getText().toString());
            userInfo.put("password", password.getText().toString());
            userInfo.put("username", name.getText().toString());
            Log.i(TAG, userInfo.toString());
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void register(){
        new Thread(){
            @Override
            public void run() {

                JSONObject userInfo = parseInfo();
                if (userInfo == null){
                    showAlert("Error", "Error collecting user information");
                }else{
                    //setup RequestBody
                    final RequestBody requestBody = RequestBody.create(
                            userInfo.toString(),
                            MediaType.get("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                            .url(BACKEND_URL + "/email")
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


                                            if (code.equals("200")){
                                                Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                                                intent.putExtra("email", userInfo.optString("email"));
                                                startActivity(intent);
                                            }else{
                                                showToast("Error! code:" + code);
                                            }
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