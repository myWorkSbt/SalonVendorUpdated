package com.vendor.salon.data_Class.areacover;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("phone")
	private String phone;

	@SerializedName("contact_no2")
	private Object contactNo2;

	@SerializedName("email")
	private String email;

	@SerializedName("password")
	private Object password;

	@SerializedName("dob")
	private String dob;

	@SerializedName("gender")
	private String gender;

	@SerializedName("vendor_type")
	private String vendorType;

	@SerializedName("shop_name")
	private String shopName;

	@SerializedName("optional")
	private Object optional;

	@SerializedName("about")
	private String about;

	@SerializedName("designation")
	private String designation;

	@SerializedName("address")
	private String address;

	@SerializedName("lat")
	private Object lat;

	@SerializedName("lng")
	private Object lng;

	@SerializedName("service_for")
	private String serviceFor;

	@SerializedName("ratings")
	private String ratings;

	@SerializedName("id_proof_image")
	private String idProofImage;

	@SerializedName("licence_image")
	private String licenceImage;

	@SerializedName("verified_at")
	private Object verifiedAt;

	@SerializedName("is_registered")
	private int isRegistered;

	@SerializedName("owner_image")
	private String ownerImage;

	@SerializedName("is_approved")
	private int isApproved;

	@SerializedName("parent")
	private Object parent;

	@SerializedName("monday")
	private String monday;

	@SerializedName("tuesday")
	private String tuesday;

	@SerializedName("wednesday")
	private String wednesday;

	@SerializedName("thursday")
	private String thursday;

	@SerializedName("friday")
	private String friday;

	@SerializedName("saturday")
	private String saturday;

	@SerializedName("sunday")
	private String sunday;

	@SerializedName("wallet")
	private Object wallet;

	@SerializedName("area_cover")
	private String areaCover;

	@SerializedName("is_deleted")
	private String isDeleted;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("status")
	private int status;

	@SerializedName("deleted_at")
	private Object deletedAt;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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

	public void setContactNo2(Object contactNo2){
		this.contactNo2 = contactNo2;
	}

	public Object getContactNo2(){
		return contactNo2;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setPassword(Object password){
		this.password = password;
	}

	public Object getPassword(){
		return password;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setVendorType(String vendorType){
		this.vendorType = vendorType;
	}

	public String getVendorType(){
		return vendorType;
	}

	public void setShopName(String shopName){
		this.shopName = shopName;
	}

	public String getShopName(){
		return shopName;
	}

	public void setOptional(Object optional){
		this.optional = optional;
	}

	public Object getOptional(){
		return optional;
	}

	public void setAbout(String about){
		this.about = about;
	}

	public String getAbout(){
		return about;
	}

	public void setDesignation(String designation){
		this.designation = designation;
	}

	public String getDesignation(){
		return designation;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setLat(Object lat){
		this.lat = lat;
	}

	public Object getLat(){
		return lat;
	}

	public void setLng(Object lng){
		this.lng = lng;
	}

	public Object getLng(){
		return lng;
	}

	public void setServiceFor(String serviceFor){
		this.serviceFor = serviceFor;
	}

	public String getServiceFor(){
		return serviceFor;
	}

	public void setRatings(String ratings){
		this.ratings = ratings;
	}

	public String getRatings(){
		return ratings;
	}

	public void setIdProofImage(String idProofImage){
		this.idProofImage = idProofImage;
	}

	public String getIdProofImage(){
		return idProofImage;
	}

	public void setLicenceImage(String licenceImage){
		this.licenceImage = licenceImage;
	}

	public String getLicenceImage(){
		return licenceImage;
	}

	public void setVerifiedAt(Object verifiedAt){
		this.verifiedAt = verifiedAt;
	}

	public Object getVerifiedAt(){
		return verifiedAt;
	}

	public void setIsRegistered(int isRegistered){
		this.isRegistered = isRegistered;
	}

	public int getIsRegistered(){
		return isRegistered;
	}

	public void setOwnerImage(String ownerImage){
		this.ownerImage = ownerImage;
	}

	public String getOwnerImage(){
		return ownerImage;
	}

	public void setIsApproved(int isApproved){
		this.isApproved = isApproved;
	}

	public int getIsApproved(){
		return isApproved;
	}

	public void setParent(Object parent){
		this.parent = parent;
	}

	public Object getParent(){
		return parent;
	}

	public void setMonday(String monday){
		this.monday = monday;
	}

	public String getMonday(){
		return monday;
	}

	public void setTuesday(String tuesday){
		this.tuesday = tuesday;
	}

	public String getTuesday(){
		return tuesday;
	}

	public void setWednesday(String wednesday){
		this.wednesday = wednesday;
	}

	public String getWednesday(){
		return wednesday;
	}

	public void setThursday(String thursday){
		this.thursday = thursday;
	}

	public String getThursday(){
		return thursday;
	}

	public void setFriday(String friday){
		this.friday = friday;
	}

	public String getFriday(){
		return friday;
	}

	public void setSaturday(String saturday){
		this.saturday = saturday;
	}

	public String getSaturday(){
		return saturday;
	}

	public void setSunday(String sunday){
		this.sunday = sunday;
	}

	public String getSunday(){
		return sunday;
	}

	public void setWallet(Object wallet){
		this.wallet = wallet;
	}

	public Object getWallet(){
		return wallet;
	}

	public void setAreaCover(String areaCover){
		this.areaCover = areaCover;
	}

	public String getAreaCover(){
		return areaCover;
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

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}
}