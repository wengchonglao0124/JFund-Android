package com.example.jacaranda.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jacaranda.Adapter.RecentActivityAdapter;
import com.example.jacaranda.Adapter.FragmentPagerAdapter;
import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.MyView.NoScrollListView;
import com.example.jacaranda.Activity.NotificationActivity;
import com.example.jacaranda.Activity.PayActivity;
import com.example.jacaranda.Activity.TransferActivity;
import com.example.jacaranda.Util.JsonToStringUtil;
import com.example.jacaranda.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    View RootView;
    List<RecentActivity> activityList = new ArrayList<>();
    private ViewPager2 ViewPager;
    private TabLayout tabLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
            RootView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        initRecentActivity();
        initBusinessPartner();
        initClick();
        return RootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initRecentActivity() {
        initRecentActivityData();
        LinearLayout recentActivity = RootView.findViewById(R.id.id_ll_recentActivity);
        LayoutInflater inflater = getLayoutInflater();
        if (activityList.size() == 0){
            View view = inflater.inflate(R.layout.home_activitiy_part_case1, null);
            recentActivity.addView(view);
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }else if (activityList.size() > 0 && activityList.size() <= 3){
            View listview = inflater.inflate(R.layout.home_activity_part_case2, null);
            recentActivity.addView(listview);
            NoScrollListView listView = (NoScrollListView) RootView.findViewById(R.id.id_listView);
            RecentActivityAdapter adapter = new RecentActivityAdapter(this.getActivity(),
                    R.layout.home_activity_part_listview, activityList);
            listView.setAdapter(adapter);
        }else if(activityList.size() > 3){
            List<RecentActivity> currentList = new ArrayList<>();
            currentList.add(activityList.get(0));
            currentList.add(activityList.get(1));
            currentList.add(activityList.get(2));
            View listview = inflater.inflate(R.layout.home_activity_part_case3,null);
            recentActivity.addView(listview);
            NoScrollListView listView = (NoScrollListView) RootView.findViewById(R.id.id_listView);
            RecentActivityAdapter adapter = new RecentActivityAdapter(this.getActivity(),
                    R.layout.home_activity_part_listview, currentList);
            listView.setAdapter(adapter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initRecentActivityData() {
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
    }

    private void initBusinessPartner(){
        initViewPager();
        initTabLayout();
    }

    private void initViewPager() {
        ViewPager = RootView.findViewById(R.id.id_NestedViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RestaurantFragment.newInstance());
        fragments.add(BeautyFragment.newInstance());
        fragments.add(TourismFragment.newInstance());
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager(),
                getLifecycle(), fragments);
        ViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(" Restaurant", "Beauty", "Tourism"));
        tabLayout = RootView.findViewById(R.id.id_tl_tab);
        new TabLayoutMediator(tabLayout, ViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(title.get(position));
            }
        }).attach();
        ViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void initClick() {
        clickNotification();
        addBalance();
        clickPay();
        clickTransfer();
    }

    private Button notification;
    public void clickNotification(){
        notification = RootView.findViewById(R.id.id_btn_notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NotificationActivity.class);
                startActivity(i);
            }
        });
    }

    private Button addBalance;
    public void addBalance(){
        addBalance = RootView.findViewById(R.id.id_btn_add);
        addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private ImageView pay;
    public void clickPay(){
        pay = RootView.findViewById(R.id.id_iv_pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PayActivity.class);
                startActivity(i);
            }
        });
    }

    private ImageView transfer;
    public void clickTransfer(){
        transfer = RootView.findViewById(R.id.id_iv_transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TransferActivity.class);
                startActivity(i);
            }
        });
    }
}