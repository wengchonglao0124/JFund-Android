package com.example.jacaranda.HintPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.Activity.TransferActivity;
import com.example.jacaranda.R;

public class SuccessfullyTransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucessfully_transfer);
        initAll();
    }

    private void initAll() {
        initBtn();
        setAmount();
        setName();
    }

    TextView amount;
    private void setAmount() {
        amount = (TextView) findViewById(R.id.id_tv_tranferAmount);
        amount.setText("$ "+getIntent().getStringExtra("amount"));
    }

    TextView name;
    private void setName() {
        name = (TextView) findViewById(R.id.id_tv_tranferSuccessfully_name);
        name.setText(" To "+getIntent().getStringExtra("username"));
    }

    Button done;
    private void initBtn() {
        done = (Button) findViewById(R.id.id_btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TransferActivity().intance.finish();
                finish();
            }
        });
    }
}