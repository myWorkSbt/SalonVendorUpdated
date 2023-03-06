package com.vendor.salon.data_Class.get_ManagePackageData;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("vendor_id")
	private String vendorId;

	@SerializedName("category_id")
	private Object categoryId;

	@SerializedName("service_id")
	private List<String> serviceId;

	@SerializedName("title")
	private String title;

	@SerializedName("about")
	private Object about;

	@SerializedName("image")
	private String image;

	@SerializedName("mrp")
	private String mrp;

	@SerializedName("offer_price")
	private String offerPrice;

	@SerializedName("disabled")
	private String disabled;

	@SerializedName("gender")
	private String gender;

	@SerializedName("is_doorstep")
	private String isDoorstep;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("services_listing")
	private List<ServicesListingItem> servicesListing;

	@SerializedName("category_name")
	private String categoryName;

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

	public void setCategoryId(Object categoryId){
		this.categoryId = categoryId;
	}

	public Object getCategoryId(){
		return categoryId;
	}

	public void setServiceId(List<String> serviceId){
		this.serviceId = serviceId;
	}

	public List<String> getServiceId(){
		return serviceId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setAbout(Object about){
		this.about = about;
	}

	public Object getAbout(){
		return about;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
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

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
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

	public void setServicesListing(List<ServicesListingItem> servicesListing){
		this.servicesListing = servicesListing;
	}

	public List<ServicesListingItem> getServicesListing(){
		return servicesListing;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryName(){
		return categoryName;
	}
}