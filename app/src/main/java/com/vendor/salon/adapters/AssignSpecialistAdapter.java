package com.vendor.salon.adapters;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.AddClientActivity;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.databinding.ItemAssignSpecialistBinding;

import java.util.ArrayList;
import java.util.List;

public class AssignSpecialistAdapter extends RecyclerView.Adapter<AssignSpecialistAdapter.ViewHolder> {

    List<DataItem> staffLists;
    private int selectstaFfposition = -1;
    String usetype;

    public AssignSpecialistAdapter(List<DataItem> data) {
        this.staffLists = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAssignSpecialistBinding itemAssignSpecialistBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_assign_specialist, parent, false);
        return new ViewHolder(itemAssignSpecialistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem staff_ones = staffLists.get(position);
        if (staff_ones != null) {
            holder.itemAssignSpecialistBinding.text.setText(staff_ones.getName());
            if (staff_ones.getDesignation() != null) {
                holder.itemAssignSpecialistBinding.specialistDesignation.setText(staff_ones.getDesignation());
            } else {
                holder.itemAssignSpecialistBinding.specialistDesignation.setText("--");
            }
            if (staff_ones.isSelected()) {
                holder.itemAssignSpecialistBinding.cardLayss.setBackgroundColor(Color.parseColor("#87CFD6"));
                if (selectstaFfposition != position) {
                    if (selectstaFfposition != -1) {

                        staffLists.get(selectstaFfposition).setSelected(false);
                        notifyItemChanged(selectstaFfposition);
                    }
                }
                selectstaFfposition = position;
                AddClientActivity.selected_specialists_id = String.valueOf(staff_ones.getId());
            } else {
                holder.itemAssignSpecialistBinding.cardLayss.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            if (position == 0) {
                holder.itemAssignSpecialistBinding.image.setImageResource(R.drawable.anyone);
                AddClientActivity.selected_specialists_id = "0";
                holder.itemAssignSpecialistBinding.specialistDesignation.setVisibility(View.GONE);
//                String anyone_background_colors = "#0EDFF4";
//                holder.itemAssignSpecialistBinding.cardLayss.setBackgroundColor(Color.parseColor(anyone_background_colors));
            } else {
                Glide.with(holder.itemAssignSpecialistBinding.image.getContext()).load(Uri.parse(staff_ones.getOwnerImage())).into(holder.itemAssignSpecialistBinding.image);
            }

        }

        holder.itemAssignSpecialistBinding.servicesItemLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setSelectedItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return staffLists.size();
    }

    public void refreshLists(List<DataItem> staff_list) {
        this.staffLists = staff_list;
        notifyDataSetChanged();
    }

    public void setSelectedItem(int selected_staff_position) {
        if (selectstaFfposition != -1) {
            staffLists.get(selectstaFfposition).setSelected(false);
            notifyItemChanged(selectstaFfposition);
        }
        this.selectstaFfposition = selected_staff_position;
        if (selected_staff_position == 0 ) {
            AddClientActivity.selected_specialists_id = "";
        } else {
            AddClientActivity.selected_specialists_id = staffLists.get(selected_staff_position).getId() + "";
        }
        staffLists.get(selected_staff_position).setSelected(true);
        notifyItemChanged(selectstaFfposition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAssignSpecialistBinding itemAssignSpecialistBinding;

        public ViewHolder(@NonNull ItemAssignSpecialistBinding itemAssignSpecialistBinding) {
            super(itemAssignSpecialistBinding.getRoot());
            this.itemAssignSpecialistBinding = itemAssignSpecialistBinding;
        }
    }
}
