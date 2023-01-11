package com.example.jacaranda.Util;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jacaranda.R;

public class profileUtil {

    public static int getNameColor(@NonNull String color){
        switch (color){
            case "#cdb4db":
                return R.drawable.circle_case1;
            case  "#ffafcc":
                return R.drawable.circle_case2;
            case "#ffcaca":
                return R.drawable.circle_case3;
            case "#fb6f92":
                return R.drawable.circle_case4;
            case "#449dd1":
                return R.drawable.circle_case5;
            case "#bccef8":
                return R.drawable.circle_case6;
            case "#a2d2ff":
                return R.drawable.circle_case7;
            case "#74c69d":
                return R.drawable.circle_case8;
            default:
                return R.drawable.rounded_rectangle;
        }
    }

    public static String uidToUidString(String uid){
        String uidString = "";
        char[] characters = uid.toCharArray();
        for (int i=0; i<uid.length(); i++){
            if ((i%4) == 0){
                uidString += " ";
            }
            uidString += characters[i];
        }
        return uidString;
    }
}
