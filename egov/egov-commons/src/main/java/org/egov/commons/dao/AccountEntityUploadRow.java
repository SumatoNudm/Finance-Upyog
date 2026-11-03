package org.egov.commons.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;



public class AccountEntityUploadRow {


    @SerializedName("Name*")
    @JsonProperty("Name*")
    private String name;

    @SerializedName("Code*")
    @JsonProperty("Code*")
    private String code;

    @SerializedName("Narration")
    @JsonProperty("Narration")
    private String narration;


    @SerializedName("Active")
    private String active;
    public String getActive() { return active; }
    public void setActive(String active) { this.active = active; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }


}