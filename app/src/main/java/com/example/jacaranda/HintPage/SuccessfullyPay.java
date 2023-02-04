package com.example.jacaranda.HintPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.Activity.SetUpPinActivity;
import com.example.jacaranda.Activity.SignInActivity;
import com.example.jacaranda.MainActivity;
import com.example.jacaranda.R;
import com.example.jacaranda.SelectAccount;

public class SuccessfullyPay extends AppCompatActivity {
    Button done;
    TextView tv_amount;
    TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_pay);
        done = (Button) findViewById(R.id.id_btn_SuccessfullyPay_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SuccessfullyPay.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tv_amount = (TextView) findViewById(R.id.id_tv_tranferAmount);

        String amount = getIntent().getStringExtra("amount");
        if (amount == null){
            amount = "UNKNOWN";
        }
        tv_amount.setText("$ "+ amount);

    }


}