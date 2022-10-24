package com.example.jacaranda.MyView;

import android.text.LoginFilter;

public class MyInputFilter extends LoginFilter.UsernameFilterGeneric {
    private String mAllowedDigits;

    public MyInputFilter( String digits ) {
        mAllowedDigits = digits;
    }

    @Override
    public boolean isAllowed(char c) {
        if (mAllowedDigits.indexOf(c) != -1) {
            return true;
        }
        return false;
    }
}