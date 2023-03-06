package com.vendor.salon.data_Class;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LogoutResponse implements Serializable {

    @SerializedName("result")
    private boolean result;

    @SerializedName("message")
    private String message;

    public void setResult(boolean result){
        this.result = result;
    }

    public boolean isResult(){
        return result;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String toString(){
        return
                "LogoutResponse{" +
                        "result = '" + result + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
