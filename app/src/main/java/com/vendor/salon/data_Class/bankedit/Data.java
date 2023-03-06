package com.vendor.salon.data_Class.bankedit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("agent_id")
	private String agentId;

	@SerializedName("for_approval")
	private String forApproval;

	@SerializedName("bank_name")
	private String bankName;

	@SerializedName("account_holder_name")
	private String accountHolderName;

	@SerializedName("account_no")
	private String accountNo;

	@SerializedName("ifsc_code")
	private String ifscCode;

	@SerializedName("check_image")
	private String checkImage;

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

	public void setAgentId(String agentId){
		this.agentId = agentId;
	}

	public String getAgentId(){
		return agentId;
	}

	public void setForApproval(String forApproval){
		this.forApproval = forApproval;
	}

	public String getForApproval(){
		return forApproval;
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

	public void setCheckImage(String checkImage){
		this.checkImage = checkImage;
	}

	public String getCheckImage(){
		return checkImage;
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