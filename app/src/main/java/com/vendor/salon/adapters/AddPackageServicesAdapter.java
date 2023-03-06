package com.vendor.salon.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.category_services.CategoriesItem;
import com.vendor.salon.data_Class.vendor_sub_catgories.DataItem;

import java.util.List;

public class AddPackageServicesAdapter extends ArrayAdapter<DataItem> {


    public AddPackageServicesAdapter(Context context,
                                    List<DataItem> spinnerList) {
        super(context, 0, spinnerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @SuppressLint("ResourceAsColor")
    private View initView(int position, View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.services_spinner_item_lays, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.spinner_item_name);
        DataItem currentItem = getItem(position);
        if (currentItem.isSelected()) {
            textViewName.setTextColor (R.color.blue_light_popular);
        }
        else {
            textViewName.setTextColor (Color.parseColor("#737373"));
        }

        if (currentItem != null) {
            textViewName.setText(currentItem.getServiceName());
        }
        return convertView;
    }

}

