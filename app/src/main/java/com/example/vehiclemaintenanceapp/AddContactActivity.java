package com.example.vehiclemaintenanceapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {
    private TextInputEditText contactNameInput, phoneNumberInput;
    private DataManager dataManager;
    private String vehicleNumber;
    private int position = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        position = getIntent().getIntExtra("position", -1);
        isEditMode = position != -1;
        dataManager = new DataManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(isEditMode ? "Edit Contact" : "Add Contact");
        toolbar.setNavigationOnClickListener(v -> finish());

        contactNameInput = findViewById(R.id.contactNameInput);
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setText(isEditMode ? "Update" : "Save");

        if (isEditMode) loadContactData();

        saveButton.setOnClickListener(v -> saveContact());
    }

    private void loadContactData() {
        List<EmergencyContact> contacts = dataManager.getEmergencyContacts(vehicleNumber);
        if (position >= 0 && position < contacts.size()) {
            EmergencyContact contact = contacts.get(position);
            contactNameInput.setText(contact.getName());
            phoneNumberInput.setText(contact.getPhone());
        }
    }

    private void saveContact() {
        String name = contactNameInput.getText().toString().trim();
        String phone = phoneNumberInput.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<EmergencyContact> contacts = dataManager.getEmergencyContacts(vehicleNumber);
        if (isEditMode) {
            contacts.set(position, new EmergencyContact(name, phone));
            Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
        } else {
            contacts.add(new EmergencyContact(name, phone));
            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
        }
        dataManager.saveEmergencyContacts(vehicleNumber, contacts);
        finish();
    }
}
