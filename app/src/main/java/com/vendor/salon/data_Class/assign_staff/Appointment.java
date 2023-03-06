package com.vendor.salon.data_Class.assign_staff;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Appointment implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("vendor_id")
	private String vendorId;

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("end_time")
	private String endTime;

	@SerializedName("service_site")
	private String serviceSite;

	@SerializedName("services_name")
	private String servicesName;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("transaction_id")
	private Object transactionId;

	@SerializedName("no_of_people")
	private String noOfPeople;

	@SerializedName("client_gender")
	private String clientGender;

	@SerializedName("status")
	private String status;

	@SerializedName("specialist")
	private String specialist;

	@SerializedName("specialist_id")
	private Object specialistId;

	@SerializedName("amount")
	private String amount;

	@SerializedName("reminder")
	private String reminder;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("deleted_at")
	private Object deletedAt;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setVendorId(String vendorId){
		this.vendorId = vendorId;
	}

	public String getVendorId(){
		return vendorId;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setServiceSite(String serviceSite){
		this.serviceSite = serviceSite;
	}

	public String getServiceSite(){
		return serviceSite;
	}

	public void setServicesName(String servicesName){
		this.servicesName = servicesName;
	}

	public String getServicesName(){
		return servicesName;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setTransactionId(Object transactionId){
		this.transactionId = transactionId;
	}

	public Object getTransactionId(){
		return transactionId;
	}

	public void setNoOfPeople(String noOfPeople){
		this.noOfPeople = noOfPeople;
	}

	public String getNoOfPeople(){
		return noOfPeople;
	}

	public void setClientGender(String clientGender){
		this.clientGender = clientGender;
	}

	public String getClientGender(){
		return clientGender;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setSpecialist(String specialist){
		this.specialist = specialist;
	}

	public String getSpecialist(){
		return specialist;
	}

	public void setSpecialistId(Object specialistId){
		this.specialistId = specialistId;
	}

	public Object getSpecialistId(){
		return specialistId;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setReminder(String reminder){
		this.reminder = reminder;
	}

	public String getReminder(){
		return reminder;
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
}