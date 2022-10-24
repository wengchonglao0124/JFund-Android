package com.example.jacaranda.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.Fragment.ProfileFragment;
import com.example.jacaranda.MainActivity;
import com.example.jacaranda.R;

import java.util.Locale;

public class AccountInformation extends AppCompatActivity {
    TextView name;
    TextView capital;
    String userName;
    String first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        initCLick();
        name = (TextView) findViewById(R.id.id_tv_accountInformation_name);
        capital = (TextView) findViewById(R.id.id_tv_accountInformation_capital);

        name.setText(getIntent().getStringExtra("name"));
        capital.setText(getIntent().getStringExtra("capital"));
    }

    private void initCLick() {
        initBack();
        initChangeUserName();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_accountInformation_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name.getText().toString());
                setResult(RESULT_CANCELED, getIntent().putExtras(bundle));
                finish();
            }
        });
    }

    ConstraintLayout cl;
    private void initChangeUserName() {
        cl = (ConstraintLayout) findViewById(R.id.id_cl_changeUserName);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInformation.this, EnterUserName.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                userName = data.getStringExtra("name");
                first = userName.substring(0,1).toUpperCase(Locale.ROOT);
                name.setText(userName);
                capital.setText(first);
            }
        }
    }
}