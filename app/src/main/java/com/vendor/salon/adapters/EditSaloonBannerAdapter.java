package com.vendor.salon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.getProfile.BannerItem;
import com.vendor.salon.databinding.ItemImageBannerBinding;
import com.vendor.salon.data_Class.getProfile.BannerItem;

import java.util.List;

public class EditSaloonBannerAdapter extends RecyclerView.Adapter<EditSaloonBannerAdapter.ViewHolder> {

    private Context context;
    private List<BannerItem> banner;

    public EditSaloonBannerAdapter(Context context, List<BannerItem> bannerList) {
        this.context= context;
        this.banner = bannerList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBannerBinding editSaloonBannerRecyclerviewItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_image_banner, parent, false);
        return new ViewHolder(editSaloonBannerRecyclerviewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BannerItem bannerItem = banner.get(position);
        Glide.with(context).load(bannerItem.getDocPath()).into(holder.editSaloonBannerRecyclerviewItemBinding.bannerItem);
//        holder.editSaloonBannerRecyclerviewItemBinding.bannerItem.setImageResource(bannerItem.getDocPath());
    }

    @Override
    public int getItemCount() {
        return banner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemImageBannerBinding editSaloonBannerRecyclerviewItemBinding;

        public ViewHolder(ItemImageBannerBinding editSaloonBannerRecyclerviewItemBinding) {
            super(editSaloonBannerRecyclerviewItemBinding.getRoot());
            this.editSaloonBannerRecyclerviewItemBinding = editSaloonBannerRecyclerviewItemBinding ;
        }
    }
}
