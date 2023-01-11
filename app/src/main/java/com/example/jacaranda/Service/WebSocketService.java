package com.example.jacaranda.Service;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONException;
import com.example.jacaranda.JacarandaApplication;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 */
public class WebSocketService extends Service {
    private static final String BACKEND_URL = "ws://lycyy.cc:8080";
    private static final String TAG = "WebSocketService";

    private SharedPreferences preferences;

    public Application application;

    public Context context;

    private String data = "running";

    public class MyBinder extends Binder{
        public void setData(String data){
            WebSocketService.this.data = data;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private InitSocketThread thread;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mWebSocket != null) {
            mWebSocket.close(1000, null);
        }
        new InitSocketThread().start();
//        application= MyApp.getInstance();//这个是application，需要在功能清单里面的--android:name=".main.app.TzApplication"
//        context=MyApp.getInstance();
        context = JacarandaApplication.getAppContext();
        Log.e(TAG,"onCreate------------*************-------------");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        data = intent.getStringExtra("data");
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测


    private WebSocket mWebSocket;
    // 初始化socket

    private void initSocket() throws UnknownHostException, IOException {

        Log.i(TAG, "initSocket");
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        String token = preferences.getString("AccessToken", null);
        String userID = preferences.getString("userID", null);

        Log.i(TAG, token);
        Log.i(TAG, userID);

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
        Request request = new Request.Builder()
                .url(BACKEND_URL + "/websocket/{" + userID +  "}")
                .addHeader("token", token)
                .build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {//开启长连接成功的回调
                super.onOpen(webSocket, response);
                mWebSocket = webSocket;
                Log.i(TAG, "onOpen");


            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {//接收消息的回调
                super.onMessage(webSocket, text);
                //收到服务器端传过来的消息text
                Log.e(TAG, "接收消息的回调--------------"+text);


                try {
                    //这个是解析你的回调数据
//                    JSONObject jsonObject = JSON.parseObject(text);
                    Log.i(TAG, text);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                Log.i(TAG, "onMessage");
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.i(TAG, "onClosing");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Log.i(TAG, "onClosed");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {//长连接连接失败的回调
                super.onFailure(webSocket, t, response);
                Log.i(TAG, "Failure");
            }
        });
        client.dispatcher().executorService().shutdown();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }


    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isStop = false;

    private long sendTime = 0L;
    // 发送心跳包
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {

            if (isStop){
                mHandler.removeCallbacks(heartBeatRunnable);
                mWebSocket.cancel();//取消掉以前的长连接
                return;
            }

            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                if(mWebSocket!=null) {
                    boolean isSuccess = mWebSocket.send("这个是发给后台服务器的消息，根据要求自定义");//发送一个消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                    if (!isSuccess) {//长连接已断开，
                        Log.e(TAG, "发送心跳包-------------长连接已断开");
                        mHandler.removeCallbacks(heartBeatRunnable);
                        mWebSocket.cancel();//取消掉以前的长连接
                        new InitSocketThread().start();//创建一个新的连接
                    } else {//长连接处于连接状态---
                        Log.e(TAG, "发送心跳包-------------长连接处于连接状态");
                    }
                }

                sendTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
        }
    };

    //关闭长连接
    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
        Log.i(TAG, "onDestory");
        if (mWebSocket != null) {
            mWebSocket.close(1000, null);
        }
    }

}

