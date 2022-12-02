package com.example.jacaranda.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacaranda.Activity.ActivitiesDetail;
import com.example.jacaranda.Activity.RestaurantDetail;
import com.example.jacaranda.Activity.TopUpBalance;
import com.example.jacaranda.Adapter.RecentActivityAdapter;
import com.example.jacaranda.Adapter.FragmentPagerAdapter;
import com.example.jacaranda.JacarandaApplication;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment {
    View RootView;
    List<RecentActivity> activityList = new ArrayList<>();
    private ViewPager2 ViewPager;
    private TabLayout tabLayout;

    private final String TAG = "HomeFragment";
    private static final String BACKEND_URL = "https://xp.lycyy.cc";
    private JacarandaApplication app;
    private SharedPreferences preferences;

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
        preferences = requireActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        app = (JacarandaApplication) getActivity().getApplication();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(RootView == null){
            RootView = inflater.inflate(R.layout.fragment_home, container, false);
            initEvent();
            initRecentActivity();
            initBusinessPartner();
            initClick();

            updateUserID(preferences.getString("userID", "0000000000000000"));
            getBalance();
        }
        return RootView;
    }

    View event;
    private void initEvent() {
        event = RootView.findViewById(R.id.id_include_event);
        event.setVisibility(View.GONE);
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

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
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
        // initTabLayout();
    }

    FragmentPagerAdapter adapter;
    private void initViewPager() {
        ViewPager = RootView.findViewById(R.id.id_NestedViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RestaurantFragment.newInstance());
        // fragments.add(BeautyFragment.newInstance());
        // fragments.add(TourismFragment.newInstance());
        adapter = new FragmentPagerAdapter(getChildFragmentManager(),
                getLifecycle(), fragments);
        if(ViewPager.getAdapter() == null) {
            ViewPager.setAdapter(adapter);
        }
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
        addBalance = RootView.findViewById(R.id.id_btn_top_up_balance);
        addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TopUpBalance.class);
                startActivity(i);
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


    private JSONObject parseResponse(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new JSONObject(responseBody.string());
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error parsing response", e);
            }
        }

        return new JSONObject();
    }

    private void getBalance(){

        //setup RequestBody
        final RequestBody requestBody = RequestBody.create(
                "",
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BACKEND_URL + "/balanceOf")
                .post(requestBody)
                .addHeader("token", preferences.getString("AccessToken", null))
                .build();


        new OkHttpClient()
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    }

                    @Override
                    public void onResponse(
                            @NonNull Call call,
                            @NonNull Response response
                    ) throws IOException {
                        if (!response.isSuccessful()) {
                        } else {
                            final JSONObject responseJson = parseResponse(response.body());
                            Log.i(TAG, responseJson.toString());

                            String code, message, data;
                            code = responseJson.optString("code");
                            message = responseJson.optString("msg");
                            data = responseJson.optString("data");
                            data = data.replace("\\\"", "'");

                            Log.i(TAG, data);

                            String balance = "UNKNOWN";

                            if (code.equals("200")){
                                try {
                                    balance = new JSONObject(data).optString("balanceof");
                                    Log.i(TAG, balance);
                                    if (balance.equals("null")){
                                        balance = "0.00";
                                    }
                                    updateBalance(balance);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Log.i(TAG, message);
                                updateBalance(balance);
                            }

                        }
                    }
                });

    }

    private void updateUserID(String userID){
        TextView userId = RootView.findViewById(R.id.id_balance_userid);
        String userIDString = "";
        char[] characters = userID.toCharArray();
        for (int i=0; i<userID.length(); i++){
            if ((i%4) == 0){
                userIDString += " ";
            }
            userIDString += characters[i];
        }
        userId.setText(userIDString);
    }

    private void updateBalance(String balance){
        getActivity().runOnUiThread(() -> {
            TextView tx_balance = RootView.findViewById(R.id.id_balance_balance);
            Log.i(TAG, balance);
            tx_balance.setText(balance);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getBalance();
    }
}