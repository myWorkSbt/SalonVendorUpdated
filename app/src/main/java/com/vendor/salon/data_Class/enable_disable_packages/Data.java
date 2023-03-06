package com.vendor.salon.data_Class.enable_disable_packages;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("vendor_id")
	private String vendorId;

	@SerializedName("title")
	private String title;

	@SerializedName("about")
	private String about;

	@SerializedName("image")
	private Object image;

	@SerializedName("mrp")
	private String mrp;

	@SerializedName("offer_price")
	private String offerPrice;

	@SerializedName("disabled")
	private String disabled;

	@SerializedName("is_doorstep")
	private String isDoorstep;

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

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setAbout(String about){
		this.about = about;
	}

	public String getAbout(){
		return about;
	}

	public void setImage(Object image){
		this.image = image;
	}

	public Object getImage(){
		return image;
	}

	public void setMrp(String mrp){
		this.mrp = mrp;
	}

	public String getMrp(){
		return mrp;
	}

	public void setOfferPrice(String offerPrice){
		this.offerPrice = offerPrice;
	}

	public String getOfferPrice(){
		return offerPrice;
	}

	public void setDisabled(String disabled){
		this.disabled = disabled;
	}

	public String getDisabled(){
		return disabled;
	}

	public void setIsDoorstep(String isDoorstep){
		this.isDoorstep = isDoorstep;
	}

	public String getIsDoorstep(){
		return isDoorstep;
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