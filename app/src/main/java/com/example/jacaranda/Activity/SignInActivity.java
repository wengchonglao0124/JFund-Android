package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
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
        initAdvertising();
        initClickEye();
        intiForgotPassword();
        initSignUp();
        initSignIn();
    }

    Button signIn;
    private void initSignIn() {
        signIn = (Button) findViewById(R.id.id_btn_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SelectAccount.class);
                startActivity(intent);
                finish();
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
    }

    TextView forgot;
    private void intiForgotPassword() {
        forgot = (TextView) findViewById(R.id.id_tv_forgotPassword);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
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