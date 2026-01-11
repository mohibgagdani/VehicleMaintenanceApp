package com.example.vehiclemaintenanceapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddFuelActivity extends AppCompatActivity {
    private TextInputEditText fuelDateInput, fuelCostInput;
    private DataManager dataManager;
    private String vehicleNumber;
    private Calendar calendar;
    private int position = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fuel);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        position = getIntent().getIntExtra("position", -1);
        isEditMode = position != -1;
        dataManager = new DataManager(this);
        calendar = Calendar.getInstance();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(isEditMode ? "Edit Fuel" : "Add Fuel");
        toolbar.setNavigationOnClickListener(v -> finish());

        fuelDateInput = findViewById(R.id.fuelDateInput);
        fuelCostInput = findViewById(R.id.fuelCostInput);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setText(isEditMode ? "Update" : "Save");

        if (isEditMode) loadFuelData();

        fuelDateInput.setOnClickListener(v -> showDatePicker());
        saveButton.setOnClickListener(v -> saveFuel());
    }

    private void loadFuelData() {
        List<FuelRecord> records = dataManager.getFuelRecords(vehicleNumber);
        if (position >= 0 && position < records.size()) {
            FuelRecord record = records.get(position);
            fuelDateInput.setText(record.getDate());
            fuelCostInput.setText(record.getCost());
        }
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            fuelDateInput.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveFuel() {
        String date = fuelDateInput.getText().toString().trim();
        String cost = fuelCostInput.getText().toString().trim();

        if (date.isEmpty() || cost.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<FuelRecord> records = dataManager.getFuelRecords(vehicleNumber);
        if (isEditMode) {
            records.set(position, new FuelRecord(date, cost));
            Toast.makeText(this, "Fuel record updated", Toast.LENGTH_SHORT).show();
        } else {
            records.add(new FuelRecord(date, cost));
            Toast.makeText(this, "Fuel record added", Toast.LENGTH_SHORT).show();
        }
        dataManager.saveFuelRecords(vehicleNumber, records);
        finish();
    }
}
