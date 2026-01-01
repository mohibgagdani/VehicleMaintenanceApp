package com.example.vehiclemaintenanceapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class VehicleDashboardActivity extends AppCompatActivity {
    private String vehicleNumber;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_dashboard);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        String vehicleType = getIntent().getStringExtra("vehicleType");
        String brand = getIntent().getStringExtra("brand");
        String model = getIntent().getStringExtra("model");

        dataManager = new DataManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(vehicleNumber);
        toolbar.setSubtitle(brand + " " + model);
        toolbar.setNavigationOnClickListener(v -> finish());

        MaterialButton themeToggleButton = findViewById(R.id.themeToggleButton);
        MaterialCardView serviceHistoryCard = findViewById(R.id.serviceHistoryCard);
        MaterialCardView fuelTrackerCard = findViewById(R.id.fuelTrackerCard);
        MaterialCardView serviceReminderCard = findViewById(R.id.serviceReminderCard);
        MaterialCardView documentsCard = findViewById(R.id.documentsCard);
        MaterialCardView emergencyContactsCard = findViewById(R.id.emergencyContactsCard);

        themeToggleButton.setOnClickListener(v -> toggleTheme());

        serviceHistoryCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceHistoryActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });

        fuelTrackerCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, FuelTrackerActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });

        serviceReminderCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceReminderActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });

        documentsCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, DocumentsActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });

        emergencyContactsCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, EmergencyContactsActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });
    }

    private void toggleTheme() {
        boolean isDark = dataManager.isDarkMode();
        dataManager.saveTheme(!isDark);
        AppCompatDelegate.setDefaultNightMode(!isDark ? 
            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
    }
}
