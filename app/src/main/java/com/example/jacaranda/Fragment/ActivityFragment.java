package com.example.jacaranda.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.example.jacaranda.Activity.ActivitiesDetail;
import com.example.jacaranda.Adapter.ActivitiesAdapter;
import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.MyView.NoScrollListView;
import com.example.jacaranda.R;
import com.example.jacaranda.Util.JsonToStringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityFragment extends Fragment {
    View RootView;
    List<RecentActivity> activityList = new ArrayList<>();
    List<RecentActivity> activitySearched = new ArrayList<>();
    List<RecentActivity> activityBackup = new ArrayList<>();
    LinearLayout Activities;
    ActivitiesAdapter adapter;
    NoScrollListView listView;

    public ActivityFragment() {
        // Required empty public constructor
    }

    public static ActivityFragment newInstance() {
        ActivityFragment fragment = new ActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(RootView == null){
            RootView = inflater.inflate(R.layout.fragment_activity, container, false);
            initActivities();
            initSearchBar();
            initFilter();
        }
        return RootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initActivities() {
        initActivitiesData();
        Activities = RootView.findViewById(R.id.id_ll_activities);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.all_activities, null);
        Activities.addView(view);
        listView = (NoScrollListView) RootView.findViewById(R.id.id_activities);
        listView.setTextFilterEnabled(true);
        adapter = new ActivitiesAdapter(this.getActivity(),
                R.layout.home_activity_part_listview, R.layout.all_activities_listview, activityList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                intent.putExtra("image", activityList.get(position).getImageName());
                intent.putExtra("name", activityList.get(position).getName());
                intent.putExtra("amount", activityList.get(position).getBalance());
                intent.putExtra("extra", activityList.get(position).getExtra());
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initActivitiesData() {
        String strByJson = JsonToStringUtil.getStringByJson(getActivity(), R.raw.activities);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement activity : jsonArray) {
            RecentActivity Activity = gson.fromJson(activity, RecentActivity.class);
            activityList.add(Activity);
        }
        activityList.sort(Comparator.comparing(RecentActivity::getDateInt));
        Collections.reverse(activityList);
        activityBackup.addAll(activityList);
    }

    private EditText text;
    private void initSearchBar() {
        activityTemp1.addAll(activityBackup);
        text = (EditText) RootView.findViewById(R.id.id_et_search);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString() == ""){
                    ActivitiesAdapter adapterNew = new ActivitiesAdapter(getActivity(),
                            R.layout.home_activity_part_listview, R.layout.all_activities_listview, activityBackup);
                    listView.setAdapter(adapterNew);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                            intent.putExtra("image", activityList.get(position).getImageName());
                            intent.putExtra("name", activityList.get(position).getName());
                            intent.putExtra("amount", activityList.get(position).getBalance());
                            intent.putExtra("extra", activityList.get(position).getExtra());
                            startActivity(intent);
                        }
                    });
                }else{
                    activitySearched.clear();
                    for(RecentActivity activity: activityTemp1){
                        if(activity.getName().contains(s.toString())){
                            activitySearched.add(activity);
                        }
                    }
                    ActivitiesAdapter adapterNew = new ActivitiesAdapter(getActivity(),
                            R.layout.home_activity_part_listview, R.layout.all_activities_listview, activitySearched);
                    listView.setAdapter(adapterNew);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                            intent.putExtra("image", activitySearched.get(position).getImageName());
                            intent.putExtra("name", activitySearched.get(position).getName());
                            intent.putExtra("amount", activitySearched.get(position).getBalance());
                            intent.putExtra("extra", activityList.get(position).getExtra());
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    PopupWindow popupWindow;
    Button filter,back;
    ConstraintLayout cl;
    private void initFilter() {
        filter = RootView.findViewById(R.id.id_btn_activity_filter);
        cl = RootView.findViewById(R.id.id_cl_activity);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View window = inflater.inflate(R.layout.filter_popupwindow, null);
                popupWindow = new PopupWindow(view ,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        cl.getHeight(), true);
                popupWindow.setContentView(window);

                initPopupWindow();

                popupWindow.showAtLocation(view, Gravity.TOP,0,0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                });
            }
        });
    }


    ImageView btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    ConstraintLayout filter1,filter2,filter3,filter4,filter5,filter6,filter7;
    Boolean flag1=true,flag2=false,flag3=false,flag4=true,flag5=false,flag6=false,flag7=false;
    private void initPopupWindow(){
        back = popupWindow.getContentView().findViewById(R.id.id_btn_filter_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        btn1 = popupWindow.getContentView().findViewById(R.id.id_btn_filter1);
        btn2 = popupWindow.getContentView().findViewById(R.id.id_btn_filter2);
        btn3 = popupWindow.getContentView().findViewById(R.id.id_btn_filter3);
        btn4 = popupWindow.getContentView().findViewById(R.id.id_btn_filter4);
        btn5 = popupWindow.getContentView().findViewById(R.id.id_btn_filter5);
        btn6 = popupWindow.getContentView().findViewById(R.id.id_btn_filter6);
        btn7 = popupWindow.getContentView().findViewById(R.id.id_btn_filter7);
        if(flag1){
            btn1.setSelected(true);
        }
        if(flag2){
            btn2.setSelected(true);
        }
        if(flag3){
            btn3.setSelected(true);
        }
        if(flag4){
            btn4.setSelected(true);
        }
        if(flag5){
            btn5.setSelected(true);
        }
        if(flag6){
            btn6.setSelected(true);
        }
        if(flag7){
            btn7.setSelected(true);
        }

        filter1 = popupWindow.getContentView().findViewById(R.id.id_cl_filter1);
        filter1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag1=true;flag2=false;flag3=false;
                btn1.setSelected(true);
                btn2.setSelected(false);
                btn3.setSelected(false);
                filterData();
            }
        });
        filter2 = popupWindow.getContentView().findViewById(R.id.id_cl_filter2);
        filter2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag1=false;flag2=true;flag3=false;
                btn1.setSelected(false);
                btn2.setSelected(true);
                btn3.setSelected(false);
                filterData();
            }
        });
        filter3 = popupWindow.getContentView().findViewById(R.id.id_cl_filter3);
        filter3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag1=false;flag2=false;flag3=true;
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(true);
                filterData();
            }
        });

        filter4 = popupWindow.getContentView().findViewById(R.id.id_cl_filter4);
        filter4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=true;flag5=false;flag6=false;flag7=false;
                btn4.setSelected(true);
                btn5.setSelected(false);
                btn6.setSelected(false);
                btn7.setSelected(false);
                filterData();
            }
        });
        filter5 = popupWindow.getContentView().findViewById(R.id.id_cl_filter5);
        filter5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=false;flag5=true;flag6=false;flag7=false;
                btn4.setSelected(false);
                btn5.setSelected(true);
                btn6.setSelected(false);
                btn7.setSelected(false);
                filterData();
            }
        });
        filter6 = popupWindow.getContentView().findViewById(R.id.id_cl_filter6);
        filter6.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=false;flag5=false;flag6=true;flag7=false;
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(true);
                btn7.setSelected(false);
                filterData();
            }
        });
        filter7 = popupWindow.getContentView().findViewById(R.id.id_cl_filter7);
        filter7.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                flag4=false;flag5=false;flag6=false;flag7=true;
                btn4.setSelected(false);
                btn5.setSelected(false);
                btn6.setSelected(false);
                btn7.setSelected(true);
                filterData();
            }
        });
    }

    List<RecentActivity> activityTemp = new ArrayList<>();
    List<RecentActivity> activityTemp1 = new ArrayList<>();
    int year,month,day,dateInt;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterData(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateInt = year*360 + month*30 + day;

        activityTemp.clear();
        activityTemp1.clear();
        if(flag1){
            activityTemp.addAll(activityBackup);
        }
        if(flag2){
            for(RecentActivity activity: activityBackup){
                if(activity.getBalance().contains("+")){
                    activityTemp.add(activity);
                }
            }
        }
        if(flag3){
            for(RecentActivity activity: activityBackup){
                if(activity.getBalance().contains("-")){
                    activityTemp.add(activity);
                }
            }
        }
        //----------------------------------
        if(flag4){
            activityTemp1.addAll(activityTemp);
        }
        if(flag5){
            for(RecentActivity activity: activityTemp){
                if(dateInt-activity.getDayInt()<=30){
                    activityTemp1.add(activity);
                }
            }
        }
        if(flag6){
            for(RecentActivity activity: activityTemp){
                if(dateInt-activity.getDayInt()<=60){
                    activityTemp1.add(activity);
                }
            }
        }
        if(flag7){
            for(RecentActivity activity: activityTemp){
                if(dateInt-activity.getDayInt()<=90){
                    activityTemp1.add(activity);
                }
            }
        }
        ActivitiesAdapter adapterNew = new ActivitiesAdapter(getActivity(),
                R.layout.home_activity_part_listview, R.layout.all_activities_listview, activityTemp1);
        listView.setAdapter(adapterNew);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivitiesDetail.class);
                intent.putExtra("image", activityTemp1.get(position).getImageName());
                intent.putExtra("name", activityTemp1.get(position).getName());
                intent.putExtra("amount", activityTemp1.get(position).getBalance());
                intent.putExtra("extra", activityTemp1.get(position).getExtra());
                startActivity(intent);
            }
        });
    }
}