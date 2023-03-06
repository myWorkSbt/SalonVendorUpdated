package com.vendor.salon.adapters;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.getStaff.DataItem;
import com.vendor.salon.databinding.ItemAssignStaffBinding;
import com.vendor.salon.fragment.AppointmentRecentAppointmentFragment;

import java.util.List;

public class AssignStaffAdapter extends RecyclerView.Adapter<AssignStaffAdapter.ViewHolder> {

    List<DataItem> staffLists;
    private int selectstaFfposition = -1 ;

    public AssignStaffAdapter(List<DataItem> data) {
        this.staffLists = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAssignStaffBinding itemAssignStaffBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_assign_staff, parent, false);
        return new ViewHolder(itemAssignStaffBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         DataItem staff_ones = staffLists.get(position);
         if(staff_ones != null ) {
             holder.itemAssignStaffBinding.text.setText(staff_ones.getName());
             Glide.with(holder.itemAssignStaffBinding.image.getContext()).load(Uri.parse(staff_ones.getOwnerImage())).into(holder.itemAssignStaffBinding.image);
             if (staff_ones.isSelected()) {
                 holder.itemAssignStaffBinding.cardLayss.setBackgroundColor(Color.parseColor( "#87CFD6"));
             }
             else {
                 holder.itemAssignStaffBinding.cardLayss.setBackgroundColor(Color.parseColor( "#ffffff"));
             }
         }

         holder.itemAssignStaffBinding.servicesItemLays.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

//                notifyItemChanged(holder.getBindingAdapterPosition());
                     if(selectstaFfposition !=holder.getBindingAdapterPosition()) {
                         if(selectstaFfposition != -1 ) {
                             staffLists.get(selectstaFfposition).setSelected(false);
                             notifyItemChanged(selectstaFfposition);
                         }
                     RecentAppointmentRecyclerAdapter.selected_Staff_name = staff_ones.getName()+"";
                     selectstaFfposition = holder.getBindingAdapterPosition() ;
                     RecentAppointmentRecyclerAdapter.selected_staff_id = staff_ones.getId()+"";
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

        private final ItemAssignStaffBinding itemAssignStaffBinding;

        public ViewHolder(@NonNull ItemAssignStaffBinding itemAssignStaffBinding) {
            super(itemAssignStaffBinding.getRoot());
            this.itemAssignStaffBinding = itemAssignStaffBinding ;
        }
    }
}
