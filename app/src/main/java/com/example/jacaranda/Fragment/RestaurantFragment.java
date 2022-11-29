package com.example.jacaranda.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.jacaranda.Activity.AccountInformation;
import com.example.jacaranda.Activity.EnterUserName;
import com.example.jacaranda.Activity.RestaurantDetail;
import com.example.jacaranda.Adapter.BusinessPartnerAdapter;
import com.example.jacaranda.Adapter.RecentActivityAdapter;
import com.example.jacaranda.Modle.BusinessPartner;
import com.example.jacaranda.Modle.RecentActivity;
import com.example.jacaranda.MyView.NoScrollListView;
import com.example.jacaranda.R;
import com.example.jacaranda.Util.JsonToStringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class RestaurantFragment extends Fragment {
    View RootView;
    List<BusinessPartner> business = new ArrayList<>();

    public RestaurantFragment() {
        // Required empty public constructor
    }

    public static RestaurantFragment newInstance() {
        RestaurantFragment fragment = new RestaurantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(RootView == null){
            RootView = inflater.inflate(R.layout.home_fragment_restaurant, container, false);
            initBusinessActivity();
        }
        return RootView;
    }

    private void initBusinessActivity() {
        initBusinessActivityData();
        NoScrollListView listView = (NoScrollListView) RootView.findViewById(R.id.id_businessList);
        BusinessPartnerAdapter adapter = new BusinessPartnerAdapter(this.getActivity(),
                R.layout.home_business_part_listview, business);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RestaurantDetail.class);
                intent.putExtra("name", business.get(position).getName());
                intent.putExtra("address", business.get(position).getAddress());
                startActivity(intent);
            }
        });
    }

    private void initBusinessActivityData() {
        String strByJson = JsonToStringUtil.getStringByJson(getActivity(), R.raw.restaurants);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement activity : jsonArray) {
            BusinessPartner Activity = gson.fromJson(activity, BusinessPartner.class);
            business.add(Activity);
        }
    }
}