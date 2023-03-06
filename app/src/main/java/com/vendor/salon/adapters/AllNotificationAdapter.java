package com.vendor.salon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.salon.R;
import com.vendor.salon.activity.Notifications;
import com.vendor.salon.databinding.NotificationDayItemBinding;

import java.util.List;

public class AllNotificationAdapter extends RecyclerView.Adapter<AllNotificationAdapter.ViewHolder> {
    List<Notifications.AllNotificationDataClass> notificationsList;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public AllNotificationAdapter(Notifications notifications, List<Notifications.AllNotificationDataClass> notificationDataCla) {
        this.notificationsList = notificationDataCla;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationDayItemBinding notificationDayItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_day_item, parent, false);

        return new ViewHolder(notificationDayItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications.AllNotificationDataClass allNotificationDataClass = notificationsList.get(position);
        // as LinearLayout with vertical orientation
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                holder.notificationDayItemBinding.
                        dateNotificationDetails
                        .getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);


        holder.notificationDayItemBinding
                .dateNotificationDetails
                .setLayoutManager(layoutManager);
        if(allNotificationDataClass== null  ) {
            holder.notificationDayItemBinding.todayLays.setVisibility(View.GONE);
        }
        else {
            holder.notificationDayItemBinding.todayHeading.setText(allNotificationDataClass.getDate());
            layoutManager
                    .setInitialPrefetchItemCount(
                            allNotificationDataClass
                                    .getNotificationList()
                                    .size());


            NotificationAdapter notificationChildAdapter
                    = new NotificationAdapter(
                    allNotificationDataClass
                            .getNotificationList());


            holder.notificationDayItemBinding
                    .dateNotificationDetails
                    .setAdapter(notificationChildAdapter);
            holder.notificationDayItemBinding
                    .dateNotificationDetails
                    .setRecycledViewPool(viewPool);
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final NotificationDayItemBinding notificationDayItemBinding;

        public ViewHolder(NotificationDayItemBinding notificationDayItemBinding) {
            super(notificationDayItemBinding.getRoot());
            this.notificationDayItemBinding = notificationDayItemBinding ;
        }
    }

}
