package com.vendor.salon.data_Class.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopServicesItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("vendor_id")
	private String vendorId;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("service_name")
	private String serviceName;

	@SerializedName("gender")
	private String gender;

	@SerializedName("mrp")
	private int mrp;

	@SerializedName("payable_amount")
	private int payableAmount;

	@SerializedName("discount_percent")
	private Object discountPercent;

	@SerializedName("image")
	private String image;

	@SerializedName("status")
	private int status;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("deleted_at")
	private Object deletedAt;
	private boolean isActive = true;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setVendorId(String vendorId){
		this.vendorId = vendorId;
	}

	public String getVendorId(){
		return vendorId;
	}

	public void setCategoryId(String categoryId){
		this.categoryId = categoryId;
	}

	public String getCategoryId(){
		return categoryId;
	}

	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}

	public String getServiceName(){
		return serviceName;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setMrp(int mrp){
		this.mrp = mrp;
	}

	public int getMrp(){
		return mrp;
	}

	public void setPayableAmount(int payableAmount){
		this.payableAmount = payableAmount;
	}

	public int getPayableAmount(){
		return payableAmount;
	}

	public void setDiscountPercent(Object discountPercent){
		this.discountPercent = discountPercent;
	}

	public Object getDiscountPercent(){
		return discountPercent;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	@Override
 	public String toString(){
		return 
			"TopServicesItem{" + 
			"id = '" + id + '\'' + 
			",vendor_id = '" + vendorId + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",service_name = '" + serviceName + '\'' + 
			",gender = '" + gender + '\'' + 
			",mrp = '" + mrp + '\'' + 
			",payable_amount = '" + payableAmount + '\'' + 
			",discount_percent = '" + discountPercent + '\'' + 
			",image = '" + image + '\'' + 
			",status = '" + status + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}