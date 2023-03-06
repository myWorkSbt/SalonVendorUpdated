package com.vendor.salon.data_Class.vendor_sub_catgories;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DataItem implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("vendor_id")
    private String vendorId;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("service_id")
    private String serviceId;

    @SerializedName("service_name")
    private String serviceName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("mrp")
    private int mrp;

    @SerializedName("offer_price")
    private int offerPrice;

    @SerializedName("discount_percent")
    private Object discountPercent;

    @SerializedName("image")
    private String image;

    @SerializedName("status")
    private int status;

    @SerializedName("is_doorstep")
    private String isDoorstep;

    @SerializedName("disabled")
    private String disabled;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    private boolean selected = false;

    @SerializedName("deleted_at")
    private Object deletedAt;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getMrp() {
        return mrp;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setDiscountPercent(Object discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Object getDiscountPercent() {
        return discountPercent;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setIsDoorstep(String isDoorstep) {
        this.isDoorstep = isDoorstep;
    }

    public String getIsDoorstep() {
        return isDoorstep;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }
}