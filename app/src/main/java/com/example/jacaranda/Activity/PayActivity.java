package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.jacaranda.HintPage.SuccessfullyPay;
import com.example.jacaranda.HintPage.SuccessfullyTransferActivity;
import com.example.jacaranda.R;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initClick();
    }

    private void initClick() {
        clickBack();
        clickMore();
        clickQRcode();
    }

    View code;
    TextView amount;
    PopupWindow popupWindow;
    Button Back,pay;
    private void clickQRcode() {
        code = (View) findViewById(R.id.id_pay_QR_code);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View window = inflater.inflate(R.layout.transfer_popupwindow, null);
                popupWindow = new PopupWindow(view ,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(window);

                amount = ((TextView)popupWindow.getContentView().findViewById(R.id.id_details_amount));
                amount.setText("$ 90.00");

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
                            Intent intent = new Intent(PayActivity.this, SuccessfullyPay.class);
                            startActivity(intent);
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
}