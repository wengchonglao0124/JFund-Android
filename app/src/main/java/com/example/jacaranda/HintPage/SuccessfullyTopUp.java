package com.example.jacaranda.HintPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.R;

public class SuccessfullyTopUp extends AppCompatActivity {
    Button done;
    TextView amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_top_up);

        amount = (TextView) findViewById(R.id.id_tv_TopUpAmount) ;
        amount.setText("$ "+ getIntent().getStringExtra("amount"));

        done = (Button) findViewById(R.id.id_btn_SuccessfullyTopUp_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}