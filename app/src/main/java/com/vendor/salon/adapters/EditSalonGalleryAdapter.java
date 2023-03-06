package com.vendor.salon.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.salon.R;
import com.vendor.salon.activity.EditSalon;
import com.vendor.salon.data_Class.getProfile.Galleries;
import com.vendor.salon.databinding.IteEditGalleryRecyclerBinding;

import java.util.List;

public class EditSalonGalleryAdapter  extends RecyclerView.Adapter<EditSalonGalleryAdapter.ViewHolder> {
    EditSalon editSalon;
    List<Galleries> galleriesList ;

    public EditSalonGalleryAdapter(EditSalon editSalons, List<Galleries> galleriesList) {
        this.editSalon = editSalons ;
        this.galleriesList =  galleriesList ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IteEditGalleryRecyclerBinding editGalleryRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ite_edit_gallery_recycler, parent, false);
        ;


        return  new ViewHolder(editGalleryRecyclerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Galleries gallery =  galleriesList.get( position);
        Glide.with(holder.editGalleryRecyclerBinding.galaryImg.getContext()).load(gallery.getDocPath()).into(holder.editGalleryRecyclerBinding.galaryImg);
    }

    @Override
    public int getItemCount() {
        return galleriesList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        private final IteEditGalleryRecyclerBinding editGalleryRecyclerBinding;

        public ViewHolder(@NonNull IteEditGalleryRecyclerBinding editGalleryRecyclerBinding) {
            super(editGalleryRecyclerBinding.getRoot() );
            this.editGalleryRecyclerBinding = editGalleryRecyclerBinding ;
        }
    }
}
