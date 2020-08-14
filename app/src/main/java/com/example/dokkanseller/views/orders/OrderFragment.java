package com.example.dokkanseller.views.orders;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewadapter;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void initializeViews(View view) {
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        viewadapter = new ViewPageAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewadapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void setListeners() {

    }
}
