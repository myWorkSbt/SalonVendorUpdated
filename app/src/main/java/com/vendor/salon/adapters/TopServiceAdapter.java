package com.vendor.salon.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.home.TopServicesItem;
import com.vendor.salon.databinding.TopServicesItemBinding;

import java.util.List;

public class TopServiceAdapter extends RecyclerView.Adapter<TopServiceAdapter.ViewHolder>{

    private Context context;
    private List<TopServicesItem> serviceList;
    public TopServiceAdapter (Context context, List<TopServicesItem> serviceList) {
        this.context = context;
        this.serviceList= serviceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TopServicesItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from( parent.getContext()),
                R.layout.top_services_item, parent, false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopServicesItem topService = serviceList.get(position);
        Uri profile = Uri.parse(topService.getImage());
        Uri text = Uri.parse(topService.getServiceName());

        Glide.with(context).load(profile).placeholder(R.drawable.image).into(holder.servicesItemBinding.image);
        holder.servicesItemBinding.text.setText(text.toString());
        int item_position = position;

    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TopServicesItemBinding servicesItemBinding;

        public ViewHolder(TopServicesItemBinding binding) {
            super(binding.getRoot());
            this.servicesItemBinding = binding;
        }
    }
}
