package com.example.jacaranda.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.jacaranda.Modle.BusinessPartner;
import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.R;

import java.util.List;

public class BusinessPartnerAdapter  extends ArrayAdapter {
    private int resourceId;

    public BusinessPartnerAdapter(Context context, int textViewResourceId, List<BusinessPartner> object){
        super(context, textViewResourceId, object);
        resourceId = textViewResourceId;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View coverView, ViewGroup parent) {
        BusinessPartner business = (BusinessPartner) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView image = (ImageView) view.findViewById(R.id.id_business_image);
        TextView name = (TextView) view.findViewById(R.id.id_business_name);
        TextView address = (TextView) view.findViewById(R.id.id_business_address);
        TextView distance = (TextView) view.findViewById(R.id.id_business_distance);

        image.setImageResource(getImageId(business.getImage()));
        name.setText(business.getName());
        address.setText(business.getAddress());
        distance.setText(business.getDistance());
        return view;
    }

    private int getImageId(String imageName){
        switch (imageName){
            case "restaurantImage":
                return R.drawable.restaurant_image;
            case "beautyImage":
                return R.drawable.beauty_image;
            case "tourismImage":
                return R.drawable.tourism_image;
            default:
                return R.drawable.rounded_rectangle;
        }
    }
}
