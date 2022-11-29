package com.example.jacaranda;

import android.app.Application;

public class JacarandaApplication extends Application {

    private static final String BACKEND_URL = "https://xp.lycyy.cc";



    private String URL;

    @Override
    public void onCreate() {
        super.onCreate();
        setURL(BACKEND_URL);
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

}