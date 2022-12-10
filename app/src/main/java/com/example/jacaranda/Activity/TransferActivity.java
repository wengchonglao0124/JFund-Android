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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.MyView.MyInputFilter;
import com.example.jacaranda.MyView.XEditText;
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

public class TransferActivity extends AppCompatActivity {
    public static TransferActivity intance = null;
    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private static final String PATH = "/checkID";
    private static final String TAG = "TransferActivity";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intance = this;
        setContentView(R.layout.activity_transfer);
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        initAll();
    }

    private void initAll() {
        clickBack();
        clickNext();
        initIDBar();
        clickHint();
    }

    Button back;
    private void clickBack() {
        back = (Button) findViewById(R.id.id_btn_back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private XEditText text;
    private String IDNumber;
    private String temp;
    private void initIDBar() {
        text = (XEditText) findViewById(R.id.id_user_ID);
        text.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new MyInputFilter(" 0123456789");
        text.setFilters(filters);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(s.toString().length() == 19){
                   next.setBackgroundResource(R.drawable.next_button1);
                   next.setEnabled(true);
               }else{
                   next.setBackgroundResource(R.drawable.next_button);
                   next.setEnabled(false);
               }

                if(s.length() > 0){
                    temp = s.toString();
                    temp = temp.replace("  "," ");
                    if(temp.substring(temp.length() - 1).equals(" ") && temp.length()%5!= 0){
                        temp = temp.trim();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                IDNumber = s.toString();
                if (!TextUtils.isEmpty(text.getText().toString()) && !text.getText().toString().equals(temp)){
                    text.setText(temp);
                }
            }
        });
    }

    Button next;
    private void clickNext() {
        next = (Button) findViewById(R.id.id_btn_next1);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkID();
//                Intent i = new Intent(TransferActivity.this, TransferActivity2.class);
//                i.putExtra("ID",IDNumber);
//                startActivity(i);
            }
        });
    }

    Button hint;
    TextView textView;
    private void clickHint() {
        hint = (Button) findViewById(R.id.id_btn_hint);
        textView  = (TextView) findViewById(R.id.id_tv_hint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hint.isSelected() == false){
                    textView.setText("User ID should contain 16 digits");
                    textView.setHeight(100);
                    hint.setSelected(true);
                }else{
                    textView.setText("");
                    textView.setHeight(0);
                    hint.setSelected(false);
                }
            }
        });
    }

    private JSONObject parseInfo(){
        try {
            //parse values in textbox and transfer to json
            JSONObject userInfo = new JSONObject();
            userInfo.put("UserID", IDNumber.replace(" ",""));
            Log.i(TAG, userInfo.toString());
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void checkID(){
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

                                            String code, message, data;
                                            code = responseJson.optString("code");
                                            message = responseJson.optString("msg");
                                            data = responseJson.optString("data");

                                            if (code.equals("200")){
                                                String username;
                                                final JSONObject responseBody = new JSONObject(data);
                                                username = responseBody.optString("UserName");

                                                Intent i = new Intent(TransferActivity.this, TransferActivity2.class);
                                                i.putExtra("ID",IDNumber);
                                                i.putExtra("username",username);
                                                startActivity(i);
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