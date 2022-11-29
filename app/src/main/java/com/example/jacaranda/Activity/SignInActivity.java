package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jacaranda.MainActivity;
import com.example.jacaranda.R;
import com.example.jacaranda.SelectAccount;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initAll();
    }

    private void initAll() {
        // initAdvertising();
        initClickEye();
        intiForgotPassword();
        initSignUp();
        initSignIn();
    }

    Button signIn;
    EditText email;
    private void initSignIn() {
        signIn = (Button) findViewById(R.id.id_btn_signIn);
        email = (EditText) findViewById(R.id.id_et_Email);
        signIn.setEnabled(false);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && password.getText().toString().length()!=0 ){
                    signIn.setBackgroundResource(R.drawable.sign_in_button1);
                    signIn.setEnabled(true);
                }else{
                    signIn.setBackgroundResource(R.drawable.sign_in_button2);
                    signIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initAdvertising() {
        Intent intent = new Intent(SignInActivity.this, AdvertisingActivity.class);
        startActivity(intent);
    }

    Button eye;
    EditText password;
    private boolean isHidden = true;
    private void initClickEye() {
        eye = (Button) findViewById(R.id.id_btn_eye);
        password = (EditText) findViewById(R.id.id_et_Password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setSelected(true);
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setSelected(false);
                }
                isHidden = !isHidden;
                password.postInvalidate();
                CharSequence charSequence = password.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && email.getText().toString().length()!=0 ){
                    signIn.setBackgroundResource(R.drawable.sign_in_button1);
                    signIn.setEnabled(true);
                }else{
                    signIn.setBackgroundResource(R.drawable.sign_in_button2);
                    signIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    TextView forgot;
    private void intiForgotPassword() {
        forgot = (TextView) findViewById(R.id.id_tv_forgotPassword);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    TextView signUp;
    private void initSignUp() {
        signUp = (TextView) findViewById(R.id.id_tv_signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}