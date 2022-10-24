package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jacaranda.R;

public class EnterUserName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_name);
        initAll();
    }

    private void initAll() {
        initBack();
        initEditText();
        initClearAll();
        initSave();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_enterUserName_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    EditText name;
    private void initEditText() {
        name = (EditText) findViewById(R.id.id_et_enterUserName);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
                    save.setBackgroundResource(R.drawable.save_button1);
                    save.setEnabled(true);
                    clear.setVisibility(View.VISIBLE);
                    clear.setEnabled(true);
                }else{
                    save.setBackgroundResource(R.drawable.save_button);
                    save.setEnabled(false);
                    clear.setVisibility(View.GONE);
                    clear.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    Button clear;
    private void initClearAll() {
        clear = (Button) findViewById(R.id.id_btn_enterUserName_clear);
        clear.setVisibility(View.GONE);
        clear.setEnabled(false);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
            }
        });
    }

    Button save;
    private void initSave() {
        save = (Button) findViewById(R.id.id_btn_enterUserName_save);
        save.setEnabled(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", name.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}