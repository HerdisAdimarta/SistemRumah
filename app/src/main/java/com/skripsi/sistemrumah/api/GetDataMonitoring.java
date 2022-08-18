package com.skripsi.sistemrumah.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import lombok.Data;

@Generated("org.jsonschema2pojo")
public
@Data
class GetDataMonitoring {
    @SerializedName("id_tag")
    @Expose
    String id_tag;
    @SerializedName("action")
    @Expose
    String action;
    @SerializedName("datetime")
    @Expose
    String datetime;

}
