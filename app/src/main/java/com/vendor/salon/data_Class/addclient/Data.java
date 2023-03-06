package com.vendor.salon.data_Class.addclient;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("user_id")
	private int userId;

	@SerializedName("vendor_id")
	private int vendorId;

	@SerializedName("appointment_date")
	private String appointmentDate;

	@SerializedName("service_site")
	private String serviceSite;

	@SerializedName("booking_time")
	private String bookingTime;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("services_name")
	private String [] servicesName;

	@SerializedName("amount")
	private String amount;

	@SerializedName("address_id")
	private String addressId;

	@SerializedName("specialist_id")
	private String specialistId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setVendorId(int vendorId){
		this.vendorId = vendorId;
	}

	public int getVendorId(){
		return vendorId;
	}

	public void setAppointmentDate(String appointmentDate){
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentDate(){
		return appointmentDate;
	}

	public void setServiceSite(String serviceSite){
		this.serviceSite = serviceSite;
	}

	public String getServiceSite(){
		return serviceSite;
	}

	public void setBookingTime(String bookingTime){
		this.bookingTime = bookingTime;
	}

	public String getBookingTime(){
		return bookingTime;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setServicesName(String[]  servicesName){
		this.servicesName = servicesName;
	}

	public String[] getServicesName(){
		return servicesName;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setAddressId(String addressId){
		this.addressId = addressId;
	}

	public String getAddressId(){
		return addressId;
	}

	public void setSpecialistId(String specialistId){
		this.specialistId = specialistId;
	}

	public String getSpecialistId(){
		return specialistId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}