package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.jacaranda.MyView.PwdEditText;
import com.example.jacaranda.R;
import com.example.jacaranda.SelectAccount;

public class ChangePaymentPin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_payment_pin);
        initAll();
    }

    private void initAll() {
        initBack();
        initPin();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_changePaymentPin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    PwdEditText pin;
    private void initPin() {
        pin = (PwdEditText) findViewById(R.id.id_et_oldPin);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6){
                    Intent intent = new Intent(ChangePaymentPin.this, EnterNewPin.class);
                    intent.putExtra("name", "Change payment pin");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}