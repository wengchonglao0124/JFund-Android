package com.example.jacaranda.Modle;

import android.icu.text.DecimalFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RecentActivity {
    private String imageName;
    private String name;
    private String dateString;
    private String balance;
    private double amount;
    private int Color;
    private String Group;
    private int DateInt;

    public RecentActivity(String imageName, String name, String dateString, double amount){
        this.imageName = imageName;
        this.name = name;
        this.dateString = dateString;
        this.amount = amount;
    }

    public String getImageName(){ return imageName; }

    public String getName(){
        return name;
    }

    public String getBalance(){
        DecimalFormat df = new DecimalFormat(".00");
        if(amount >= 0){
            this.balance = "+" + "$" + df.format(amount);
            this.Color = 1;
        }else{
            this.balance = "-" + "$" + df.format(-1*amount);
            this.Color = -1;
        }

        return balance; }

    public int getColor(){ return Color; }

    public double getAmount(){ return amount; }

    public String setGroup(String date){ return this.Group = date; }

    public String getGroup(){ return Group; }

    public int getDateInt(){
        String[] newStr = this.dateString.split(" ");
        int day = Integer.valueOf(getDay());
        int month = 0;
        switch (getMonth()){
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
        }
        int year = Integer.valueOf(getYear());
        return year*10000 + month*100 + day ;
    }

    public String getDay(){
        String[] newStr = this.dateString.split(" ");
        return newStr[0];
    }

    public String getMonth(){
        String[] newStr = this.dateString.split(" ");
        return newStr[1];
    }

    public String getYear(){
        String[] newStr = this.dateString.split(" ");
        return newStr[2];
    }
}
