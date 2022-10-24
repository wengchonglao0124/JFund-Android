package com.example.jacaranda.HintPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jacaranda.Activity.SignUpActivity;
import com.example.jacaranda.R;

public class SignUpSuccessfullyActivity extends AppCompatActivity {
    private Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_successfully);
        getStarted = (Button) findViewById(R.id.id_btn_getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SignUpActivity().intance.finish();
                finish();
            }
        });
    }
}