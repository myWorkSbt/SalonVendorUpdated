package com.vendor.salon.data_Class.appointmentsfilter;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AppointmentsItem implements Serializable {

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
	private String specialistId;

	@SerializedName("amount")
	private String amount;

	@SerializedName("reminder")
	private String reminder;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("doorstep_status")
	private String doorstep_status;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("user_phone")
	private String userPhone;

	@SerializedName("user_lat")
	private String user_lat;

	public String getUser_lat() {
		return user_lat;
	}

	public void setUser_lat(String user_lat) {
		this.user_lat = user_lat;
	}

	public String getUser_lng() {
		return user_lng;
	}

	public void setUser_lng(String user_lng) {
		this.user_lng = user_lng;
	}

	@SerializedName("user_lng")
	private String user_lng;

	@SerializedName("user_location")
	private Object userLocation;

	@SerializedName("distance")
	private String distance;

	@SerializedName("user_image")
	private String userImage;

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

	public void setSpecialistId(String specialistId){
		this.specialistId = specialistId;
	}

	public String getSpecialistId(){
		return specialistId;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getDoorstep_status() {
		return doorstep_status;
	}

	public void setDoorstep_status(String doorstep_status) {
		this.doorstep_status = doorstep_status;
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

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserPhone(String userPhone){
		this.userPhone = userPhone;
	}

	public String getUserPhone(){
		return userPhone;
	}

	public void setUserLocation(Object userLocation){
		this.userLocation = userLocation;
	}

	public Object getUserLocation(){
		return userLocation;
	}

	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getDistance(){
		return distance;
	}

	public void setUserImage(String userImage){
		this.userImage = userImage;
	}

	public String getUserImage(){
		return userImage;
	}
}