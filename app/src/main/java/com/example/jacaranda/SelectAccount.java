package com.example.jacaranda;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jacaranda.Activity.AdvertisingActivity;
import com.example.jacaranda.Activity.LoadingActivity;
import com.example.jacaranda.Activity.SignInActivity;

public class SelectAccount extends AppCompatActivity {
    private static final String TAG = "SelectAccount";
    ConstraintLayout account1,account2;

    TextView tv_DebitBalance, tv_JacBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        account1 = findViewById(R.id.id_cl_select_account1);
        account2 = findViewById(R.id.id_cl_select_account2);
        account1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAccount.this, DebitCard.class);
                startActivity(intent);
            }
        });
        account2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAccount.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tv_JacBalance = (TextView) findViewById(R.id.id_tv_selectAc_JacBalance);
        String balance = getIntent().getStringExtra("balance");
        Log.i(TAG, balance);
        tv_JacBalance.setText(balance);

//        Intent intent = new Intent(this, LoadingActivity.class);
//        intent.putExtra("request", Constants.HOMEFRAG);
//        mainActLauncher.launch(intent);
    }

    ActivityResultLauncher mainActLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Log.d("SelectAccount",result.getData().getStringExtra("data_return"));
        String balance = result.getData().getStringExtra("balance");
        Intent intent = new Intent();
        intent.setClass(SelectAccount.this, MainActivity.class);
        intent.putExtra("balance", balance);
        intent.putExtra("activities", balance);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    });
}