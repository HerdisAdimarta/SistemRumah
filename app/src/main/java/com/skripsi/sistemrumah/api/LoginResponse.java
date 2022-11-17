package com.skripsi.sistemrumah.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import lombok.Data;

@Generated("org.jsonschema2pojo")
public
@Data
class LoginResponse {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("severity")
    @Expose
    String severity;
    @SerializedName("user")
    @Expose
    String user;
    @SerializedName("role")
    @Expose
    int role;
    @SerializedName("id_user")
    @Expose
    String idUser;
}
