package com.vendor.salon.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vendor.salon.fragment.AppointmentAllAppointmentFragment;
import com.vendor.salon.fragment.AppointmentRecentAppointmentFragment;
import com.vendor.salon.fragment.AppointmentTodayAppointmentFragment;
import com.vendor.salon.fragment.AppointmentUpcomingAppointmentFragment;

import java.util.HashMap;

public class AppointmentTabLayoutAdapter extends FragmentStateAdapter {

    String start_date= "",status="",end_date="",filter="";
    Context homeContext;
    private final HashMap<Integer, Fragment> mapFragment ;
    FragmentManager fragmentManager ;

    int pos;
    public AppointmentTabLayoutAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context homeContext) {
        super(fragmentManager, lifecycle);
        this.homeContext = homeContext;
        this.fragmentManager = fragmentManager ;
        this.mapFragment = new HashMap<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                AppointmentAllAppointmentFragment appointmentAllAppointmentFragment = new AppointmentAllAppointmentFragment(homeContext);
                mapFragment.put(position, appointmentAllAppointmentFragment);
                 return appointmentAllAppointmentFragment;
            case 1:
                AppointmentRecentAppointmentFragment appointmentRecentAppointmentFragment = new AppointmentRecentAppointmentFragment(homeContext);
                mapFragment.put(position, appointmentRecentAppointmentFragment);
                return appointmentRecentAppointmentFragment;
            case 2:
                AppointmentTodayAppointmentFragment appointmentTodayAppointmentFragment = new AppointmentTodayAppointmentFragment(homeContext);
                mapFragment.put(position, appointmentTodayAppointmentFragment);
                return appointmentTodayAppointmentFragment;
            case 3:
                AppointmentUpcomingAppointmentFragment appointmentUpcomingAppointmentFragment = new AppointmentUpcomingAppointmentFragment(homeContext);
                mapFragment.put(position, appointmentUpcomingAppointmentFragment);
                return appointmentUpcomingAppointmentFragment;
        }

        return null;
    }


    public Fragment getRegisteredFragment(int position) {
        return mapFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
