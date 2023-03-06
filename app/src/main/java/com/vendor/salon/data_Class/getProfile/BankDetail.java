package com.vendor.salon.data_Class.getProfile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BankDetail implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("bank_name")
	private String bankName;

	@SerializedName("account_holder_name")
	private String accountHolderName;

	@SerializedName("account_no")
	private String accountNo;

	@SerializedName("ifsc_code")
	private String ifscCode;

	@SerializedName("image")
	private String image;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("cancel_check")
	private String cancelCheck;

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

	public void setBankName(String bankName){
		this.bankName = bankName;
	}

	public String getBankName(){
		return bankName;
	}

	public void setAccountHolderName(String accountHolderName){
		this.accountHolderName = accountHolderName;
	}

	public String getAccountHolderName(){
		return accountHolderName;
	}

	public void setAccountNo(String accountNo){
		this.accountNo = accountNo;
	}

	public String getAccountNo(){
		return accountNo;
	}

	public void setIfscCode(String ifscCode){
		this.ifscCode = ifscCode;
	}

	public String getIfscCode(){
		return ifscCode;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
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

	public void setCancelCheck(String cancelCheck){
		this.cancelCheck = cancelCheck;
	}

	public String getCancelCheck(){
		return cancelCheck;
	}

	@Override
 	public String toString(){
		return 
			"BankDetail{" + 
			"id = '" + id + '\'' + 
			",user_id = '" + userId + '\'' + 
			",bank_name = '" + bankName + '\'' + 
			",account_holder_name = '" + accountHolderName + '\'' + 
			",account_no = '" + accountNo + '\'' + 
			",ifsc_code = '" + ifscCode + '\'' + 
			",image = '" + image + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",cancel_check = '" + cancelCheck + '\'' + 
			"}";
		}
}