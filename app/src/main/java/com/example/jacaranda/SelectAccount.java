package com.example.jacaranda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.jacaranda.Activity.SignInActivity;

public class SelectAccount extends AppCompatActivity {
    ConstraintLayout account1,account2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        account1 = findViewById(R.id.id_cl_select_account1);
        account2 = findViewById(R.id.id_cl_select_account2);
        account1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAccount.this, DebitCard.class);
                startActivity(intent);
            }
        });
        account2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAccount.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}