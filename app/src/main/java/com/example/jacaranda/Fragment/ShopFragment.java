package com.example.jacaranda.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jacaranda.R;

import java.util.ArrayList;
import java.util.List;


public class ShopFragment extends Fragment {
    View RootView;

    public ShopFragment() {
        // Required empty public constructor
    }

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
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
            RootView = inflater.inflate(R.layout.fragment_shop, container, false);
        }
        initAll();
        return RootView;
    }

    private void initAll() {
        initPager();
    }

    ViewPager viewPager;
    List<View> pictures;
    ImageView iv1,iv2,iv3;
    private void initPager() {
        iv1 = RootView.findViewById(R.id.id_iv_shopImage1);
        iv2 = RootView.findViewById(R.id.id_iv_shopImage2);
        iv3 = RootView.findViewById(R.id.id_iv_shopImage3);
        iv1.setSelected(true);

        pictures= new ArrayList<>();
        pictures.add(getView(R.drawable.shop_image1));
        pictures.add(getView(R.drawable.shop_image2));
        pictures.add(getView(R.drawable.shop_image1));

        viewPager = (ViewPager) RootView.findViewById(R.id.id_looper_pager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                if(pictures!=null){
                    return pictures.size();
                }
                else return 0;
            }
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = pictures.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switchPager (position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private View getView(int image) {
        ImageView iv = new ImageView(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(lp);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setImageResource(image);
        return iv;
    }

    public void switchPager(int position){
        if(position == 0){
            iv1.setSelected(true);
            iv2.setSelected(false);
            iv3.setSelected(false);
        }else if(position == 1){
            iv1.setSelected(false);
            iv2.setSelected(true);
            iv3.setSelected(false);
        }else if(position == 2){
            iv1.setSelected(false);
            iv2.setSelected(false);
            iv3.setSelected(true);
        }
    }
}