package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.MyView.MyInputFilter;
import com.example.jacaranda.MyView.XEditText;
import com.example.jacaranda.R;

public class TransferActivity extends AppCompatActivity {
    public static TransferActivity intance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intance = this;
        setContentView(R.layout.activity_transfer);
        initAll();
    }

    private void initAll() {
        clickBack();
        clickNext();
        initIDBar();
        clickHint();
    }

    Button back;
    private void clickBack() {
        back = (Button) findViewById(R.id.id_btn_back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private XEditText text;
    private String IDNumber;
    private String temp;
    private void initIDBar() {
        text = (XEditText) findViewById(R.id.id_user_ID);
        text.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new MyInputFilter(" 0123456789");
        text.setFilters(filters);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(s.toString().length() == 19){
                   next.setBackgroundResource(R.drawable.next_button1);
                   next.setEnabled(true);
               }else{
                   next.setBackgroundResource(R.drawable.next_button);
                   next.setEnabled(false);
               }

                if(s.length() > 0){
                    temp = s.toString();
                    temp = temp.replace("  "," ");
                    if(temp.substring(temp.length() - 1).equals(" ") && temp.length()%5!= 0){
                        temp = temp.trim();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                IDNumber = s.toString();
                if (!TextUtils.isEmpty(text.getText().toString()) && !text.getText().toString().equals(temp)){
                    text.setText(temp);
                }
            }
        });
    }

    Button next;
    private void clickNext() {
        next = (Button) findViewById(R.id.id_btn_next1);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransferActivity.this, TransferActivity2.class);
                i.putExtra("ID",IDNumber);
                startActivity(i);
            }
        });
    }

    Button hint;
    TextView textView;
    private void clickHint() {
        hint = (Button) findViewById(R.id.id_btn_hint);
        textView  = (TextView) findViewById(R.id.id_tv_hint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hint.isSelected() == false){
                    textView.setText("User ID should contain 16 digits");
                    textView.setHeight(100);
                    hint.setSelected(true);
                }else{
                    textView.setText("");
                    textView.setHeight(0);
                    hint.setSelected(false);
                }
            }
        });
    }
}