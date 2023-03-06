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

import java.util.List;

public class AssignSpecialistAdapter extends RecyclerView.Adapter<AssignSpecialistAdapter.ViewHolder> {

    List<DataItem> staffLists;
    private int selectstaFfposition = -1 ;

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
        if(staff_ones != null ) {
            holder.itemAssignSpecialistBinding.text.setText(staff_ones.getName());
            holder.itemAssignSpecialistBinding.specialistDesignation.setText(staff_ones.getDesignation());
            Glide.with(holder.itemAssignSpecialistBinding.image.getContext()).load(Uri.parse(staff_ones.getOwnerImage())).into(holder.itemAssignSpecialistBinding.image);
            if (staff_ones.isSelected()) {
                holder.itemAssignSpecialistBinding.cardLayss.setBackgroundColor(Color.parseColor( "#87CFD6"));
            }
            else {
                holder.itemAssignSpecialistBinding.cardLayss.setBackgroundColor(Color.parseColor( "#ffffff"));
            }
        }

        holder.itemAssignSpecialistBinding.servicesItemLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                notifyItemChanged(holder.getBindingAdapterPosition());
                if(selectstaFfposition !=holder.getBindingAdapterPosition()) {
                    if(selectstaFfposition != -1 ) {
                        staffLists.get(selectstaFfposition).setSelected(false);
                        notifyItemChanged(selectstaFfposition);
                    }

                    selectstaFfposition = holder.getBindingAdapterPosition() ;
                    AddClientActivity.selected_specialists_id = staff_ones.getId()+"";
                    staff_ones.setSelected(true);
                    notifyItemChanged(selectstaFfposition);
//            recentAppointmentsBinding.btnRecentAccept.setVisibility(View.GONE);
//            recentAppointmentsBinding.btnRecentReject.setVisibility(View.GONE);
//            getStaffData(holder.getBindingAdapterPosition());
                }
                else {
                    notifyItemChanged(selectstaFfposition);
                    selectstaFfposition = -1 ;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return staffLists.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {

        private final ItemAssignSpecialistBinding itemAssignSpecialistBinding;

        public ViewHolder(@NonNull ItemAssignSpecialistBinding itemAssignSpecialistBinding) {
            super(itemAssignSpecialistBinding.getRoot());
            this.itemAssignSpecialistBinding = itemAssignSpecialistBinding ;
        }
    }
}
