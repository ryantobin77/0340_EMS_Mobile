package com.jia0340.ems_android_app.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Class representing a Hospital object.
 *
 * @author Allison Nakazawa & Anna Dingler
 * Created on 1/24/21
 */
public class Hospital {

    private String mName;
    private NedocsScore mNedocsScore;
    private ArrayList<HospitalType> mHospitalTypes;
    private ArrayList<String> mDiversions;
    private String mPhoneNumber;
    private String mStreetAddress;
    private String mCity;
    private String mState;
    private String mZipCode;
    private String mCounty;
    private String mRegion;
    private String mRegionalCoordinatingHospital;
    private String mLastUpdated;
    private double mLatitude;
    private double mLongitude;

    private double mDistance;
    private boolean mExpanded = false;
    private boolean mFavorite = false;

    //TODO: figure out naming conventions
    @JsonCreator
    public Hospital(@JsonProperty("name") String name,
                    @JsonProperty("nedocs_score") NedocsScore nedocsScore,
                    @JsonProperty("specialty_centers") ArrayList<HospitalType> hospitalTypes,
                    @JsonProperty("phone") String phoneNumber,
                    @JsonProperty("street") String street,
                    @JsonProperty("city") String city,
                    @JsonProperty("state") String state,
                    @JsonProperty("zip") String zip,
                    @JsonProperty("county") String county,
                    @JsonProperty("ems_region") String region,
                    @JsonProperty("rch") String regionalCoordinatingHospital,
                    @JsonProperty("diversions") ArrayList<String> diversions,
                    @JsonProperty("last_updated") String lastUpdated,
                    @JsonProperty("lat") double latitude,
                    @JsonProperty("long") double longitude) {

        mName = name;
        mNedocsScore = nedocsScore;
        mHospitalTypes = hospitalTypes;
        mPhoneNumber = phoneNumber;
        mStreetAddress = street;
        mCity = city;
        mState = state;
        mZipCode = zip;
        mCounty = county;
        mRegion = region;
        mRegionalCoordinatingHospital = regionalCoordinatingHospital;
        mDiversions = diversions;
        mLastUpdated = lastUpdated;
        mLatitude = latitude;
        mLongitude = longitude;

        mDistance = 1.11;

    }

    public String getName() {
        return mName;
    }

    public NedocsScore getNedocsScore() {
        return mNedocsScore;
    }

    public double getDistance() {
        return mDistance;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getStreetAddress() {
        return mStreetAddress;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public String getCounty() {
        return mCounty;
    }

    public String getRegion() {
        return mRegion;
    }

    public String getRegionalCoordinatingHospital() {
        return mRegionalCoordinatingHospital;
    }

    public ArrayList<HospitalType> getHospitalTypes() {
        return mHospitalTypes;
    }

    public ArrayList<String> getDiversions() {
        return mDiversions;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }
}
