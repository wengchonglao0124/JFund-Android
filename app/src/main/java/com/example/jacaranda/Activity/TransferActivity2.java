package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jacaranda.HintPage.SucessfullyTransferActivity;
import com.example.jacaranda.MyView.MyInputFilter;
import com.example.jacaranda.R;

import java.util.Timer;
import java.util.TimerTask;


public class TransferActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer2);
        initAll();
    }

    private void initAll() {
        clickBack();
        initIDBar();
        clickNext();
        setID();
    }

    TextView IDNumber;
    private void setID() {
        IDNumber = (TextView) findViewById(R.id.id_tv_IDNumber);
        IDNumber.setText(getIntent().getStringExtra("ID"));
    }

    Button back;
    private void clickBack() {
        back = (Button) findViewById(R.id.id_btn_back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private EditText text;
    private String  numberStr;
    private String  transferAmount;
    private void initIDBar() {
        text = (EditText) findViewById(R.id.id_transfer_amount);
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
                    Continue.setBackgroundResource(R.drawable.continue_button);
                    Continue.setClickable(false);
                }else{
                    Continue.setBackgroundResource(R.drawable.continue_button1);
                    Continue.setClickable(true);
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

    Button Continue;
    PopupWindow popupWindow;
    TextView amount;
    Button Back;
    Button Transfer;
    ProgressBar progressBar;
    private void clickNext() {
        Continue = (Button) findViewById(R.id.id_btn_continue);
        Continue.setClickable(false);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View window = inflater.inflate(R.layout.transfer_popupwindow, null);
                popupWindow = new PopupWindow(view ,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(window);

                amount = ((TextView)popupWindow.getContentView().findViewById(R.id.id_details_amount));
                amount.setText("$ "+text.getText());

                Back = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_details_back);
                Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(popupWindow!=null){
                            popupWindow.dismiss();
                        }
                    }
                });

                Transfer = (Button)popupWindow.getContentView().findViewById(R.id.id_btn_details_transfer);
                progressBar = (ProgressBar)findViewById(R.id.id_progressBar);
                Transfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(popupWindow!=null){
                            popupWindow.dismiss();
                            progressBar.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(TransferActivity2.this, SucessfullyTransferActivity.class);

                            Timer timer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    intent.putExtra("amount", transferAmount);
                                    startActivity(intent);
                                    finish();
                                }
                            };
                            timer.schedule(timerTask,1000);
                        }
                    }
                });

                popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                bgAlpha(0.618f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        bgAlpha(1.0f);
                    }
                });
            }
        });
    }

    private  void bgAlpha(float f){
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.alpha = f;
        this.getWindow().setAttributes(layoutParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_DOT){
            text.setText("0.03");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}