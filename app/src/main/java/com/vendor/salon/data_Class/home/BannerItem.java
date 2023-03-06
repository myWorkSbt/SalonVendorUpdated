package com.vendor.salon.data_Class.home;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BannerItem implements Serializable {


    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("user_type")
    private String userType;

    @SerializedName("link_type")
    private Object linkType;

    @SerializedName("link")
    private Object link;

    @SerializedName("timer_text")
    private Object timerText;

    @SerializedName("timer_end_on")
    private Object timerEndOn;

    @SerializedName("status")
    private int status;

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

    public void setUserType(String userType){
        this.userType = userType;
    }

    public String getUserType(){
        return userType;
    }

    public void setLinkType(Object linkType){
        this.linkType = linkType;
    }

    public Object getLinkType(){
        return linkType;
    }

    public void setLink(Object link){
        this.link = link;
    }

    public Object getLink(){
        return link;
    }

    public void setTimerText(Object timerText){
        this.timerText = timerText;
    }

    public Object getTimerText(){
        return timerText;
    }

    public void setTimerEndOn(Object timerEndOn){
        this.timerEndOn = timerEndOn;
    }

    public Object getTimerEndOn(){
        return timerEndOn;
    }

    public void setStatus(int status){
        this.status = status;
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


}
