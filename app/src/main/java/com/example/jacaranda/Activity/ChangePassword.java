package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jacaranda.HintPage.ChangePasswordSuccessfully;
import com.example.jacaranda.R;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initAll();
    }

    private void initAll() {
        initBack();
        initClickEye1();
        initClickEye2();
        initClickEye3();
        initSave();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_changePassword_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Button eye1;
    EditText password1;
    private boolean isHidden1 = true;
    private void initClickEye1() {
        eye1 = (Button) findViewById(R.id.id_btn_changePassword1);
        password1 = (EditText) findViewById(R.id.id_et_changePassword1);
        password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden1) {
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye1.setSelected(true);
                } else {
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye1.setSelected(false);
                }
                isHidden1 = !isHidden1;
                password1.postInvalidate();
                CharSequence charSequence = password1.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
                    step1 = true;
                }else{
                    step1 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(step1&&step2&&step3){
                    save.setBackgroundResource(R.drawable.save_button1);
                    save.setEnabled(true);
                }else{
                    save.setBackgroundResource(R.drawable.save_button);
                    save.setEnabled(false);
                }
            }
        });
    }

    Button eye2;
    EditText password2;
    TextView hint1,hint2;
    View view1,view2;
    boolean numberFlag = false;
    boolean upperCaseFlag = false;
    boolean lowerCaseFlag = false;
    private boolean isHidden2 = true;
    private void initClickEye2() {
        eye2 = (Button) findViewById(R.id.id_btn_changePassword2);
        password2 = (EditText) findViewById(R.id.id_et_changePassword2);
        hint1 = (TextView) findViewById(R.id.id_tv_newPassword_hint1);
        hint2 = (TextView) findViewById(R.id.id_tv_newPassword_hint2);
        view1 = (View) findViewById(R.id.id_changePassword_case1);
        view2 = (View) findViewById(R.id.id_changePassword_case2);
        password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden2) {
                    password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye2.setSelected(true);
                } else {
                    password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye2.setSelected(false);
                }
                isHidden2 = !isHidden2;
                password2.postInvalidate();
                CharSequence charSequence = password2.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=6){
                    hint1.setTextColor(Color.parseColor("#25c26e"));
                    view1.setBackgroundResource(R.drawable.subtract2);
                    if(step1&&step2&&step3){
                        save.setBackgroundResource(R.drawable.save_button1);
                        save.setEnabled(true);
                    }
                }else{
                    hint1.setTextColor(Color.parseColor("#c4c4c4"));
                    save.setBackgroundResource(R.drawable.save_button);
                    view1.setBackgroundResource(R.drawable.subtract);
                    save.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                numberFlag = false;
                upperCaseFlag = false;
                lowerCaseFlag = false;
                String password = s.toString();
                for(int i=0;i < password.length();i++) {
                    if(Character.isDigit(password.charAt(i))){
                        numberFlag = true;
                    }else if(Character.isUpperCase(password.charAt(i))){
                        upperCaseFlag = true;
                    }else if(Character.isLowerCase((password.charAt(i)))){
                        lowerCaseFlag = true;
                    }
                }
                if(numberFlag&&upperCaseFlag&&lowerCaseFlag){
                    hint2.setTextColor(Color.parseColor("#25c26e"));
                    view2.setBackgroundResource(R.drawable.subtract2);
                    step2 = true;
                }else{
                    hint2.setTextColor(Color.parseColor("#c4c4c4"));
                    view2.setBackgroundResource(R.drawable.subtract);
                    step2 = false;
                }
            }
        });
    }

    Button eye3;
    EditText password3;
    private boolean isHidden3 = true;
    private void initClickEye3() {
        eye3 = (Button) findViewById(R.id.id_btn_changePassword3);
        password3 = (EditText) findViewById(R.id.id_et_changePassword3);
        password3.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eye3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden3) {
                    password3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye3.setSelected(true);
                } else {
                    password3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye3.setSelected(false);
                }
                isHidden3 = !isHidden3;
                password3.postInvalidate();
                CharSequence charSequence = password3.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        password3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(password2.getText().toString())){
                    step3 = true;
                }else{
                    step3 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(step1&&step2&&step3){
                    save.setBackgroundResource(R.drawable.save_button1);
                    save.setEnabled(true);
                }else{
                    save.setBackgroundResource(R.drawable.save_button);
                    save.setEnabled(false);
                }
            }
        });
    }

    Button save;
    boolean step1 = false;
    boolean step2 = false;
    boolean step3 = false;
    private void initSave() {
        save = (Button) findViewById(R.id.id_btn_changePassword_save);
        save.setEnabled(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, ChangePasswordSuccessfully.class);
                startActivity(intent);
                finish();
            }
        });
    }
}