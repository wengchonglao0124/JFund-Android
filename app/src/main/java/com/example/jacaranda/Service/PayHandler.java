package com.example.jacaranda.Service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.jacaranda.Activity.PayActivity;

import org.json.JSONException;

import java.lang.ref.WeakReference;

public class PayHandler extends Handler {

    WeakReference<PayActivity> payActivityWeakReference;

    public PayHandler(PayActivity payActivity){
        payActivityWeakReference =
                new WeakReference<PayActivity>(payActivity);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        PayActivity payActivity = payActivityWeakReference.get();
        if (payActivity == null){
            return;
        }

        try {
            payActivity.pay(msg.getData().getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
