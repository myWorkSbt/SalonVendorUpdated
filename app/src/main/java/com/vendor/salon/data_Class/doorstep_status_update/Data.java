package com.vendor.salon.data_Class.doorstep_status_update;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("vendor_id")
	private String vendorId;

	@SerializedName("appointment_date")
	private String appointmentDate;

	@SerializedName("slote_time")
	private Object sloteTime;

	@SerializedName("service_site")
	private String serviceSite;

	@SerializedName("services_name")
	private String servicesName;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("transaction_id")
	private Object transactionId;

	@SerializedName("payment_method")
	private Object paymentMethod;

	@SerializedName("no_of_people")
	private Object noOfPeople;

	@SerializedName("booking_time")
	private Object bookingTime;

	@SerializedName("client_gender")
	private Object clientGender;

	@SerializedName("status")
	private String status;

	@SerializedName("doorstep_status")
	private String doorstepStatus;

	@SerializedName("specialist")
	private String specialist;

	@SerializedName("specialist_id")
	private String specialistId;

	@SerializedName("amount")
	private String amount;

	@SerializedName("reminder")
	private String reminder;

	@SerializedName("address_id")
	private Object addressId;

	@SerializedName("reason")
	private Object reason;

	@SerializedName("start_code")
	private String startCode;

	@SerializedName("end_code")
	private String endCode;

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

	public void setAppointmentDate(String appointmentDate){
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentDate(){
		return appointmentDate;
	}

	public void setSloteTime(Object sloteTime){
		this.sloteTime = sloteTime;
	}

	public Object getSloteTime(){
		return sloteTime;
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

	public void setPaymentMethod(Object paymentMethod){
		this.paymentMethod = paymentMethod;
	}

	public Object getPaymentMethod(){
		return paymentMethod;
	}

	public void setNoOfPeople(Object noOfPeople){
		this.noOfPeople = noOfPeople;
	}

	public Object getNoOfPeople(){
		return noOfPeople;
	}

	public void setBookingTime(Object bookingTime){
		this.bookingTime = bookingTime;
	}

	public Object getBookingTime(){
		return bookingTime;
	}

	public void setClientGender(Object clientGender){
		this.clientGender = clientGender;
	}

	public Object getClientGender(){
		return clientGender;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setDoorstepStatus(String doorstepStatus){
		this.doorstepStatus = doorstepStatus;
	}

	public String getDoorstepStatus(){
		return doorstepStatus;
	}

	public void setSpecialist(String specialist){
		this.specialist = specialist;
	}

	public String getSpecialist(){
		return specialist;
	}

	public void setSpecialistId(String specialistId){
		this.specialistId = specialistId;
	}

	public String getSpecialistId(){
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

	public void setAddressId(Object addressId){
		this.addressId = addressId;
	}

	public Object getAddressId(){
		return addressId;
	}

	public void setReason(Object reason){
		this.reason = reason;
	}

	public Object getReason(){
		return reason;
	}

	public void setStartCode(String startCode){
		this.startCode = startCode;
	}

	public String getStartCode(){
		return startCode;
	}

	public void setEndCode(String endCode){
		this.endCode = endCode;
	}

	public String getEndCode(){
		return endCode;
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