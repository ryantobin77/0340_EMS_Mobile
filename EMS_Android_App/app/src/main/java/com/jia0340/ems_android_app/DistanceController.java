package com.jia0340.ems_android_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jia0340.ems_android_app.network.DistanceService;
import com.jia0340.ems_android_app.network.RetrofitClientDistanceAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class DistanceController {

    private LocationManager mLocationManager;
    private Location mCurrentLocation;

    private ObjectMapper mJsonMapper;
    private ObjectNode mRootJsonNode;

    private final List<String> mHospitalNames;
    private final List<Double> mLatitudes;
    private final List<Double> mLongitudes;
    private List<Double> mDistances;

    private HashMap<String, Integer> mHospitalNameIndices;
    private boolean mUpdating = false;

    private Context mContext;

    public DistanceController(List<String> hospitalNames, List<Double> latitudes, List<Double> longitudes, Context context) {

        mHospitalNames = hospitalNames;
        mLongitudes = longitudes;
        mLatitudes = latitudes;
        mContext = context;

        createDestinationJson();
        createHospitalHashMap();
    }

    public void checkForNewLocation(Context context) {

        if (!mUpdating) {
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Location loc = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);

                if (loc != null && !loc.equals(mCurrentLocation)) {
                    mCurrentLocation = loc;

                    Log.d("DistanceController", "Current latitude: " + Double.toString(loc.getLatitude()));
                    Log.d("DistanceController", "Current longitude: " + Double.toString(loc.getLongitude()));

                    mUpdating = true;

                    calculateDistances();
                }

            } else {
                Log.d("DistanceController: ", "Permissions were not granted!");
                //TODO: location permissions not granted, what do we want to do?
            }
        }
    }

    private void calculateDistances() {

        ArrayNode origins = mJsonMapper.createArrayNode();

        ObjectNode origin = mJsonMapper.createObjectNode();

        origin.put("latitude", mCurrentLocation.getLatitude());
        origin.put("longitude", mCurrentLocation.getLongitude());
        origins.add(origin);

        mRootJsonNode.set("origins", origins);

        String data = null;

        try {
            data = mJsonMapper.writeValueAsString(mRootJsonNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (data != null) {

            Log.d("DistanceController: ", "Making call to Bing API" + data);

            DistanceService service = RetrofitClientDistanceAPI.getRetrofitInstance().create(DistanceService.class);
            Call<JsonNode> call = service.getDistances("application/json", mRootJsonNode);
            call.enqueue(new Callback<JsonNode>() {
                @Override
                public void onResponse(Call<JsonNode> call, Response<JsonNode> response) {
                    if (response.isSuccessful()) {
                        parseDistances(response.body());
                    } else {
                        mUpdating = false;
                    }
                }

                @Override
                public void onFailure(Call<JsonNode> call, Throwable t) {
                    // Failed to collect hospital data
                    // TODO: what do we want to happen when it fails?
                    mUpdating = false;
                    Log.d("DistanceController", t.getMessage());
                }
            });
        } else {
            mUpdating = false;
            Log.d("DistanceController: ", "Data could not be posted");
        }

        mRootJsonNode.remove("origins");
    }

    private void createDestinationJson() {

        if (mJsonMapper == null) {
            mJsonMapper = new ObjectMapper();
            mRootJsonNode = mJsonMapper.createObjectNode();

            mRootJsonNode.put("travelMode", "driving");
            mRootJsonNode.put("distanceUnit", "mi");

            ArrayNode destinations = mJsonMapper.createArrayNode();

            for (int i = 0; i < mLatitudes.size(); i++) {
                ObjectNode dest = mJsonMapper.createObjectNode();

                dest.put("latitude", mLatitudes.get(i));
                dest.put("longitude", mLongitudes.get(i));

                destinations.add(dest);
            }

            mRootJsonNode.set("destinations", destinations);
        }

    }

    public void createHospitalHashMap() {

        if (mHospitalNameIndices == null) {

            mHospitalNameIndices = new HashMap<>();

            for (int i = 0; i < mHospitalNames.size(); i++) {
                mHospitalNameIndices.put(mHospitalNames.get(i), i);
            }

        }

    }

    private void parseDistances(JsonNode json) {

        Log.d("DistanceController: ", "Parsing distances from the response");

        mDistances = new ArrayList<>();

        ArrayNode resourceSets = (ArrayNode) json.get("resourceSets");

        for (JsonNode obj : resourceSets) {

            ArrayNode resources = (ArrayNode) obj.get("resources");

            for (JsonNode resource : resources) {

                ArrayNode results = (ArrayNode) resource.get("results");

                for (JsonNode result : results) {
                    int hospitalIndex = result.get("destinationIndex").asInt();
                    double distance = result.get("travelDistance").asDouble();

                    mDistances.add(hospitalIndex, distance);
                }

            }

        }

        mUpdating = false;
        sendCompleteBroadcast();
    }

    private void sendCompleteBroadcast() {

        Log.d("DistanceController: ", "Sending broadcast...");

        Intent intent = new Intent("DISTANCE_COMPLETE");
        mContext.sendBroadcast(intent);
    }

    public String getDistanceToHospital(String hospital) {

        if (mHospitalNameIndices == null || mDistances == null) {
            return "-";
        }

        double currDist = mDistances.get(mHospitalNameIndices.get(hospital));

        return String.format("%.2f", currDist);
    }
}
