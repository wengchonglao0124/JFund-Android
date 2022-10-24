package com.example.jacaranda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jacaranda.Activity.SignInActivity;
import com.example.jacaranda.Adapter.ViewPagerAdapter;
import com.example.jacaranda.Fragment.ActivityFragment;
import com.example.jacaranda.Fragment.HomeFragment;
import com.example.jacaranda.Fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager2 ViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPage();
        initBottomNavigation();
    }


    private void initViewPage() {
        ViewPager = findViewById(R.id.id_ViewPager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(ActivityFragment.newInstance());
        fragments.add(ProfileFragment.newInstance());
        ViewPagerAdapter PagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                getLifecycle(), fragments);
        ViewPager.setAdapter(PagerAdapter);
        ViewPager.setUserInputEnabled(false);
        ViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SwitchPager(position);
            }
        });

    }

    private LinearLayout ll_Home,ll_Activity,ll_Profile;
    private ImageView iv_Home,iv_Activity,iv_Profile,iv_Current;
    private TextView tv_Home,tv_Activity,tv_Profile,tv_Current;
    private void initBottomNavigation(){
        ll_Home = findViewById(R.id.id_ll_home);
        ll_Activity = findViewById(R.id.id_ll_activity);
        ll_Profile = findViewById(R.id.id_ll_profile);
        ll_Home.setOnClickListener(this);
        ll_Activity.setOnClickListener(this);
        ll_Profile.setOnClickListener(this);

        iv_Home = findViewById(R.id.id_iv_home);
        iv_Activity = findViewById(R.id.id_iv_activity);
        iv_Profile = findViewById(R.id.id_iv_profile);

        tv_Home = findViewById(R.id.id_tv_home);
        tv_Activity = findViewById(R.id.id_tv_activity);
        tv_Profile = findViewById(R.id.id_tv_profile);

        iv_Home.setSelected(true);
        iv_Current = iv_Home;
        tv_Home.setSelected(true);
        tv_Current = tv_Home;
    }

    @Override
    public void onClick(View view) {
        SwitchPager(view.getId());
    }

    private void SwitchPager(int position) {
        iv_Current.setSelected(false);
        tv_Current.setSelected(false);
        if(position == R.id.id_ll_home|| position == 0){
            ViewPager.setCurrentItem(0);
            iv_Home.setSelected(true);
            iv_Current = iv_Home;
            tv_Home.setSelected(true);
            tv_Current = tv_Home;
        }else if(position == R.id.id_ll_activity || position == 1){
            ViewPager.setCurrentItem(1);
            iv_Activity.setSelected(true);
            iv_Current = iv_Activity;
            tv_Activity.setSelected(true);
            tv_Current = tv_Activity;
        }else if(position == R.id.id_ll_profile || position == 2){
            ViewPager.setCurrentItem(2);
            iv_Profile.setSelected(true);
            iv_Current = iv_Profile;
            tv_Profile.setSelected(true);
            tv_Current = tv_Profile;
        }else if(position == R.id.id_btn_showAll){
            ViewPager.setCurrentItem(1);
            iv_Activity.setSelected(true);
            iv_Current = iv_Activity;
        }
    }
}