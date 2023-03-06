package com.vendor.salon.data_Class.getProfile;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SalonDetail implements Serializable {
    private List<BannerItem> banner;
    private String salon_name;
    private String phone;
    private String email;
    private String address;
    private String id_proof_image;

    @SerializedName("galleries")
    private List<Galleries> galleries;

    private String licence_image;
    private String about;

    public List<BannerItem> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerItem> banner) {
        this.banner = banner;
    }

    public String getSalon_name() {
        return salon_name;
    }

    public void setSalon_name(String salon_name) {
        this.salon_name = salon_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_proof_image() {
        return id_proof_image;
    }

    public void setId_proof_image(String id_proof_image) {
        this.id_proof_image = id_proof_image;
    }

    public String getLicence_image() {
        return licence_image;
    }

    public void setLicence_image(String licence_image) {
        this.licence_image = licence_image;
    }

    public List<Galleries> getGalleries() {
        return galleries;
    }

    public void setGalleries(List<Galleries> galleries) {
        this.galleries = galleries;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
