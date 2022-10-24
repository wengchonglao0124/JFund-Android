package com.example.jacaranda.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jacaranda.Adapter.BusinessPartnerAdapter;
import com.example.jacaranda.Modle.BusinessPartner;
import com.example.jacaranda.MyView.NoScrollListView;
import com.example.jacaranda.R;
import com.example.jacaranda.Util.JsonToStringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class TourismFragment extends Fragment {
    View RootView;
    List<BusinessPartner> business = new ArrayList<>();

    public TourismFragment() {
        // Required empty public constructor
    }

    public static TourismFragment newInstance() {
        TourismFragment fragment = new TourismFragment();
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
            RootView = inflater.inflate(R.layout.home_fragment_tourism, container, false);
        }
        initBusinessActivity();
        return RootView;
    }

    private void initBusinessActivity() {
        initBusinessActivityData();
        NoScrollListView listView = (NoScrollListView) RootView.findViewById(R.id.id_businessList);
        BusinessPartnerAdapter adapter = new BusinessPartnerAdapter(this.getActivity(),
                R.layout.home_business_part_listview, business);
        listView.setAdapter(adapter);
    }

    private void initBusinessActivityData() {
        String strByJson = JsonToStringUtil.getStringByJson(getActivity(), R.raw.tourisms);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement activity : jsonArray) {
            BusinessPartner Activity = gson.fromJson(activity, BusinessPartner.class);
            business.add(Activity);
        }
    }

}