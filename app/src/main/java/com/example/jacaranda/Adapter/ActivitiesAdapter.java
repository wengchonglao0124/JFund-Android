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

public class ActivitiesAdapter extends ArrayAdapter<RecentActivity> {
    private int resource_parent;
    private int resource_child;
    List<RecentActivity> list;

    public ActivitiesAdapter(Context context, int resource_parent, int resource_child, List<RecentActivity> objects){
        super(context,resource_parent,resource_child,objects);
        this.resource_parent=resource_parent;
        this.resource_child=resource_child;
        this.list = objects;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View coverView, ViewGroup parent){
        View view;
        RecentActivity activity = (RecentActivity) getItem(position);
        if(position == 0){
            view=LayoutInflater.from(getContext()).inflate(this.resource_child, parent, false);
            ImageView imageName = (ImageView) view.findViewById(R.id.id_activity_image);
            TextView name = (TextView) view.findViewById(R.id.id_activity_name);
            TextView dateString = (TextView) view.findViewById(R.id.id_activity_date);
            TextView amount = (TextView) view.findViewById(R.id.id_activity_amount);
            TextView group = (TextView) view.findViewById(R.id.id_tv_group);
            imageName.setImageResource(getImageId(activity.getImageName()));
            name.setText(activity.getName());
            dateString.setText(activity.getDay() + " " + activity.getMonth());
            amount.setText(activity.getBalance());
            group.setText(activity.getMonth() + " " + activity.getYear());
            int color = activity.getColor();
            if(color == 1){
                amount.setTextColor(Color.parseColor("#25c26e"));
            }else{
                amount.setTextColor(Color.parseColor("#1e1e20"));
            }
            return view;
        }
        RecentActivity beforeActivity = (RecentActivity) getItem(position - 1);
        if(activity.getMonth().equals(beforeActivity.getMonth())){
            view=LayoutInflater.from(getContext()).inflate(this.resource_parent, parent, false);
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
        }else{
            view=LayoutInflater.from(getContext()).inflate(this.resource_child, parent, false);
            ImageView imageName = (ImageView) view.findViewById(R.id.id_activity_image);
            TextView name = (TextView) view.findViewById(R.id.id_activity_name);
            TextView dateString = (TextView) view.findViewById(R.id.id_activity_date);
            TextView amount = (TextView) view.findViewById(R.id.id_activity_amount);
            TextView group = (TextView) view.findViewById(R.id.id_tv_group);
            imageName.setImageResource(getImageId(activity.getImageName()));
            name.setText(activity.getName());
            dateString.setText(activity.getDay() + " " + activity.getMonth());
            amount.setText(activity.getBalance());
            group.setText(activity.getMonth() + " " + activity.getYear());
            int color = activity.getColor();
            if(color == 1){
                amount.setTextColor(Color.parseColor("#25c26e"));
            }else{
                amount.setTextColor(Color.parseColor("#1e1e20"));
            }
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
            case "apple":
                return R.drawable.apple;
            default:
                return R.drawable.rounded_rectangle;
        }
    }
}