package com.vendor.salon.data_Class.inventoryCategory;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class DataItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("image")
	private Object image;

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

	private boolean selected = false ;

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

	public void setImage(Object image){
		this.image = image;
	}

	public Object getImage(){
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


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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
}