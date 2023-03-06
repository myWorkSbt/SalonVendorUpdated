package com.vendor.salon.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.Staff;
import com.vendor.salon.activity.ViewStaff;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.databinding.ViewStaffLayBinding;

import java.util.List;

public class ViewStaffAdapter extends ArrayAdapter<DataItem> {
    private ViewStaffLayBinding staffLayBinding;
    Staff staff;
    public ViewStaffAdapter(Staff staff, List<DataItem> data) {
        super(staff,0,data);
        this.staff = staff ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        staffLayBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_staff_lay, parent, false);
        DataItem staff_item_datas = getItem(position);
        staffLayBinding.staffName.setText(staff_item_datas.getName()+"");

        Glide.with(staffLayBinding.imageStaff.getContext()).load(staff_item_datas.getOwnerImage()).into(staffLayBinding.imageStaff);

        staffLayBinding.servicesItemLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( staff, ViewStaff.class);
                intent.putExtra( "id", staff_item_datas.getId()+"");
                staff.startActivity(intent);
            }
        });
        return staffLayBinding.getRoot();
    }

}
