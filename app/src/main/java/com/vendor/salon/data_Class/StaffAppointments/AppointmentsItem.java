package com.vendor.salon.data_Class.StaffAppointments;

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
	private int status;

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

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("phone")
	private String phone;

	@SerializedName("gender")
	private String gender;

	@SerializedName("dob")
	private String dob;

	@SerializedName("image")
	private String image;

	@SerializedName("is_registered")
	private int isRegistered;

	@SerializedName("is_blocked")
	private int isBlocked;

	@SerializedName("lat")
	private String lat;

	@SerializedName("lng")
	private String lng;

	@SerializedName("location")
	private String location;

	@SerializedName("distance")
	private Object distance;

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

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
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

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setIsRegistered(int isRegistered){
		this.isRegistered = isRegistered;
	}

	public int getIsRegistered(){
		return isRegistered;
	}

	public void setIsBlocked(int isBlocked){
		this.isBlocked = isBlocked;
	}

	public int getIsBlocked(){
		return isBlocked;
	}

	public void setLat(String lat){
		this.lat = lat;
	}

	public String getLat(){
		return lat;
	}

	public void setLng(String lng){
		this.lng = lng;
	}

	public String getLng(){
		return lng;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setDistance(Object distance){
		this.distance = distance;
	}

	public Object getDistance(){
		return distance;
	}
}