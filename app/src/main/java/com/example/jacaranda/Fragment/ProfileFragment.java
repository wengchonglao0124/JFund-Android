package com.example.jacaranda.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.Activity.AccountInformation;
import com.example.jacaranda.Activity.AccountSecurity;
import com.example.jacaranda.Activity.SignInActivity;
import com.example.jacaranda.Activity.VerificationActivity;
import com.example.jacaranda.HintPage.SignUpSuccessfullyActivity;
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
        }
        initAll();
        return RootView;
    }

    private void initAll() {
        initName();
        initAccountInformation();
        initAccountSecurity();
        initLogout();
    }

    private void initLogout(){
        Button btn_logout;
        btn_logout = RootView.findViewById(R.id.id_btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void initName() {
        name = (TextView) RootView.findViewById(R.id.id_tv_profile_name);
        capital = (TextView) RootView.findViewById(R.id.id_tv_profile_capital);
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
}