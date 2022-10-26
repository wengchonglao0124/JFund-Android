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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jacaranda.R;

public class SignUpActivity extends AppCompatActivity {
    public static SignUpActivity intance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intance = this;
        setContentView(R.layout.activity_sign_up);
        initAll();
    }

    private void initAll() {
        initClickEye1();
        initClickEye2();
        initSignIn();
        initSignUp();
        initBack();
    }

    Button eye;
    EditText password;
    private boolean isHidden = true;
    private void initClickEye1() {
        eye = (Button) findViewById(R.id.id_btn_signUp_eye1);
        password = (EditText) findViewById(R.id.id_et_signUp_password1);
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
                if(s.length()!=0 && email.getText().toString().length()!=0
                        && name.getText().toString().length()!=0 && password2.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    Button eye2;
    EditText password2;
    private boolean isHidden2 = true;
    private void initClickEye2() {
        eye2 = (Button) findViewById(R.id.id_btn_signUp_eye2);
        password2 = (EditText) findViewById(R.id.id_et_signUp_password2);
        password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden2) {
                    password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye2.setSelected(true);
                } else {
                    password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye2.setSelected(false);
                }
                isHidden2 = !isHidden2;
                password2.postInvalidate();
                CharSequence charSequence = password2.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && email.getText().toString().length()!=0
                        && password.getText().toString().length()!=0 && name.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    TextView signIn;
    private void initSignIn() {
        signIn = (TextView) findViewById(R.id.id_tv_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Button signUp;
    EditText name, email;
    private void initSignUp() {
        signUp = (Button) findViewById(R.id.id_btn_signUp);
        name = (EditText) findViewById(R.id.id_et_signUp_name);
        email = (EditText) findViewById(R.id.id_et_signUp_email);
        signUp.setEnabled(false);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                startActivity(intent);
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && email.getText().toString().length()!=0
                        && password.getText().toString().length()!=0 && password2.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0 && name.getText().toString().length()!=0
                        && password.getText().toString().length()!=0 && password2.getText().toString().length()!=0){
                    signUp.setBackgroundResource(R.drawable.sign_up_button1);
                    signUp.setEnabled(true);
                }else{
                    signUp.setBackgroundResource(R.drawable.sign_up_button2);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_signUp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}