package com.vendor.salon.data_Class.appointment_categories;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class DataItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("image")
	private String image;

	@SerializedName("status")
	private int status;

	@SerializedName("type")
	private String type;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("is_delete")
	private int isDelete;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("gender")
	private String gender;

	@SerializedName("is_doorstep")
	private String isDoorstep;

	@SerializedName("disabled")
	private String disabled;

	@SerializedName("count")
	private String count;

	@SerializedName("selected")
	private String selected;

	@SerializedName("sum")
	private int sum;

	@SerializedName("service_name")
	private String serviceName;

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

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public void setIsDelete(int isDelete){
		this.isDelete = isDelete;
	}

	public int getIsDelete(){
		return isDelete;
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

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setIsDoorstep(String isDoorstep){
		this.isDoorstep = isDoorstep;
	}

	public String getIsDoorstep(){
		return isDoorstep;
	}

	public void setDisabled(String disabled){
		this.disabled = disabled;
	}

	public String getDisabled(){
		return disabled;
	}

	public void setCount(String count){
		this.count = count;
	}

	public String getCount(){
		return count;
	}

	public void setSelected(String selected){
		this.selected = selected;
	}

	public String getSelected(){
		return selected;
	}

	public void setSum(int sum){
		this.sum = sum;
	}

	public int getSum(){
		return sum;
	}

	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}

	public String getServiceName(){
		return serviceName;
	}
}