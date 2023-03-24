package com.vendor.salon.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vendor.salon.R;
import com.vendor.salon.adapters.AllNotificationAdapter;
import com.vendor.salon.databinding.ActivityNotificationsBinding;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.NetworkChangeListener;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    private ActivityNotificationsBinding notificationsBinding;
    ArrayList<AllNotificationDataClass> notificationDataClassList;
    private boolean isApiCalled = false ;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsBinding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);
        getNotificationData();
        if(notificationDataClassList== null || notificationDataClassList.size()< 1 ) {
            notificationsBinding.showNoDataText.setVisibility(View.VISIBLE);
        }


        notificationsBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotificationData();
                notificationsBinding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        notificationsBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getNotificationData() {
        if (!isApiCalled) {
            isApiCalled = true ;
            List<NotificationItem> notificationList = new ArrayList<>();
            notificationList.add(new NotificationItem(R.drawable.no_image_rectangle, "As there is no \"Mark directory as\" menu in Android Studio, I found out that such settings like including and excluding directories are stored in a \".iml\" file. Name"));
            notificationList.add(new NotificationItem(R.drawable.image, "id Studio, I found "));
            notificationList.add(new NotificationItem(R.drawable.no_image, "sdkfsjd asd sd; sd sd;k ;sdf sd a;a fd  droid Studio, I found out that such settings "));

            notificationDataClassList = new ArrayList<>();
            notificationDataClassList.add(new AllNotificationDataClass("today", notificationList));
            notificationDataClassList.add(new AllNotificationDataClass("Yesterday", notificationList));
            AllNotificationDataClass notificationData = new AllNotificationDataClass("23-11-2023", notificationList);
            notificationDataClassList.add(notificationData);
            notificationDataClassList.add(new AllNotificationDataClass("27-11-20223", notificationList));
            notificationDataClassList.add(new AllNotificationDataClass("27-07-2024", notificationList));
            notificationsBinding.everyDayNotificationRecycler.setAdapter(new AllNotificationAdapter(this, notificationDataClassList));
            isApiCalled = false ;
        }
    }

    public class AllNotificationDataClass {
        String date;
        List<NotificationItem> notificationList;

        public AllNotificationDataClass(String date, List<NotificationItem> notificationList) {
            this.date = date;
            this.notificationList = notificationList;
        }

        public String getDate() {
            return date;
        }

        public List<NotificationItem> getNotificationList() {
            return notificationList;
        }

    }

    public class NotificationItem {
        int notification_image;
        String notification_text;

        public NotificationItem(int notification_image, String notification_text) {
            this.notification_image = notification_image;
            this.notification_text = notification_text;
        }

        public int getNotification_image() {
            return notification_image;
        }

        public String getNotification_text() {
            return notification_text;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Notifications.this, Home.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener , intentFilter );
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}