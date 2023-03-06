package com.vendor.salon.data_Class.manage_inventory;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("vendor_id")
	private String vendorId;

	@SerializedName("inventory_category_id")
	private String inventoryCategoryId;

	@SerializedName("item")
	private String item;

	@SerializedName("qty")
	private String qty;

	@SerializedName("is_deleted")
	private String isDeleted;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

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

	public void setInventoryCategoryId(String inventoryCategoryId){
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public String getInventoryCategoryId(){
		return inventoryCategoryId;
	}

	public void setItem(String item){
		this.item = item;
	}

	public String getItem(){
		return item;
	}

	public void setQty(String qty){
		this.qty = qty;
	}

	public String getQty(){
		return qty;
	}

	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}

	public String getIsDeleted(){
		return isDeleted;
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
}