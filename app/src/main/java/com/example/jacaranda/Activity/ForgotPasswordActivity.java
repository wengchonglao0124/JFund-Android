package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jacaranda.HintPage.PasswordChangedActivity;
import com.example.jacaranda.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initAll();
    }

    private void initAll() {
        initClickBack();
        initClickNext();
    }

    Button back;
    private void initClickBack() {
        back = (Button) findViewById(R.id.id_btn_forgot_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Button next;
    private void initClickNext() {
        next = (Button) findViewById(R.id.id_btn_forgot_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, PasswordChangedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}