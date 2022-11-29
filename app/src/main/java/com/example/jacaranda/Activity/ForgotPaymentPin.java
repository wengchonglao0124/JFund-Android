package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jacaranda.R;

public class ForgotPaymentPin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_payment_pin);
        initAll();
    }

    private void initAll() {
        initBack();
        initEditText();
        initNext();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_forgotPaymentPin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    EditText text;
    private void initEditText() {
        text = (EditText) findViewById(R.id.id_et_forgotPaymentPin_Email);
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

    Button next;
    private void initNext() {
        next = (Button) findViewById(R.id.id_btn_forgotPaymentPin_next);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPaymentPin.this, ForgotPinVerification.class);
                i.putExtra("name", "Forgot payment pin");
                startActivity(i);
                finish();
            }
        });
    }
}