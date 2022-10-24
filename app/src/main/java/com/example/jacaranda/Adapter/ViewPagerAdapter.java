package com.example.jacaranda.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    List<Fragment> FragmentList = new ArrayList<>();
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager,
                            @NonNull Lifecycle lifecycle, List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        FragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return FragmentList.size();
    }
}
