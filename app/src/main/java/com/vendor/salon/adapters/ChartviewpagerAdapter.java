package com.vendor.salon.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.vendor.salon.data_Class.home.HomeResponse;
import com.vendor.salon.fragment.DayFragment;
import com.vendor.salon.fragment.MonthFragment;
import com.vendor.salon.fragment.YearFragment;

public class ChartviewpagerAdapter extends FragmentStateAdapter {

    HomeResponse homeResponse;
    public ChartviewpagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, HomeResponse homeResponse) {
        super(fragmentManager,lifecycle);
        this.homeResponse = homeResponse ;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment= null;
//        switch (position) {
//            case 0:
//                fragment=  new DayFragment(homeResponse.getDailySales());
//                break;
//            case 1:
//                fragment= new MonthFragment(homeResponse.getMonthlySales());
//                break;
//            case 2:
//                fragment= new YearFragment(homeResponse.getYearlySales());
//                break;
//        }

                fragment= new MonthFragment(homeResponse.getDailySales());

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
