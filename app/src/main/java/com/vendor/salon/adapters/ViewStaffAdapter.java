package com.vendor.salon.adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.AddClientActivity;
import com.vendor.salon.activity.Staff;
import com.vendor.salon.activity.ViewStaff;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.databinding.ViewStaffLayBinding;

import java.util.List;

public class ViewStaffAdapter extends ArrayAdapter<DataItem> {
    private ViewStaffLayBinding staffLayBinding;
    Staff staff;
    String use_type;
    private int previousSelectedPosition = -1;
    private String blue_light = "#87CFD6";
    private String white_color = "#ffffff";

    public ViewStaffAdapter(Staff staff, List<DataItem> data, String type) {
        super(staff, 0, data);
        this.staff = staff;
        this.use_type = type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemVews = convertView ;
        if (convertView == null ) {
            itemVews = LayoutInflater.from(parent.getContext()).inflate( R.layout.view_staff_lay, parent, false);
        }

        DataItem staff_item_datas = getItem(position);

        ((AppCompatTextView) itemVews.findViewById(R.id.staff_name)).setText(staff_item_datas.getName() );
        AppCompatImageView staff_image =  (AppCompatImageView) itemVews.findViewById(R.id.image_staff) ;
        ConstraintLayout item_lays = itemVews.findViewById(R.id.item_lay_bg);
        CardView servicCardLays = itemVews.findViewById(R.id.services_item_lays);

        Glide.with(staff_image.getContext()).load(staff_item_datas.getOwnerImage()).into(staff_image);

        if (previousSelectedPosition == position) {
            item_lays.setBackgroundColor(Color.parseColor(blue_light));
        } else {
            item_lays.setBackgroundColor(Color.parseColor(white_color));
        }

        servicCardLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (use_type != null && use_type.equals("add_client")) {
//                    if (previousSelectedPosition == -1) {
//                        View previousSelectedView = parent.getChildAt(previousSelectedPosition);
//                        previousSelectedView.setSelected(false);
//                    }
//                    View currentSelectedItm = parent.getChildAt(position);
//                    currentSelectedItm.setSelected(true);
                    previousSelectedPosition = position;
//
                    Staff.selected_staff_position = position ;
                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(staff, ViewStaff.class);
                    intent.putExtra("staff_data", staff_item_datas);
                    staff.startActivity(intent);
                }
            }
        });
        return itemVews ;
    }

}
