package com.example.jacaranda.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jacaranda.R;

public class RestaurantDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        initAll();
    }

    private void initAll() {
        initBack();
        initName();
        initAddress();
    }

    Button back;
    private void initBack() {
        back = (Button) findViewById(R.id.id_btn_restaurantDetail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    TextView name;
    private void initName() {
        name = (TextView) findViewById(R.id.id_tv_restaurantDetail_name);
        name.setText(getIntent().getStringExtra("name"));
    }

    TextView address;
    private void initAddress() {
        address = (TextView) findViewById(R.id.id_tv_restaurantDetail_address);
        address.setText(getIntent().getStringExtra("address"));
    }
}