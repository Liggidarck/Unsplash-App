package com.george.unsplashapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Exif implements Serializable {

    @SerializedName("make")
    @Expose
    private String make;

    @SerializedName("model")
    @Expose
    private String model;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("exposure_time")
    @Expose
    private String exposure_time;

    @SerializedName("aperture")
    @Expose
    private String aperture;

    @SerializedName("focal_length")
    @Expose
    private String focal_length;

    @SerializedName("iso")
    @Expose
    private int iso;

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getExposure_time() {
        return exposure_time;
    }

    public String getAperture() {
        return aperture;
    }

    public String getFocal_length() {
        return focal_length;
    }

    public int getIso() {
        return iso;
    }
}
