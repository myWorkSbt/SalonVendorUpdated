package com.vendor.salon.data_Class.appointment_services_edit_confirm;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DataItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("appointment_id")
	private String appointmentId;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("service_id")
	private String serviceId;

	@SerializedName("service_name")
	private String serviceName;

	@SerializedName("qty")
	private String qty;

	@SerializedName("amount")
	private String amount;

	@SerializedName("offer_price")
	private String offerPrice;

	@SerializedName("gender")
	private String gender;

	@SerializedName("is_doorstep")
	private String isDoorstep;

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

	public void setAppointmentId(String appointmentId){
		this.appointmentId = appointmentId;
	}

	public String getAppointmentId(){
		return appointmentId;
	}

	public void setCategoryId(String categoryId){
		this.categoryId = categoryId;
	}

	public String getCategoryId(){
		return categoryId;
	}

	public void setServiceId(String serviceId){
		this.serviceId = serviceId;
	}

	public String getServiceId(){
		return serviceId;
	}

	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}

	public String getServiceName(){
		return serviceName;
	}

	public void setQty(String qty){
		this.qty = qty;
	}

	public String getQty(){
		return qty;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setOfferPrice(String offerPrice){
		this.offerPrice = offerPrice;
	}

	public String getOfferPrice(){
		return offerPrice;
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