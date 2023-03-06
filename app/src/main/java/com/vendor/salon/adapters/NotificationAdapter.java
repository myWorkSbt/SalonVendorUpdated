package com.vendor.salon.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.activity.Notifications;
import com.vendor.salon.databinding.NotificationItemBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<Notifications.NotificationItem> notificationList;
    public NotificationAdapter(List<Notifications.NotificationItem> notificationList) {
        this.notificationList =  notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         NotificationItemBinding notificationItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_item, parent, false);

         return new ViewHolder(notificationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.notificationItemBinding.itemNotificationImage.setImageResource( notificationList.get(position).getNotification_image());
        holder.notificationItemBinding.tvNotificationTitle.setText("Notification  "+ (position+1)+ " .");
        holder.notificationItemBinding.tvOtificationTextDetails.setText(notificationList.get(position).getNotification_text());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        private final NotificationItemBinding notificationItemBinding;

        public ViewHolder(NotificationItemBinding notificationItemBinding) {
            super(notificationItemBinding.getRoot());
            this.notificationItemBinding = notificationItemBinding;
        }
    }
}
