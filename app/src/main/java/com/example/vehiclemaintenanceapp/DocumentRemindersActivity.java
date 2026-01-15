package com.example.vehiclemaintenanceapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class DocumentRemindersActivity extends AppCompatActivity {
    private TextView rcExpiryText, pucExpiryText, insuranceExpiryText;
    private DataManager dataManager;
    private String vehicleNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_reminders);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        dataManager = new DataManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        rcExpiryText = findViewById(R.id.rcExpiryText);
        pucExpiryText = findViewById(R.id.pucExpiryText);
        insuranceExpiryText = findViewById(R.id.insuranceExpiryText);

        loadReminders();
    }

    private void loadReminders() {
        String rcExpiry = dataManager.getDocumentData(vehicleNumber, "rcExpiryDate");
        String pucExpiry = dataManager.getDocumentData(vehicleNumber, "pucExpiryDate");
        String insuranceExpiry = dataManager.getDocumentData(vehicleNumber, "insuranceExpiryDate");

        rcExpiryText.setText(rcExpiry.isEmpty() ? "Not set" : "Expires on: " + rcExpiry);
        pucExpiryText.setText(pucExpiry.isEmpty() ? "Not set" : "Expires on: " + pucExpiry);
        insuranceExpiryText.setText(insuranceExpiry.isEmpty() ? "Not set" : "Expires on: " + insuranceExpiry);
    }
}
