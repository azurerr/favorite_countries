package com.example.jihyun_vacation.models;

import com.google.gson.annotations.SerializedName;

public class Country {
    private String id;
    private String name;
    private String nativeName;
    private @SerializedName("alpha2Code") String code;
    private String subregion;
    private String capital;
    // private String flag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getCode() {
        return code;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getCapital() {
        return capital;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nativeName='" + nativeName + '\'' +
                ", code='" + code + '\'' +
                ", subregion='" + subregion + '\'' +
                ", capital='" + capital + '\'' +
                '}';
    }
}
