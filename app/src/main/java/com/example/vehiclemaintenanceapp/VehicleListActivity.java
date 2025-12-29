package com.example.vehiclemaintenanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class VehicleListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VehicleAdapter adapter;
    private DataManager dataManager;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataManager = new DataManager(this);
        AppCompatDelegate.setDefaultNightMode(dataManager.isDarkMode() ? 
            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        recyclerView = findViewById(R.id.vehicleRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        MaterialButton addVehicleButton = findViewById(R.id.addVehicleButton);
        MaterialButton lightModeButton = findViewById(R.id.lightModeButton);
        MaterialButton darkModeButton = findViewById(R.id.darkModeButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        addVehicleButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddVehicleActivity.class));
        });

        lightModeButton.setOnClickListener(v -> {
            dataManager.saveTheme(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        });

        darkModeButton.setOnClickListener(v -> {
            dataManager.saveTheme(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            recreate();
        });

        loadVehicles();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVehicles();
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = dataManager.getVehicles();
        if (vehicles.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new VehicleAdapter(vehicles, new VehicleAdapter.OnVehicleClickListener() {
                @Override
                public void onVehicleClick(Vehicle vehicle) {
                    Intent intent = new Intent(VehicleListActivity.this, VehicleDashboardActivity.class);
                    intent.putExtra("vehicleNumber", vehicle.getVehicleNumber());
                    intent.putExtra("vehicleType", vehicle.getVehicleType());
                    intent.putExtra("brand", vehicle.getBrand());
                    intent.putExtra("model", vehicle.getModel());
                    startActivity(intent);
                }

                @Override
                public void onEditClick(int position) {
                    Intent intent = new Intent(VehicleListActivity.this, AddVehicleActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(int position) {
                    vehicles.remove(position);
                    dataManager.saveVehicles(vehicles);
                    loadVehicles();
                    Toast.makeText(VehicleListActivity.this, "Vehicle deleted", Toast.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }
}
