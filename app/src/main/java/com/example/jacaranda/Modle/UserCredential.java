package com.example.jacaranda.Modle;

import android.util.Log;

public class UserCredential {

    public String email;
    public String password;

    public UserCredential(){};

    public UserCredential(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginCredential{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
