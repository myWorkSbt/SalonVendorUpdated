package com.vendor.salon.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.vendor.salon.R;
import com.vendor.salon.activity.AddServices;

public class AddServicesServiceLocationAdapter extends ArrayAdapter<String> {
    AddServices addServices ;
    public AddServicesServiceLocationAdapter(Context context, String[] serviceLocationList ) {
        super(context,0 ,serviceLocationList);
        this.addServices = addServices ;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position , convertView , parent);
    }

    private View initview(int position, View convertView, ViewGroup parent) {
        if(convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.services_spinner_item_lays, parent , false );
        }
        androidx.appcompat.widget.AppCompatTextView serviceLocationTypeName = convertView.findViewById(R.id.spinner_item_name);
        serviceLocationTypeName.setText(getItem(position)+"");

        return convertView ;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position , convertView ,parent);
    }

}
