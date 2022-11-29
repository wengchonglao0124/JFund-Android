package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        initEmail();
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
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, PasswordChangedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    EditText text;
    private void initEmail() {
        text = (EditText) findViewById(R.id.id_et_registeredEmail);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
                    next.setBackgroundResource(R.drawable.next_button1);
                    next.setEnabled(true);
                }else{
                    next.setBackgroundResource(R.drawable.next_button);
                    next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}