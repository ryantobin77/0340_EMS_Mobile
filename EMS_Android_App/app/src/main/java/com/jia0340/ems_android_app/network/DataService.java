package com.jia0340.ems_android_app.network;

import com.jia0340.ems_android_app.models.Hospital;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface that makes service calls to Django database
 *
 * @author Anna Dingler
 * Created on 2/22/21
 */
public interface DataService {

    @GET("hospitals/")
    Call<List<Hospital>> getHospitals();
}
