package com.jia0340.ems_android_app.network;

import com.fasterxml.jackson.databind.JsonNode;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Interface that makes service calls to Django database
 *
 * @author Anna Dingler
 * Created on 2/22/21
 */
public interface DistanceService {

    @POST("DistanceMatrix?key=AuFw3loi8VYCD9XJsvEuG7eoaoFcq1moNc-UPuQlSZrZqj6WNaBBoCAz_MGKteG5")
    Call<JsonNode> getDistances(@Header("Content-Type") String json, @Body JsonNode data);
}
