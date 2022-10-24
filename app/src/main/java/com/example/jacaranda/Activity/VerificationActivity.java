package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.jacaranda.HintPage.SignUpSuccessfullyActivity;
import com.example.jacaranda.R;

import java.util.Timer;
import java.util.TimerTask;

public class VerificationActivity extends AppCompatActivity {
    private EditText code1, code2, code3, code4, code5, code6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        code1 = (EditText) findViewById(R.id.id_otp_code1);
        code2 = (EditText) findViewById(R.id.id_otp_code2);
        code3 = (EditText) findViewById(R.id.id_otp_code3);
        code4 = (EditText) findViewById(R.id.id_otp_code4);
        code5 = (EditText) findViewById(R.id.id_otp_code5);
        code6 = (EditText) findViewById(R.id.id_otp_code6);

        int length1 = code1.getText().toString().replace(" ", "").length();
        int length2 = code2.getText().toString().replace(" ", "").length();
        int length3 = code3.getText().toString().replace(" ", "").length();
        int length4 = code4.getText().toString().replace(" ", "").length();
        int length5 = code5.getText().toString().replace(" ", "").length();
        int length6 = code6.getText().toString().replace(" ", "").length();
        if(length1 == 0 && length2 == 0 && length3 == 0 && length4 == 0 && length5 == 0 && length6 == 0){
            code1.setFocusable(true);
            code2.setFocusable(false);
            code3.setFocusable(false);
            code4.setFocusable(false);
            code5.setFocusable(false);
            code6.setFocusable(false);
        }

        code1.addTextChangedListener(textWatcher);
        code2.addTextChangedListener(textWatcher);
        code3.addTextChangedListener(textWatcher);
        code4.addTextChangedListener(textWatcher);
        code5.addTextChangedListener(textWatcher);
        code6.addTextChangedListener(textWatcher);

        initBack();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_verification_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()==1){
                if(code1.isFocusable()){
                    code2.setFocusable(true);
                    code2.setFocusableInTouchMode(true);
                }else if(code2.isFocusable()){
                    code3.setFocusable(true);
                    code3.setFocusableInTouchMode(true);
                }else if(code3.isFocusable()){
                    code4.setFocusable(true);
                    code4.setFocusableInTouchMode(true);
                }else if(code4.isFocusable()){
                    code5.setFocusable(true);
                    code5.setFocusableInTouchMode(true);
                }else if(code5.isFocusable()){
                    code6.setFocusable(true);
                    code6.setFocusableInTouchMode(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        if (editable.toString().length() == 1) {
            if (code1.isFocused()) {
                code1.setFocusable(false);
                code2.requestFocus();
            }else if (code2.isFocused()) {
                code2.setFocusable(false);
                code3.requestFocus();
            }else if(code3.isFocused()){
                code3.setFocusable(false);
                code4.requestFocus();
            }else if(code4.isFocused()){
                code4.setFocusable(false);
                code5.requestFocus();
            }else if(code5.isFocused()){
                code5.setFocusable(false);
                code6.requestFocus();
            }else if(code6.isFocused()){
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.hideSoftInputFromWindow(code6.getWindowToken(), 0);
                isTrue();
            }
        }
        }
    };

    private void isTrue(){
        final String getCode = code1.getText().toString()+code2.getText().toString()+
                code3.getText().toString()+code4.getText().toString()+
                code5.getText().toString()+code6.getText().toString();
        if(getCode.equals("111111")){
            code1.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code2.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code3.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code4.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code5.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            code6.setBackgroundResource(R.drawable.rounded_rectangle_code_right);
            Intent intent = new Intent(VerificationActivity.this, SignUpSuccessfullyActivity.class);
            startActivity(intent);
            finish();
        }else{
            code1.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code2.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code3.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code4.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code5.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            code6.setBackgroundResource(R.drawable.rounded_rectangle_code_false);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    code1.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code2.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code3.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code4.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code5.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                    code6.setBackgroundResource(R.drawable.rounded_rectangle_code_normal);
                }
            };
            timer.schedule(timerTask,1000);
        }
    }

    boolean flag = true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            if(code6.isFocused()){
                if (!code6.getText().toString().equals("")) {
                    code6.getText().clear();
                    code6.requestFocus();
                    flag = false;
                } else if (!flag) {
                    code6.clearFocus();
                    code6.setFocusable(false);
                    code5.setFocusableInTouchMode(true);
                    code5.getText().clear();
                    code5.requestFocus();
                    flag = true;
                } else {
                    code6.getText().clear();
                    code6.requestFocus();
                    flag = false;
                }
            } else if (code5.isFocused()) {
                code5.clearFocus();
                code5.setFocusable(false);
                code4.setFocusableInTouchMode(true);
                code4.getText().clear();
                code4.requestFocus();
            } else if (code4.isFocused()) {
                code4.clearFocus();
                code4.setFocusable(false);
                code3.setFocusableInTouchMode(true);
                code3.getText().clear();
                code3.requestFocus();
            } else if (code3.isFocused()) {
                code3.clearFocus();
                code3.setFocusable(false);
                code2.setFocusableInTouchMode(true);
                code2.getText().clear();
                code2.requestFocus();
            } else if (code2.isFocused()) {
                code2.clearFocus();
                code2.setFocusable(false);
                code1.setFocusableInTouchMode(true);
                code1.getText().clear();
                code1.requestFocus();
            }
            return true;
        }else{
            return super.onKeyUp(keyCode, event);
        }
    }
}