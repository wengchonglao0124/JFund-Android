package com.example.jacaranda.Modle;

public class Activity {

    private String type;
    private String payUser;
    private String payUsername;
    private String payColor;
    private String receiveUser;
    private String receiveUsername;
    private String receiveColor;
    private double amount;
    private String dateString;
    private String receipt;

    public Activity(){}

    public Activity(String type, String payUser, String payUsername, String payColor, String receiveUser, String receiveUsername, String receiveColor, double amount, String dateString, String receipt) {
        this.type = type;
        this.payUser = payUser;
        this.payUsername = payUsername;
        this.payColor = payColor;
        this.receiveUser = receiveUser;
        this.receiveUsername = receiveUsername;
        this.receiveColor = receiveColor;
        this.amount = amount;
        this.dateString = dateString;
        this.receipt = receipt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    public String getPayUsername() {
        return payUsername;
    }

    public void setPayUsername(String payUsername) {
        this.payUsername = payUsername;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getReceiveUsername() {
        return receiveUsername;
    }

    public void setReceiveUsername(String receiveUsername) {
        this.receiveUsername = receiveUsername;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getPayColor() {
        return payColor;
    }

    public void setPayColor(String payColor) {
        this.payColor = payColor;
    }

    public String getReceiveColor() {
        return receiveColor;
    }

    public void setReceiveColor(String receiveColor) {
        this.receiveColor = receiveColor;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "type='" + type + '\'' +
                ", payUser='" + payUser + '\'' +
                ", payUsername='" + payUsername + '\'' +
                ", payColor='" + payColor + '\'' +
                ", receiveUser='" + receiveUser + '\'' +
                ", receiveUsername='" + receiveUsername + '\'' +
                ", receiveColor='" + receiveColor + '\'' +
                ", amount='" + amount + '\'' +
                ", dateString='" + dateString + '\'' +
                ", receipt='" + receipt + '\'' +
                '}';
    }
}
