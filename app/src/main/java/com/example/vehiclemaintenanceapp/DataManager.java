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
}