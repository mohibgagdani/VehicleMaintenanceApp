package com.example.vehiclemaintenanceapp;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class DataManager {
    private static final String PREFS_NAME = "VehicleMaintenancePrefs";
    private static final String KEY_VEHICLES = "vehicles";
    private static final String KEY_THEME = "theme_mode";
    private SharedPreferences prefs;

    public DataManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public void saveVehicles(List<Vehicle> vehicles) {
        JSONArray jsonArray = new JSONArray();
        for (Vehicle vehicle : vehicles) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("vehicleNumber", vehicle.getVehicleNumber());
                obj.put("vehicleType", vehicle.getVehicleType());
                obj.put("brand", vehicle.getBrand());
                obj.put("model", vehicle.getModel());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString(KEY_VEHICLES, jsonArray.toString()).apply();
    }
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String json = prefs.getString(KEY_VEHICLES, "[]");
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                vehicles.add(new Vehicle(
                        obj.getString("vehicleNumber"),
                        obj.getString("vehicleType"),
                        obj.getString("brand"),
                        obj.getString("model")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
    public void saveServiceRecords(String vehicleNumber, List<ServiceRecord> records) {
        JSONArray jsonArray = new JSONArray();
        for (ServiceRecord record : records) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("date", record.getDate());
                obj.put("type", record.getType());
                obj.put("cost", record.getCost());
                obj.put("notes", record.getNotes());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString("service_" + vehicleNumber, jsonArray.toString()).apply();
    }
}