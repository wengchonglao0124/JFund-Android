package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.MyView.PwdEditText;
import com.example.jacaranda.R;


public class EnterNewPin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_pin);
        initAll();
    }

    private void initAll() {
        initTitle();
        initBack();
        initPin();
    }

    TextView title;
    String text;
    private void initTitle() {
        title = (TextView) findViewById(R.id.id_text_EnterNewPin);
        text = this.getIntent().getStringExtra("name");
        title.setText(text);
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_EnterNewPin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    PwdEditText pin;
    private void initPin() {
        pin = (PwdEditText) findViewById(R.id.id_et_newPin);
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6){
                    Intent intent = new Intent(EnterNewPin.this, ConfirmNewPin.class);
                    intent.putExtra("name", text);
                    intent.putExtra("newPin", pin.getText().toString());
//                    int request_type = getIntent().getIntExtra("request", 0);
                    intent.putExtra("intent", getIntent());
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