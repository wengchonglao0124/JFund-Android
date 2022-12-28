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
        image.setImageResource(getImageId(getIntent().getStringExtra("image")));
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

}