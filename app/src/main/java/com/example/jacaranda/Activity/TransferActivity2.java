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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.MyView.MyInputFilter;
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


public class TransferActivity2 extends AppCompatActivity {
    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private static final String PATH = "/transferTo";
    private static final String TAG = "TransferActivity2";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer2);
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        initAll();
    }

    private void initAll() {
        clickBack();
        initIDBar();
        clickNext();
        setID();
    }

    TextView IDNumber;
    private void setID() {
        IDNumber = (TextView) findViewById(R.id.id_tv_IDNumber);
        IDNumber.setText(getIntent().getStringExtra("ID"));
    }

    Button back;
    private void clickBack() {
        back = (Button) findViewById(R.id.id_btn_back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private EditText text;
    private String  numberStr;
    private String  transferAmount;
    private void initIDBar() {
        text = (EditText) findViewById(R.id.id_transfer_amount);
        text.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new MyInputFilter(".0123456789");
        text.setFilters(filters);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("0.00")){
                    Continue.setBackgroundResource(R.drawable.continue_button);
                    Continue.setEnabled(false);
                }else{
                    Continue.setBackgroundResource(R.drawable.continue_button1);
                    Continue.setEnabled(true);
                }
                int lenght = s.length();
                double number = 0.00;
                if (lenght <= 1){
                    if(s.toString().equals(".")){
                        numberStr = "0.00";
                    }else{
                        number = Double.parseDouble(s.toString());
                        number =number /100;
                        numberStr = number + "";
                    }
                }else {
                    if (s.toString().contains(".")){
                        if(s.toString().substring(s.length() - 1).equals(".")){
                            numberStr = getMoneyString(s.toString().substring(0,s.length() - 1));
                        }else{
                            numberStr = getMoneyString(s.toString());
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                transferAmount = s.toString();
                if (!TextUtils.isEmpty(text.getText().toString()) && !text.getText().toString().equals(numberStr)){
                    text.setText(numberStr);
                    text.setSelection(numberStr.length());
                }
            }
        });
    }

    private String getMoneyString(String money){
        String overMoney = "";
        String[] pointBoth = money.split("\\.");
        String beginOne = pointBoth[0].substring(pointBoth[0].length()-1);
        String endOne = pointBoth[1].substring(0, 1);

        String beginPoint = pointBoth[0].substring(0,pointBoth[0].length()-1);
        String endPoint = pointBoth[1].substring(1);

        if (pointBoth[1].length()>2){
            overMoney=  pointBoth[0]+endOne+"."+endPoint;
        }else if (
                pointBoth[1].length()<2){
            overMoney = beginPoint+"."+beginOne+pointBoth[1];
        }else {
            overMoney = money;
        }

        String overLeft = overMoney.substring(0,overMoney.indexOf("."));

        if (overLeft ==null || overLeft == ""||overLeft.length()<1){
            overMoney = "0"+overMoney;
        }else if(overLeft.length() > 1 && "0".equals(overLeft.subSequence(0, 1))){
            overMoney = overMoney.substring(1);
        }

        return overMoney;
    }

    Button Continue;
    PopupWindow popupWindow;
    TextView amount;
    Button Back;
    Button Transfer;
    ProgressBar progressBar;
    private void clickNext() {
        Continue = (Button) findViewById(R.id.id_btn_continue);
        Continue.setEnabled(false);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View window = inflater.inflate(R.layout.transfer_popupwindow, null);
                popupWindow = new PopupWindow(view ,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(window);

                amount = ((TextView)popupWindow.getContentView().findViewById(R.id.id_details_amount));
                amount.setText("$ "+text.getText());

                Back = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_details_back);
                Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(popupWindow!=null){
                            popupWindow.dismiss();
                        }
                    }
                });

                Transfer = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_details_transfer);
                progressBar = (ProgressBar)findViewById(R.id.id_progressBar);
                Transfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(popupWindow!=null){
                            popupWindow.dismiss();
                            progressBar.setVisibility(View.VISIBLE);
                            transferTo();

//                            progressBar.setVisibility(View.INVISIBLE);
//                            Intent intent = new Intent(TransferActivity2.this, EnterPin.class);
//                            intent.putExtra("amount", transferAmount);
//                            startActivity(intent);
//                            finish();
                        }
                    }
                });

                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    private  void bgAlpha(float f){
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.alpha = f;
        this.getWindow().setAttributes(layoutParams);
    }

    private JSONObject parseInfo(){
        try {
            //parse values in textbox and transfer to json
            JSONObject transferInfo = new JSONObject();
            transferInfo.put("UserID", IDNumber.getText().toString().replace(" ",""));
            transferInfo.put("Amount", text.getText().toString());
            Log.i(TAG, transferInfo.toString());
            return transferInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void transferTo(){
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

                                            String code, data;
                                            code = responseJson.optString("code");
                                            data = responseJson.optString("data");


                                            Timer timer = new Timer();
                                            TimerTask timerTask = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    if (code.equals("200")){

                                                        try {
                                                            final JSONObject responseBody = new JSONObject(data);
                                                            String amount, fid;
                                                            fid = responseBody.optString("fid");
                                                            amount = responseBody.optString("amount");
                                                            Intent intent = new Intent(TransferActivity2.this, CheckPin.class);
                                                            intent.putExtra("fid", fid);
                                                            intent.putExtra("amount", amount);
                                                            startActivity(intent);
                                                            finish();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

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