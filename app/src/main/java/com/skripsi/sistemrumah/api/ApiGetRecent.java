package com.skripsi.sistemrumah.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import lombok.Data;

@Generated("org.jsonschema2pojo")
public
@Data
class ApiGetRecent {

    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("severity")
    @Expose
    String severity;

    @SerializedName("data")
    @Expose
    GetRecent data;
}
