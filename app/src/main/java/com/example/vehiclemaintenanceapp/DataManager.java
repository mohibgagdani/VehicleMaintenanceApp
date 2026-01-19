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
    public List<ServiceRecord> getServiceRecords(String vehicleNumber) {
        List<ServiceRecord> records = new ArrayList<>();
        String json = prefs.getString("service_" + vehicleNumber, "[]");
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                records.add(new ServiceRecord(
                        obj.getString("date"),
                        obj.getString("type"),
                        obj.getString("cost"),
                        obj.getString("notes")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return records;
    }
    public void saveFuelRecords(String vehicleNumber, List<FuelRecord> records) {
        JSONArray jsonArray = new JSONArray();
        for (FuelRecord record : records) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("date", record.getDate());
                obj.put("cost", record.getCost());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString("fuel_" + vehicleNumber, jsonArray.toString()).apply();
    }
        public List<ServiceRecord> getServiceRecords(String vehicleNumber) {
            List<ServiceRecord> records = new ArrayList<>();
            String json = prefs.getString("service_" + vehicleNumber, "[]");
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    records.add(new ServiceRecord(
                            obj.getString("date"),
                            obj.getString("type"),
                            obj.getString("cost"),
                            obj.getString("notes")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return records;
        }
    public void saveEmergencyContacts(String vehicleNumber, List<EmergencyContact> contacts) {
        JSONArray jsonArray = new JSONArray();
        for (EmergencyContact contact : contacts) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", contact.getName());
                obj.put("phone", contact.getPhone());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString("contacts_" + vehicleNumber, jsonArray.toString()).apply();
    }

    public List<EmergencyContact> getEmergencyContacts(String vehicleNumber) {
        List<EmergencyContact> contacts = new ArrayList<>();
        String json = prefs.getString("contacts_" + vehicleNumber, "[]");
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                contacts.add(new EmergencyContact(
                        obj.getString("name"),
                        obj.getString("phone")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contacts;
    }
    public void saveDocumentData(String vehicleNumber, String key, String value) {
        prefs.edit().putString("doc_" + vehicleNumber + "_" + key, value).apply();
    }

    public String getDocumentData(String vehicleNumber, String key) {
        return prefs.getString("doc_" + vehicleNumber + "_" + key, "");
    }

    public void saveReminderData(String vehicleNumber, String key, String value) {
        prefs.edit().putString("reminder_" + vehicleNumber + "_" + key, value).apply();
    }

    public String getReminderData(String vehicleNumber, String key) {
        return prefs.getString("reminder_" + vehicleNumber + "_" + key, "");
    }

    public void saveTheme(boolean isDarkMode) {
        prefs.edit().putBoolean(KEY_THEME, isDarkMode).apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_THEME, false);
    }

}


