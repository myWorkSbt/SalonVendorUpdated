package com.vendor.salon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vendor.salon.R;
import com.vendor.salon.data_Class.category_services.CategoriesItem;

import java.util.List;

public class AddServicesServiceSpinnerAdapter extends ArrayAdapter<CategoriesItem> {

    public AddServicesServiceSpinnerAdapter(Context context,
                                             List<CategoriesItem> spinnerList)
    {
        super(context, 0, spinnerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.services_spinner_item_lays, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.spinner_item_name);
        CategoriesItem currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }
        return convertView;
    }

}
