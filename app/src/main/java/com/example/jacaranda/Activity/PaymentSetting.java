package com.example.jacaranda.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import okhttp3.ResponseBody;

public class PaymentSetting extends AppCompatActivity {
    private final String TAG = "PaymentSetting";
    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private JacarandaApplication app;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_setting);

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        app = (JacarandaApplication) getApplication();

        initAll();
    }

    Switch s_pinfree;
    private void initPinFreeSwtich(){
        s_pinfree = (Switch) findViewById(R.id.id_s_pinfree_switch);
        s_pinfree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setPinFree("50");
                }else{
                    setPinFree("0");
                }
            }
        });
    }

    private void initAll() {
        initBack();
        initPinFree();
        initPinFreeSwtich();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_paymentSetting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    ConstraintLayout cl;
    Button Back;
    PwdEditText pin;
    private void initPinFree() {
        cl = (ConstraintLayout) findViewById(R.id.id_cl_paymentSetting_free);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View window = inflater.inflate(R.layout.payment_popupwindow, null);
                PopupWindow popupWindow = new PopupWindow(view ,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(window);

                Back = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_confirmIdentity_back);
                Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(popupWindow!=null){
                            popupWindow.dismiss();
                        }
                    }
                });

                pin = (PwdEditText) popupWindow.getContentView().findViewById(R.id.id_et_confirmIdentity);
                pin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length() == 6){
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(pin.getWindowToken(), 0);

                            if(popupWindow!=null){
                                popupWindow.dismiss();
                            }

                            chooseLimit();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                bgAlpha(0.618f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        bgAlpha(1.0f);
                    }
                });
            }
        });
    }

    private void bgAlpha(float f){
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.alpha = f;
        this.getWindow().setAttributes(layoutParams);
    }

    View v1,v2,v3,v4,check1,check2,check3,check4;
    TextView limit;
    private void chooseLimit(){
        LayoutInflater inflater = getLayoutInflater();
        View window = inflater.inflate(R.layout.pinfree_popupwindow, null);
        PopupWindow popupWindow = new PopupWindow(cl ,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(window);

        limit = (TextView) findViewById(R.id.id_tv_limit);
        v1 = popupWindow.getContentView().findViewById(R.id.id_view_AUD50);
        v2 = popupWindow.getContentView().findViewById(R.id.id_view_AUD100);
        v3 = popupWindow.getContentView().findViewById(R.id.id_view_AUD150);
        v4 = popupWindow.getContentView().findViewById(R.id.id_view_AUD200);
        check1 = popupWindow.getContentView().findViewById(R.id.id_AUD50_check);
        check2 = popupWindow.getContentView().findViewById(R.id.id_AUD100_check);
        check3 = popupWindow.getContentView().findViewById(R.id.id_AUD150_check);
        check4 = popupWindow.getContentView().findViewById(R.id.id_AUD200_check);

        if(limit.getText().toString().equals("AUD50 / trade")){
            check1.setVisibility(View.VISIBLE);
        }else if(limit.getText().toString().equals("AUD100 / trade")){
            check2.setVisibility(View.VISIBLE);
        }else if(limit.getText().toString().equals("AUD150 / trade")){
            check3.setVisibility(View.VISIBLE);
        }else if(limit.getText().toString().equals("AUD200 / trade")){
            check4.setVisibility(View.VISIBLE);
        }

        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check1.setVisibility(View.VISIBLE);
                check2.setVisibility(View.INVISIBLE);
                check3.setVisibility(View.INVISIBLE);
                check4.setVisibility(View.INVISIBLE);
                limit.setText("AUD50 / trade");
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check1.setVisibility(View.INVISIBLE);
                check2.setVisibility(View.VISIBLE);
                check3.setVisibility(View.INVISIBLE);
                check4.setVisibility(View.INVISIBLE);
                limit.setText("AUD100 / trade");
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check1.setVisibility(View.INVISIBLE);
                check2.setVisibility(View.INVISIBLE);
                check3.setVisibility(View.VISIBLE);
                check4.setVisibility(View.INVISIBLE);
                limit.setText("AUD150 / trade");
            }
        });
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check1.setVisibility(View.INVISIBLE);
                check2.setVisibility(View.INVISIBLE);
                check3.setVisibility(View.INVISIBLE);
                check4.setVisibility(View.VISIBLE);
                limit.setText("AUD200 / trade");
            }
        });

        Back = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_pinFree_back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.showAtLocation(cl, Gravity.BOTTOM,0,0);
        bgAlpha(0.618f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bgAlpha(1.0f);
            }
        });
    }

    private void setPinFree(String amount){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Amount", amount);
            Log.i(TAG, jsonObject.toString());
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }


        //setup RequestBody
        final RequestBody requestBody = RequestBody.create(
                jsonObject.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BACKEND_URL + "/setPin_free")
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



                            if (code.equals("200")){

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