package com.vendor.salon.data_Class.manage_service;

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

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("is_delete")
	private int isDelete;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("count")
	private int count;

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

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}
}