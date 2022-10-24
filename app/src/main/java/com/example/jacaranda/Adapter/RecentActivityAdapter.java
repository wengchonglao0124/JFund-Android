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

import androidx.annotation.RequiresApi;

import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.R;

import java.util.List;

public class RecentActivityAdapter extends ArrayAdapter {
    private int resourceId;

    public RecentActivityAdapter(Context context, int textViewResourceId, List<RecentActivity> object){
        super(context, textViewResourceId, object);
        resourceId = textViewResourceId;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View coverView, ViewGroup parent){
        RecentActivity activity = (RecentActivity) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView imageName = (ImageView) view.findViewById(R.id.id_activity_image);
        TextView name = (TextView) view.findViewById(R.id.id_activity_name);
        TextView dateString = (TextView) view.findViewById(R.id.id_activity_date);
        TextView amount = (TextView) view.findViewById(R.id.id_activity_amount);

        imageName.setImageResource(getImageId(activity.getImageName()));
        name.setText(activity.getName());
        dateString.setText(activity.getDay() + " " + activity.getMonth());
        amount.setText(activity.getBalance());

        int color = activity.getColor();
        if(color == 1){
            amount.setTextColor(Color.parseColor("#25c26e"));
        }else{
            amount.setTextColor(Color.parseColor("#1e1e20"));
        }
        return view;
    }

    private int getImageId(String imageName){
        switch (imageName){
            case "dribbble":
                return R.drawable.dribbble;
            case  "payoneer":
                return R.drawable.payoneer;
            case "uber":
                return R.drawable.uber;
            default:
                return R.drawable.rounded_rectangle;
        }
    }

}
