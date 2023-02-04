package com.example.jacaranda.Activity;

import static com.example.jacaranda.Util.JsonToStringUtil.parseResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.Constants;
import com.example.jacaranda.HintPage.SuccessfullyPay;
import com.example.jacaranda.JacarandaApplication;
import com.example.jacaranda.R;
import com.example.jacaranda.Service.PayHandler;
import com.example.jacaranda.Service.WebSocketService;
import com.example.jacaranda.Util.QRcodeUtil;
import com.example.jacaranda.Util.profileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity implements ServiceConnection {
    private WebSocketService.MyBinder myBinder = null;
    private static final String TAG = "PayActivity";
    private SharedPreferences preferences;
    private JacarandaApplication app;

    Timer mTimer;
    TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (JacarandaApplication)getApplication();
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_pay);
        initUserDetails();
        initClick();
//        initQRcode();
    }

    String username, uid;
    private void initUserDetails(){
        uid = preferences.getString("userID", "0000 0000 0000 0000");
        username = preferences.getString("username", "UNKNOWN");
        TextView tv_username, tv_uid;
        tv_uid = findViewById(R.id.id_tv_pay_uid);
        tv_username = findViewById(R.id.id_tv_pay_username);

        tv_username.setText(username);
        tv_uid.setText(profileUtil.uidToUidString(uid));
    }

    private void initQRcode(){
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        Date time = new Date();
//
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("uid", uid);
//
//            jsonObject.put("time", df.format(time));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        code.setImageBitmap(QRcodeUtil.createQRCodeBitmap(jsonObject.toString(),
//                400,
//                400,
//                "UTF-8",
//                "H",
//                "1",
//                Color.BLACK,
//                Color.WHITE));

        refreshCipher();
    }

    private void initClick() {
        clickBack();
        clickMore();
        clickQRcode();
    }

    ImageView code;
    TextView tv_amount;
    PopupWindow popupWindow;
    Button Back,pay;
    private void clickQRcode() {
        code = (ImageView) findViewById(R.id.id_pay_QR_code);
        code.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showPopupWindow(String amount){
        LayoutInflater inflater = getLayoutInflater();
        View window = inflater.inflate(R.layout.transfer_popupwindow, null);
        popupWindow = new PopupWindow(window ,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(window);

        tv_amount = ((TextView)popupWindow.getContentView().findViewById(R.id.id_details_amount));
        tv_amount.setText(amount);

        Back = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_details_back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
            }
        });

        pay = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_details_transfer);
        pay.setBackgroundResource(R.drawable.pay_button);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow!=null){
                    popupWindow.dismiss();
                    Intent intent = new Intent(PayActivity.this, CheckPin.class);
                    intent.putExtra("path", Constants.PATH_PAY_PIN);
                    intent.putExtra("type", Constants.PAY);
                    intent.putExtra("nextPage", SuccessfullyPay.class);
                    intent.putExtra("fid", fid);
                    intent.putExtra("amount", amount);
                    startActivity(intent);
                }
            }
        });

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(window, Gravity.BOTTOM,0,0);
        bgAlpha(0.618f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bgAlpha(1.0f);
            }
        });
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

    Button more;
    Button PaymentSetting, RenewQRCode, HelpAndSupport;
    private void clickMore() {
        more = (Button) findViewById(R.id.id_btn_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View window = inflater.inflate(R.layout.pay_popupwindow, null);
                PopupWindow popupWindow = new PopupWindow(view ,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(window);

                PaymentSetting = (Button) popupWindow.getContentView().findViewById(R.id.id_btn_PaymentSetting);
                PaymentSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PayActivity.this, PaymentSetting.class);
                        startActivity(intent);
                    }
                });

                RenewQRCode = (Button) popupWindow.getContentView().findViewById(R.id.id_btn_RenewQRCode);

                HelpAndSupport = (Button) popupWindow.getContentView().findViewById(R.id.id_btn_HelpAndSupport);
                HelpAndSupport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PayActivity.this, HelpAndSupport.class);
                        startActivity(intent);
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

    private  void bgAlpha(float f){
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.alpha = f;
        this.getWindow().setAttributes(layoutParams);
    }

    private void refreshQRcode(String cipher){
        code.setImageBitmap(QRcodeUtil.createQRCodeBitmap(cipher,
                400,
                400,
                "UTF-8",
                "H",
                "1",
                Color.BLACK,
                Color.WHITE));
    }

    private void refreshCipher(){
        new Thread(){
            @Override
            public void run() {

                //setup RequestBody
                final RequestBody requestBody = RequestBody.create(
                        "",
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getURL() + Constants.PATH_REFRESH_QR)
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

                                        String code;
                                        code = responseJson.optString("code");

                                        if (code.equals("200")){
                                            String data;
                                            data = responseJson.optString("data");

                                            runOnUiThread(() -> {
                                                refreshQRcode(data);
                                            });
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

    @Override
    protected void onResume() {
        super.onResume();
//        startService();
        Intent intent1 = new Intent(PayActivity.this, WebSocketService.class);
        bindService(intent1, this, BIND_AUTO_CREATE);

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                refreshCipher();
            }
        };
        mTimer.schedule(mTimerTask,0, 120000);
    }

    private void startService(){
        Intent intent1 = new Intent(PayActivity.this, WebSocketService.class);
        stopService(intent1);
        Log.e("TAG", "stop----");
        Intent intent2 = new Intent(PayActivity.this, WebSocketService.class);
        startService(intent2);
    }

    private void stopService(){
        Intent intent1 = new Intent(PayActivity.this, WebSocketService.class);
        stopService(intent1);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        stopService();

        if (mTimer != null){
            mTimer.cancel();
        }
        if (mTimerTask != null){
            mTimerTask.cancel();
        }
        unbindService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    String fid, amount;
    private PayHandler payHandler = new PayHandler(this);
    public void pay(String msg) throws JSONException {
        Log.i(TAG, msg);
        final JSONObject payDetails = new JSONObject(msg);
        String code = payDetails.optString("code");
        amount = payDetails.getString("amount");
        if (code.equals("200")){
            showPopupWindow(amount);
            fid = payDetails.optString("fid");
        }else if (code.equals("201")){
            Intent intent = new Intent(PayActivity.this, SuccessfullyPay.class);
            intent.putExtra("amount", amount);
            startActivity(intent);
            finish();
        }



    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        myBinder = (WebSocketService.MyBinder) binder;
        myBinder.getService().setCallback(new WebSocketService.Callback(){
            @Override
            public void onDataChange(String data) {
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("data",data);
                msg.setData(b);
                payHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}