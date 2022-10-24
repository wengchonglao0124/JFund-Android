package com.example.jacaranda.HintPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jacaranda.R;

public class PasswordChangedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changed);
        initClick();
    }

    Button signIn;
    private void initClick() {
        signIn = (Button) findViewById(R.id.id_btn_changed_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}