package com.example.jacaranda.Modle;

public class Activity {

    private String type;
    private String payUser;
    private String payUsername;
    private String receiveUser;
    private String receiveUsername;
    private String amount;
    private String dateString;
    private String receipt;

    public Activity(){}

    public Activity(String type, String payUser, String payUsername, String receiveUser, String receiveUsername, String amount, String date, String receipt) {
        this.type = type;
        this.payUser = payUser;
        this.payUsername = payUsername;
        this.receiveUser = receiveUser;
        this.receiveUsername = receiveUsername;
        this.amount = amount;
        this.dateString = date;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    @Override
    public String toString() {
        return "Activity{" +
                "type='" + type + '\'' +
                ", payUser='" + payUser + '\'' +
                ", payUsername='" + payUsername + '\'' +
                ", receiveUser='" + receiveUser + '\'' +
                ", receiveUsername='" + receiveUsername + '\'' +
                ", Amount='" + amount + '\'' +
                ", Date='" + dateString + '\'' +
                ", Receipt='" + receipt + '\'' +
                '}';
    }
}
