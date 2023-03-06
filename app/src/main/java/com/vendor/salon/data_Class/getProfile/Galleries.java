package com.vendor.salon.data_Class.getProfile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Galleries implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("table_name")
	private String tableName;

	@SerializedName("table_name_id")
	private String tableNameId;

	@SerializedName("child_name")
	private String childName;

	@SerializedName("doc_type")
	private String docType;

	@SerializedName("doc_ext")
	private String docExt;

	@SerializedName("doc_path")
	private String docPath;

	@SerializedName("is_deleted")
	private String isDeleted;

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

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public String getTableName(){
		return tableName;
	}

	public void setTableNameId(String tableNameId){
		this.tableNameId = tableNameId;
	}

	public String getTableNameId(){
		return tableNameId;
	}

	public void setChildName(String childName){
		this.childName = childName;
	}

	public String getChildName(){
		return childName;
	}

	public void setDocType(String docType){
		this.docType = docType;
	}

	public String getDocType(){
		return docType;
	}

	public void setDocExt(String docExt){
		this.docExt = docExt;
	}

	public String getDocExt(){
		return docExt;
	}

	public void setDocPath(String docPath){
		this.docPath = docPath;
	}

	public String getDocPath(){
		return docPath;
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
}