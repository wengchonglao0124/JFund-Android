package com.example.jacaranda.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.jacaranda.Activity.AccountInformation;
import com.example.jacaranda.Activity.AccountSecurity;
import com.example.jacaranda.Activity.HelpAndSupport;
import com.example.jacaranda.Activity.PaymentSetting;
import com.example.jacaranda.Activity.SignInActivity;
import com.example.jacaranda.R;

import java.util.Locale;

public class ProfileFragment extends Fragment {
    private static final int RESULT_CANCELED = 0;
    TextView name,capital;
    String userName,first;

    private SharedPreferences preferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    View RootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(RootView == null){
            RootView = inflater.inflate(R.layout.fragment_profile, container, false);
            initAll();
        }
        return RootView;
    }

    private void initAll() {
        initName();
        initAccountInformation();
        initAccountSecurity();
        initPaymentSetting();
        initHelpAndSupport();
        initNotificationPreference();
        initLogOut();
    }

    private void initName() {
        name = (TextView) RootView.findViewById(R.id.id_tv_profile_name);
        capital = (TextView) RootView.findViewById(R.id.id_tv_profile_capital);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_CANCELED){
                userName = data.getStringExtra("name");
                first = userName.substring(0,1).toUpperCase(Locale.ROOT);
                name.setText(userName);
                capital.setText(first);
            }
        }
    }

    ConstraintLayout accountInformation;
    private void initAccountInformation() {
        accountInformation = (ConstraintLayout) RootView.findViewById(R.id.id_cl_profile_accountInformation);
        accountInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountInformation.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("capital",capital.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
    }

    ConstraintLayout accountSecurity;
    private void initAccountSecurity() {
        accountSecurity = (ConstraintLayout) RootView.findViewById(R.id.id_cl_profile_acconutSecurity);
        accountSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountSecurity.class);
                startActivity(intent);
            }
        });
    }

    ConstraintLayout paymentSetting;
    private void initPaymentSetting() {
        paymentSetting = (ConstraintLayout) RootView.findViewById(R.id.id_cl_profile_paymentSetting);
        paymentSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentSetting.class);
                startActivity(intent);
            }
        });
    }

    ConstraintLayout helpAndSupport;
    private void initHelpAndSupport() {
        helpAndSupport = (ConstraintLayout) RootView.findViewById(R.id.id_cl_profile_HelpAndSupport);
        helpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpAndSupport.class);
                startActivity(intent);
            }
        });
    }

    ConstraintLayout notificationPreference;
    private void initNotificationPreference() {
        notificationPreference = (ConstraintLayout) RootView.findViewById(R.id.id_cl_profile_notificationPreference);
        notificationPreference.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                startActivity(intent);
            }
        });
    }

    Button logOut;
    private void initLogOut(){
        logOut = (Button) RootView.findViewById(R.id.id_btn_logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LayoutInflater inflater = getLayoutInflater();
//                View window = inflater.inflate(R.layout.log_out_popupwindow, null);
//                PopupWindow popupWindow = new PopupWindow(view ,
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
//                popupWindow.setContentView(window);
//
//                popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL,0,500);
//                bgAlpha(0.618f);
//                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        bgAlpha(1.0f);
//                    }
//                });

                SharedPreferences.Editor  editor = preferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent();
                intent.setClass(getActivity(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void bgAlpha(float f){
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = f;
        getActivity().getWindow().setAttributes(layoutParams);
    }

}