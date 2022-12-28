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
import com.example.jacaranda.MyView.RotateTextView;
import com.example.jacaranda.R;

import java.util.List;
import java.util.Locale;

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

    View view;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View coverView, ViewGroup parent){
        RecentActivity activity = (RecentActivity) getItem(position);
        if(position == 0){
            view=LayoutInflater.from(getContext()).inflate(this.resource_child, parent, false);
            ImageView imageName = (ImageView) view.findViewById(R.id.id_activity_image);
            TextView name = (TextView) view.findViewById(R.id.id_activity_name);
            TextView dateString = (TextView) view.findViewById(R.id.id_activity_date);
            TextView amount = (TextView) view.findViewById(R.id.id_activity_amount);
            TextView group = (TextView) view.findViewById(R.id.id_tv_group);
            RotateTextView extra = (RotateTextView) view.findViewById(R.id.id_extra_money);
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
            if(activity.getExtra() == null){
                extra.setVisibility(View.GONE);
            }else {
                extra.setText(activity.getExtra());
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
            RotateTextView extra = (RotateTextView) view.findViewById(R.id.id_extra_money);
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
            if(activity.getExtra() == null){
                extra.setVisibility(View.GONE);
            }else {
                extra.setText(activity.getExtra());
            }
        }else{
            view=LayoutInflater.from(getContext()).inflate(this.resource_child, parent, false);
            ImageView imageName = (ImageView) view.findViewById(R.id.id_activity_image);
            TextView name = (TextView) view.findViewById(R.id.id_activity_name);
            TextView dateString = (TextView) view.findViewById(R.id.id_activity_date);
            TextView amount = (TextView) view.findViewById(R.id.id_activity_amount);
            TextView group = (TextView) view.findViewById(R.id.id_tv_group);
            RotateTextView extra = (RotateTextView) view.findViewById(R.id.id_extra_money);
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
            if(activity.getExtra() == null){
                extra.setVisibility(View.GONE);
            }else {
                extra.setText(activity.getExtra());
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

    TextView letter;
    private int getNameImage(String name){
        letter = view.findViewById(R.id.id_activity_letter);
        letter.setText(name.substring(0,1).toUpperCase());
        switch (name.substring(0,1).toUpperCase()){
            case "A":case "I": case "Q":case "Y":
                return R.drawable.circle_case1;
            case  "B":case "J":case "R":case "Z":
                return R.drawable.circle_case2;
            case "C":case "K":case "S":
                return R.drawable.circle_case3;
            case "D":case "L":case "T":
                return R.drawable.circle_case4;
            case "E":case "M":case "U":
                return R.drawable.circle_case5;
            case "F":case "N":case "V":
                return R.drawable.circle_case6;
            case "G":case "O":case "W":
                return R.drawable.circle_case7;
            case "H":case "P":case "X":
                return R.drawable.circle_case8;
            default:
                return R.drawable.rounded_rectangle;
        }
    }
}