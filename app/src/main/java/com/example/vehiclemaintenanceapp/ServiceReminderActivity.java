package com.example.vehiclemaintenanceapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ServiceReminderActivity extends AppCompatActivity {
    private TextInputEditText currentKmInput, oilChangeKmInput;
    private TextView currentKmDisplay, nextOilChangeDisplay;
    private DataManager dataManager;
    private String vehicleNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_reminder);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        dataManager = new DataManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        currentKmInput = findViewById(R.id.currentKmInput);
        oilChangeKmInput = findViewById(R.id.oilChangeKmInput);
        currentKmDisplay = findViewById(R.id.currentKmDisplay);
        nextOilChangeDisplay = findViewById(R.id.nextOilChangeDisplay);
        MaterialButton setKmReminderButton = findViewById(R.id.setKmReminderButton);

        loadData();

        setKmReminderButton.setOnClickListener(v -> setKmReminder());
    }

    private void loadData() {
        String savedKm = dataManager.getReminderData(vehicleNumber, "currentKm");
        String savedOilChangeKm = dataManager.getReminderData(vehicleNumber, "oilChangeKm");
        
        if (!savedKm.isEmpty()) {
            currentKmDisplay.setText(savedKm + " KM");
            currentKmInput.setText(savedKm);
        }
        if (!savedOilChangeKm.isEmpty()) {
            oilChangeKmInput.setText(savedOilChangeKm);
        }

        updateNextOilChangeDisplay();
    }

    private void updateNextOilChangeDisplay() {
        String savedKm = dataManager.getReminderData(vehicleNumber, "currentKm");
        String savedOilChangeKm = dataManager.getReminderData(vehicleNumber, "oilChangeKm");

        if (!savedKm.isEmpty() && !savedOilChangeKm.isEmpty()) {
            int current = Integer.parseInt(savedKm);
            int oilChange = Integer.parseInt(savedOilChangeKm);
            int nextOilChange = current + oilChange;
            nextOilChangeDisplay.setText("Next Oil Change: " + nextOilChange + " KM");
        } else {
            nextOilChangeDisplay.setText("Next Oil Change: Not Set");
        }
    }

    private void setKmReminder() {
        String currentKm = currentKmInput.getText().toString().trim();
        String oilChangeKm = oilChangeKmInput.getText().toString().trim();

        if (currentKm.isEmpty() || oilChangeKm.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        dataManager.saveReminderData(vehicleNumber, "currentKm", currentKm);
        dataManager.saveReminderData(vehicleNumber, "oilChangeKm", oilChangeKm);

        int current = Integer.parseInt(currentKm);
        int oilChange = Integer.parseInt(oilChangeKm);
        int nextOilChange = current + oilChange;

        currentKmDisplay.setText(currentKm + " KM");
        nextOilChangeDisplay.setText("Next Oil Change: " + nextOilChange + " KM");

        Toast.makeText(this, "Reminder saved! Next oil change at " + nextOilChange + " KM", Toast.LENGTH_LONG).show();
    }
}
