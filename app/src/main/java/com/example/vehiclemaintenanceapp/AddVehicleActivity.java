package com.example.vehiclemaintenanceapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class AddVehicleActivity extends AppCompatActivity {
    private AutoCompleteTextView vehicleTypeSpinner;
    private TextInputEditText vehicleNumberInput, brandInput, modelInput;
    private DataManager dataManager;
    private int position = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        position = getIntent().getIntExtra("position", -1);
        isEditMode = position != -1;
        dataManager = new DataManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(isEditMode ? "Edit Vehicle" : "Add Vehicle");
        toolbar.setNavigationOnClickListener(v -> finish());

        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);
        vehicleNumberInput = findViewById(R.id.vehicleNumberInput);
        brandInput = findViewById(R.id.brandInput);
        modelInput = findViewById(R.id.modelInput);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setText(isEditMode ? "Update" : "Save");

        String[] types = {getString(R.string.bike), getString(R.string.car), "Heavy Vehicle"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, types);
        vehicleTypeSpinner.setAdapter(adapter);

        if (isEditMode) loadVehicleData();

        saveButton.setOnClickListener(v -> saveVehicle());
    }

    private void loadVehicleData() {
        List<Vehicle> vehicles = dataManager.getVehicles();
        if (position >= 0 && position < vehicles.size()) {
            Vehicle vehicle = vehicles.get(position);
            vehicleTypeSpinner.setText(vehicle.getVehicleType(), false);
            vehicleNumberInput.setText(vehicle.getVehicleNumber());
            brandInput.setText(vehicle.getBrand());
            modelInput.setText(vehicle.getModel());
        }
    }

    private void saveVehicle() {
        String type = vehicleTypeSpinner.getText().toString().trim();
        String number = vehicleNumberInput.getText().toString().trim();
        String brand = brandInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();

        if (type.isEmpty() || number.isEmpty() || brand.isEmpty() || model.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Vehicle> vehicles = dataManager.getVehicles();
        
        if (!isEditMode) {
            for (Vehicle v : vehicles) {
                if (v.getVehicleNumber().equalsIgnoreCase(number)) {
                    Toast.makeText(this, "Vehicle number already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (isEditMode) {
            vehicles.set(position, new Vehicle(number, type, brand, model));
            Toast.makeText(this, "Vehicle updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            vehicles.add(new Vehicle(number, type, brand, model));
            Toast.makeText(this, "Vehicle added successfully", Toast.LENGTH_SHORT).show();
        }
        dataManager.saveVehicles(vehicles);
        finish();
    }
}
