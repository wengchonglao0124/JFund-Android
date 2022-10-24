package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jacaranda.R;

public class AccountSecurity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        initAll();
    }

    private void initAll() {
        initBack();
        initChangePassword();
        initForgotPassword();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_accountSecurity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ConstraintLayout changePassword;
    private void initChangePassword() {
        changePassword = (ConstraintLayout) findViewById(R.id.id_cl_accountSecurity_changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSecurity.this, ChangePassword.class);
                startActivity(intent);
            }
        });
    }

    ConstraintLayout forgotPassword;
    private void initForgotPassword() {
        forgotPassword = (ConstraintLayout) findViewById(R.id.id_cl_accountSecurity_fogotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSecurity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
}