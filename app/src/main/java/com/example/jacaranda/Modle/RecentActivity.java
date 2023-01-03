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
    private String extra;

    private String receipt;
    private String dateTime;
    private String type;
    private String profilePic;

    public RecentActivity(String imageName, String name, String dateString, double amount,String extra){
        this.imageName = imageName;
        this.name = name;
        this.dateString = dateString;
        this.amount = amount;
        this.extra = extra;
    }

    public RecentActivity() {

    }

    public RecentActivity(String imageName, String name, double amount, String extra, String dateTime) {
        this.imageName = imageName;
        this.name = name;
        this.amount = amount;
        this.extra = extra;
        this.dateTime = dateTime;
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

    public String getExtra(){
        return extra;
    }

    public int getDayInt(){
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
        return year*360 + month*30 + day ;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        if (this.type.equals("topUp")){
            Double extraAmount = Math.floor(amount/10);
            if (!(extraAmount < 10)){
                this.extra = String.valueOf(extraAmount);
            }
        }
    }

    public void setColor(int color) {
        Color = color;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "RecentActivity{" +
                "receipt='" + receipt + '\'' +
                ", imageName='" + imageName + '\'' +
                ", dateString='" + dateString + '\'' +
                ", amount=" + amount +
                ", extra='" + extra + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
