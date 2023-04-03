package com.vendor.salon.data_Class.manage_inventory;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeleteInventoryResponse  implements Serializable {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
