package com.example.jacaranda.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

public class JsonToStringUtil {
    public static String getStringByJson(Context context, int fileId) {
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().openRawResource(fileId);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static JSONObject parseResponse(ResponseBody responseBody) throws IOException, JSONException {
        if (responseBody != null) {
            return new JSONObject(responseBody.string());
        }
        return new JSONObject();
    }

}
