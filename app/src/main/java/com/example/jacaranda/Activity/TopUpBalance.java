package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jacaranda.MyView.MyInputFilter;
import com.example.jacaranda.R;

public class TopUpBalance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_balance);
        initAll();
    }

    private void initAll() {
        initBack();
        initAmount();
        initNext();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_top_up_balance_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private EditText text;
    private String  numberStr;
    private String  transferAmount;
    private void initAmount() {
        text = (EditText) findViewById(R.id.id_et_top_up_balance_amount);
        text.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new MyInputFilter(".0123456789");
        text.setFilters(filters);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("0.00")){
                    next.setBackgroundResource(R.drawable.next_button);
                    next.setEnabled(false);
                }else{
                    next.setBackgroundResource(R.drawable.next_button1);
                    next.setEnabled(true);
                }
                int lenght = s.length();
                double number = 0.00;
                if (lenght <= 1){
                    if(s.toString().equals(".")){
                        numberStr = "0.00";
                    }else{
                        number = Double.parseDouble(s.toString());
                        number =number /100;
                        numberStr = number + "";
                    }
                }else {
                    if (s.toString().contains(".")){
                        if(s.toString().substring(s.length() - 1).equals(".")){
                            numberStr = getMoneyString(s.toString().substring(0,s.length() - 1));
                        }else{
                            numberStr = getMoneyString(s.toString());
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                transferAmount = s.toString();
                if (!TextUtils.isEmpty(text.getText().toString()) && !text.getText().toString().equals(numberStr)){
                    text.setText(numberStr);
                    text.setSelection(numberStr.length());
                }
            }
        });
    }

    private String getMoneyString(String money){
        String overMoney = "";
        String[] pointBoth = money.split("\\.");
        String beginOne = pointBoth[0].substring(pointBoth[0].length()-1);
        String endOne = pointBoth[1].substring(0, 1);

        String beginPoint = pointBoth[0].substring(0,pointBoth[0].length()-1);
        String endPoint = pointBoth[1].substring(1);

        if (pointBoth[1].length()>2){
            overMoney=  pointBoth[0]+endOne+"."+endPoint;
        }else if (
                pointBoth[1].length()<2){
            overMoney = beginPoint+"."+beginOne+pointBoth[1];
        }else {
            overMoney = money;
        }

        String overLeft = overMoney.substring(0,overMoney.indexOf("."));

        if (overLeft ==null || overLeft == ""||overLeft.length()<1){
            overMoney = "0"+overMoney;
        }else if(overLeft.length() > 1 && "0".equals(overLeft.subSequence(0, 1))){
            overMoney = overMoney.substring(1);
        }

        return overMoney;
    }

    Button next;
    private void initNext() {
        next = (Button) findViewById(R.id.id_btn_top_up_balance_next);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}