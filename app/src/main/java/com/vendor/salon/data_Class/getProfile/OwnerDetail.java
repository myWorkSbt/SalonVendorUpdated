package com.vendor.salon.data_Class.getProfile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OwnerDetail implements Serializable {

	@SerializedName("name")
	private String name;

	@SerializedName("phone")
	private String phone;

	@SerializedName("email")
	private String email;

	@SerializedName("designation")
	private String designation;

	@SerializedName("gender")
	private String gender;

	@SerializedName("vendor_type")
	private String vendorType;

	@SerializedName("dob")
	private String dob;

	@SerializedName("is_registered")
	private int isRegistered;

	public String getVendorType() {
		return vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	@SerializedName("is_approved")
	private int isApproved;

	@SerializedName("user_image")
	private String userImage;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public void setIsRegistered(int isRegistered){
		this.isRegistered = isRegistered;
	}

	public int getIsRegistered(){
		return isRegistered;
	}

	public void setIsApproved(int isApproved){
		this.isApproved = isApproved;
	}

	public int getIsApproved(){
		return isApproved;
	}

	public void setUserImage(String userImage){
		this.userImage = userImage;
	}

	public String getUserImage(){
		return userImage;
	}

	@Override
 	public String toString(){
		return 
			"OwnerDetail{" + 
			"name = '" + name + '\'' + 
			",phone = '" + phone + '\'' + 
			",email = '" + email + '\'' + 
			",dob = '" + dob + '\'' + 
			",is_registered = '" + isRegistered + '\'' + 
			",is_approved = '" + isApproved + '\'' + 
			",user_image = '" + userImage + '\'' + 
			"}";
		}
}