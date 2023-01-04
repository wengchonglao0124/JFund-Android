package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jacaranda.R;

public class ActivitiesDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_detail);
        initAll();
    }

    private void initAll() {
        initBack();
        initImage();
        initName();
        initAmount();
        initExtra();
        initTime();
        initReceipt();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_activitiesDetail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ImageView image;
    private void initImage() {
        image = (ImageView) findViewById(R.id.id_iv_activitiesDetail_image);
//        image.setImageResource(getImageId(getIntent().getStringExtra("image")));
        image.setImageResource(getNameColor(getIntent().getStringExtra("name"),
                getIntent().getStringExtra("image")));
    }

    TextView name;
    private void initName() {
        name = (TextView) findViewById(R.id.id_iv_activitiesDetail_name);
        name.setText("To "+getIntent().getStringExtra("name"));
    }

    TextView amount;
    private void initAmount() {
        amount = (TextView) findViewById(R.id.id_iv_activitiesDetail_amount);
        amount.setText(getIntent().getStringExtra("amount"));
    }

    TextView extra;
    private void initExtra() {
        extra = (TextView) findViewById(R.id.id_extra_money);
        String money = getIntent().getStringExtra("extra");
        if(money==null){
            extra.setVisibility(View.GONE);
        }else{
            extra.setText(getIntent().getStringExtra("extra"));
        }
    }

    TextView time;
    private void initTime(){
        time = (TextView) findViewById(R.id.id_tv_activityDetail_time);
        time.setText(getIntent().getStringExtra("time"));
    }

    TextView receipt;
    private void initReceipt(){
        receipt = (TextView) findViewById(R.id.id_tv_activityDetail_receipt);
        receipt.setText(getIntent().getStringExtra("receipt"));
    }

    private int getImageId(String imageName){
        switch (imageName){
            case "dribbble":
                return R.drawable.dribbble;
            case  "payoneer":
                return R.drawable.payoneer;
            case "uber":
                return R.drawable.uber;
            case "apple":
                return R.drawable.apple;
            default:
                return R.drawable.rounded_rectangle;
        }
    }

    TextView letter;
    private int getNameColor(String name, String color){
        letter = (TextView) findViewById(R.id.id_activity_detail_letter);
        letter.setText(name.substring(0,1).toUpperCase());
        switch (color){
            case "#cdb4db":
                return R.drawable.circle_case1;
            case  "#ffafcc":
                return R.drawable.circle_case2;
            case "#ffcaca":
                return R.drawable.circle_case3;
            case "#fb6f92":
                return R.drawable.circle_case4;
            case "#449dd1":
                return R.drawable.circle_case5;
            case "#bccef8":
                return R.drawable.circle_case6;
            case "#a2d2ff":
                return R.drawable.circle_case7;
            case "#74c69d":
                return R.drawable.circle_case8;
            default:
                letter.setText("");
                return R.drawable.payoneer;
        }
    }

}