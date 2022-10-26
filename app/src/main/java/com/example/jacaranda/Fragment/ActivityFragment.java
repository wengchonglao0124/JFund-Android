package com.example.jacaranda.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

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
        }

        initActivities();
        initSearchBar();
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
        activityBackup = activityList;
    }

    private EditText text;
    private void initSearchBar() {
        text = (EditText) RootView.findViewById(R.id.id_et_Email);
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
                            startActivity(intent);
                        }
                    });
                }else{
                    activitySearched.clear();
                    for(RecentActivity activity: activityBackup){
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
}