package com.example.jacaranda.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.MainActivity;
import com.example.jacaranda.MyView.MyInputFilter;
import com.example.jacaranda.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

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

public class TopUpBalance extends AppCompatActivity {

    private JacarandaApplication app;

    private static final String TAG = "TopUpActivity";

    private static final String PATH = "/create-payment-intent";


    private String paymentIntentClientSecret;
    private PaymentSheet paymentSheet;
    private PaymentSheet.CustomerConfiguration customerConfiguration;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_balance);

        app = (JacarandaApplication) getApplication();

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        initAll();
    }

    private void initAll() {
        initBack();
        initAmount();
        initNext();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_top_up_balance_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private EditText text;
    private String  numberStr;
    private String  transferAmount;
    private void initAmount() {
        text = (EditText) findViewById(R.id.id_et_top_up_balance_amount);
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
                    next.setBackgroundResource(R.drawable.next_button);
                    next.setEnabled(false);
                }else{
                    next.setBackgroundResource(R.drawable.next_button1);
                    next.setEnabled(true);
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

    Button next;
    private void initNext() {
        next = (Button) findViewById(R.id.id_btn_top_up_balance_next);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPaymentIntent();
            }
        });
    }


    private void fetchPaymentIntent() {
        JSONObject json_amount = new JSONObject();

        try {

            json_amount.put("amounts", Double.parseDouble(text.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final RequestBody requestBody = RequestBody.create(
                json_amount.toString(),
                MediaType.get("application/json; charset=utf-8")

        );

        Log.i(TAG, requestBody.toString());

        Request request = new Request.Builder()
                .url(app.getURL() + PATH)
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
                            final JSONObject responseJson = parseResponse(response.body());
                            Log.i(TAG, responseJson.toString());

                            String code, message, data;
                            code = responseJson.optString("code");
                            message = responseJson.optString("msg");
                            data = responseJson.optString("data");
                            data = data.replace("\\\"", "'");


                            if (code.equals("200")){
                                try {
                                    JSONObject json_data = new JSONObject(data);
                                    Log.i(TAG, json_data.toString());
                                    paymentIntentClientSecret = json_data.optString("paymentIntent");
//                            runOnUiThread(() -> payButton.setEnabled(true));
                                    Log.i(TAG, "Retrieved PaymentIntent");
                                    Log.i(TAG, paymentIntentClientSecret.toString());

                                    customerConfiguration = new PaymentSheet.CustomerConfiguration(
                                            json_data.optString("customer"),
                                            json_data.optString("ephemeralKey")
                                    );

                                    PaymentConfiguration.init(getApplicationContext(), json_data.optString("publishableKey"));

                                    createPaymentSheet();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                showToast(message);
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


    private void createPaymentSheet(){
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Jacaranda, Inc.")
                .customer(customerConfiguration)
                .allowsDelayedPaymentMethods(true)
                .build();

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            showToast("Payment complete!");
            Intent intent = new Intent();
            intent.setClass(TopUpBalance.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.i(TAG, "Payment canceled!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
            showAlert("Payment failed", error.getLocalizedMessage());
        }
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