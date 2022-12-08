package com.example.jacaranda;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class JacarandaApplication extends Application {

    private static final String BACKEND_URL = "https://xp.lycyy.cc";



    private String URL;

    @Override
    public void onCreate() {
        super.onCreate();
        setURL(BACKEND_URL);
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51LImNKAOiNy9BWzWmrjh6L2oHrjbNtPDxaBkavZ4yJnFqy6bDUutFcvZLUFfC5enOPGNDIuTLHISMUes2m5mc0yJ00H7FnRIcj"
        );
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

}