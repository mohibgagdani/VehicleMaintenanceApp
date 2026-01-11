package com.example.vehiclemaintenanceapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddServiceActivity extends AppCompatActivity {
    private TextInputEditText serviceDateInput, serviceCostInput, notesInput;
    private AutoCompleteTextView serviceTypeSpinner;
    private DataManager dataManager;
    private String vehicleNumber;
    private Calendar calendar;
    private int position = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        position = getIntent().getIntExtra("position", -1);
        isEditMode = position != -1;
        dataManager = new DataManager(this);
        calendar = Calendar.getInstance();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(isEditMode ? "Edit Service" : "Add Service");
        toolbar.setNavigationOnClickListener(v -> finish());

        serviceDateInput = findViewById(R.id.serviceDateInput);
        serviceTypeSpinner = findViewById(R.id.serviceTypeSpinner);
        serviceCostInput = findViewById(R.id.serviceCostInput);
        notesInput = findViewById(R.id.notesInput);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setText(isEditMode ? "Update" : "Save");

        String[] types = {getString(R.string.general), getString(R.string.oil_change), getString(R.string.repair)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, types);
        serviceTypeSpinner.setAdapter(adapter);

        if (isEditMode) loadServiceData();

        serviceDateInput.setOnClickListener(v -> showDatePicker());
        saveButton.setOnClickListener(v -> saveService());
    }

    private void loadServiceData() {
        List<ServiceRecord> records = dataManager.getServiceRecords(vehicleNumber);
        if (position >= 0 && position < records.size()) {
            ServiceRecord record = records.get(position);
            serviceDateInput.setText(record.getDate());
            serviceTypeSpinner.setText(record.getType(), false);
            serviceCostInput.setText(record.getCost());
            notesInput.setText(record.getNotes());
        }
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            serviceDateInput.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveService() {
        String date = serviceDateInput.getText().toString().trim();
        String type = serviceTypeSpinner.getText().toString().trim();
        String cost = serviceCostInput.getText().toString().trim();
        String notes = notesInput.getText().toString().trim();

        if (date.isEmpty() || type.isEmpty() || cost.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<ServiceRecord> records = dataManager.getServiceRecords(vehicleNumber);
        if (isEditMode) {
            records.set(position, new ServiceRecord(date, type, cost, notes));
            Toast.makeText(this, "Service record updated", Toast.LENGTH_SHORT).show();
        } else {
            records.add(new ServiceRecord(date, type, cost, notes));
            Toast.makeText(this, "Service record added", Toast.LENGTH_SHORT).show();
        }
        dataManager.saveServiceRecords(vehicleNumber, records);
        finish();
    }
}
