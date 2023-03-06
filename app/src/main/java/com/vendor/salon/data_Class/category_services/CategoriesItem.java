package com.vendor.salon.data_Class.category_services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CategoriesItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("name")
	private String name;

	@SerializedName("image")
	private String image;

	@SerializedName("price")
	private Object price;

	@SerializedName("discounted_price")
	private Object discountedPrice;

	@SerializedName("discount_percent")
	private Object discountPercent;

	@SerializedName("status")
	private int status;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("updated_at")
	private String updatedAt;

	private boolean isActive = true ;

	@SerializedName("deleted_at")
	private Object deletedAt;

	private boolean selected = false ;

	public void setId(int id){
		this.id = id;
	}


	public int getId(){
		return id;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
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

	public void setPrice(Object price){
		this.price = price;
	}

	public Object getPrice(){
		return price;
	}

	public void setDiscountedPrice(Object discountedPrice){
		this.discountedPrice = discountedPrice;
	}

	public Object getDiscountedPrice(){
		return discountedPrice;
	}

	public void setDiscountPercent(Object discountPercent){
		this.discountPercent = discountPercent;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public Object getDiscountPercent(){
		return discountPercent;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getStatus(){
		return status;
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
}