package com.vendor.salon.data_Class.addstaff;

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

	@SerializedName("dob")
	private String dob;

	@SerializedName("gender")
	private String gender;

	@SerializedName("vendor_type")
	private String vendorType;

	@SerializedName("shop_name")
	private Object shopName;

	@SerializedName("optional")
	private Object optional;

	@SerializedName("about")
	private Object about;

	@SerializedName("designation")
	private String designation;

	@SerializedName("address")
	private Object address;

	@SerializedName("lat")
	private Object lat;

	@SerializedName("lng")
	private Object lng;

	@SerializedName("service_for")
	private Object serviceFor;

	@SerializedName("ratings")
	private Object ratings;

	@SerializedName("id_proof_image")
	private Object idProofImage;

	@SerializedName("licence_image")
	private Object licenceImage;

	@SerializedName("verified_at")
	private Object verifiedAt;

	@SerializedName("is_registered")
	private int isRegistered;

	@SerializedName("owner_image")
	private Object ownerImage;

	@SerializedName("is_approved")
	private int isApproved;

	@SerializedName("parent")
	private String parent;

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

	public void setShopName(Object shopName){
		this.shopName = shopName;
	}

	public Object getShopName(){
		return shopName;
	}

	public void setOptional(Object optional){
		this.optional = optional;
	}

	public Object getOptional(){
		return optional;
	}

	public void setAbout(Object about){
		this.about = about;
	}

	public Object getAbout(){
		return about;
	}

	public void setDesignation(String designation){
		this.designation = designation;
	}

	public String getDesignation(){
		return designation;
	}

	public void setAddress(Object address){
		this.address = address;
	}

	public Object getAddress(){
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

	public void setServiceFor(Object serviceFor){
		this.serviceFor = serviceFor;
	}

	public Object getServiceFor(){
		return serviceFor;
	}

	public void setRatings(Object ratings){
		this.ratings = ratings;
	}

	public Object getRatings(){
		return ratings;
	}

	public void setIdProofImage(Object idProofImage){
		this.idProofImage = idProofImage;
	}

	public Object getIdProofImage(){
		return idProofImage;
	}

	public void setLicenceImage(Object licenceImage){
		this.licenceImage = licenceImage;
	}

	public Object getLicenceImage(){
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

	public void setOwnerImage(Object ownerImage){
		this.ownerImage = ownerImage;
	}

	public Object getOwnerImage(){
		return ownerImage;
	}

	public void setIsApproved(int isApproved){
		this.isApproved = isApproved;
	}

	public int getIsApproved(){
		return isApproved;
	}

	public void setParent(String parent){
		this.parent = parent;
	}

	public String getParent(){
		return parent;
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