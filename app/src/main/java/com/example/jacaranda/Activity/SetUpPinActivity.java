package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.example.jacaranda.Constants;
import com.example.jacaranda.MyView.PwdEditText;
import com.example.jacaranda.R;

public class SetUpPinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_pin);
        initAll();
    }

    private void initAll() {
        initPin();
    }

    PwdEditText pin;
    private void initPin() {
        pin = (PwdEditText) findViewById(R.id.id_et_setupPin_newPin);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6){
                    Intent intent = new Intent(SetUpPinActivity.this, ConfirmNewPin.class);
                    intent.putExtra("name", "Set up Pin");
                    intent.putExtra("newPin", pin.getText().toString());
//                    int request_type = getIntent().getIntExtra("request", 0);
                    intent.putExtra("intent",
                            getIntent().putExtra("request", Constants.PIN_SET));
                    startActivity(intent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pin.clearText();
        pin.animate();
    }
}