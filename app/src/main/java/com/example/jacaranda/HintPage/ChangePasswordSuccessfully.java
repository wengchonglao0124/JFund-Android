package com.example.jacaranda.HintPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jacaranda.Activity.ChangePassword;
import com.example.jacaranda.Activity.SignInActivity;
import com.example.jacaranda.R;

public class ChangePasswordSuccessfully extends AppCompatActivity {
    Button back;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_change_password_successfully);
        back = (Button) findViewById(R.id.id_btn_changePasswordSuccessfully_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut(){
        SharedPreferences.Editor  editor = preferences.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(ChangePasswordSuccessfully.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}