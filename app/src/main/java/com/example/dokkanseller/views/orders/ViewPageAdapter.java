package com.example.dokkanseller.views.orders;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentPagerAdapter {


    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public ViewPageAdapter(FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }



    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            AllOrdersListFragment allOrdersListFragment = new AllOrdersListFragment();
            return allOrdersListFragment;

        } else if (position == 1) {
            ProcessFragment processFragment = new ProcessFragment();
            return processFragment;


        } else if (position == 2) {
            DoneFragment doneFragment = new DoneFragment();
            return  doneFragment;
        }

        else if (position == 3) {

            DeliveredFragment deliveredFragment = new DeliveredFragment();
            return deliveredFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "New Orders";
            case 1:
                return "Process";
            case 2:
                return "Done";
            case 3:
                return "Delivered";
        }
        return null;
    }


}

