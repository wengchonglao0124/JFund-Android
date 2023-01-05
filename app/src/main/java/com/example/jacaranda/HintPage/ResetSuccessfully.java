package com.example.jacaranda.HintPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jacaranda.Activity.ForgotPassword;
import com.example.jacaranda.Activity.TransferActivity;
import com.example.jacaranda.Activity.Verification2;
import com.example.jacaranda.MainActivity;
import com.example.jacaranda.R;

public class ResetSuccessfully extends AppCompatActivity {
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_successfully);

        back = (Button) findViewById(R.id.id_btn_resetPasswordSuccessfully_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetSuccessfully.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}