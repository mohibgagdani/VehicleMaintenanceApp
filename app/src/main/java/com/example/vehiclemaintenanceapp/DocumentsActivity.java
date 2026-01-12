package com.example.vehiclemaintenanceapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DocumentsActivity extends AppCompatActivity {
    private TextInputEditText rcIssueDateInput, rcExpiryDateInput;
    private TextInputEditText pucIssueDateInput, pucExpiryDateInput;
    private TextInputEditText insuranceCompanyInput, policyNumberInput, insuranceExpiryDateInput;
    private DataManager dataManager;
    private String vehicleNumber;
    private Calendar calendar;
    private int selectedField = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        dataManager = new DataManager(this);
        calendar = Calendar.getInstance();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        rcIssueDateInput = findViewById(R.id.rcIssueDateInput);
        rcExpiryDateInput = findViewById(R.id.rcExpiryDateInput);
        pucIssueDateInput = findViewById(R.id.pucIssueDateInput);
        pucExpiryDateInput = findViewById(R.id.pucExpiryDateInput);
        insuranceCompanyInput = findViewById(R.id.insuranceCompanyInput);
        policyNumberInput = findViewById(R.id.policyNumberInput);
        insuranceExpiryDateInput = findViewById(R.id.insuranceExpiryDateInput);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        MaterialButton viewRemindersButton = findViewById(R.id.viewRemindersButton);

        loadData();

        rcIssueDateInput.setOnClickListener(v -> { selectedField = 1; showDatePicker(); });
        rcExpiryDateInput.setOnClickListener(v -> { selectedField = 2; showDatePicker(); });
        pucIssueDateInput.setOnClickListener(v -> { selectedField = 3; showDatePicker(); });
        pucExpiryDateInput.setOnClickListener(v -> { selectedField = 4; showDatePicker(); });
        insuranceExpiryDateInput.setOnClickListener(v -> { selectedField = 5; showDatePicker(); });

        saveButton.setOnClickListener(v -> saveData());
        viewRemindersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DocumentRemindersActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });
    }

    private void loadData() {
        rcIssueDateInput.setText(dataManager.getDocumentData(vehicleNumber, "rcIssueDate"));
        rcExpiryDateInput.setText(dataManager.getDocumentData(vehicleNumber, "rcExpiryDate"));
        pucIssueDateInput.setText(dataManager.getDocumentData(vehicleNumber, "pucIssueDate"));
        pucExpiryDateInput.setText(dataManager.getDocumentData(vehicleNumber, "pucExpiryDate"));
        insuranceCompanyInput.setText(dataManager.getDocumentData(vehicleNumber, "insuranceCompany"));
        policyNumberInput.setText(dataManager.getDocumentData(vehicleNumber, "policyNumber"));
        insuranceExpiryDateInput.setText(dataManager.getDocumentData(vehicleNumber, "insuranceExpiryDate"));
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String date = sdf.format(calendar.getTime());
            
            switch (selectedField) {
                case 1: rcIssueDateInput.setText(date); break;
                case 2: rcExpiryDateInput.setText(date); break;
                case 3: pucIssueDateInput.setText(date); break;
                case 4: pucExpiryDateInput.setText(date); break;
                case 5: insuranceExpiryDateInput.setText(date); break;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveData() {
        dataManager.saveDocumentData(vehicleNumber, "rcIssueDate", rcIssueDateInput.getText().toString());
        dataManager.saveDocumentData(vehicleNumber, "rcExpiryDate", rcExpiryDateInput.getText().toString());
        dataManager.saveDocumentData(vehicleNumber, "pucIssueDate", pucIssueDateInput.getText().toString());
        dataManager.saveDocumentData(vehicleNumber, "pucExpiryDate", pucExpiryDateInput.getText().toString());
        dataManager.saveDocumentData(vehicleNumber, "insuranceCompany", insuranceCompanyInput.getText().toString());
        dataManager.saveDocumentData(vehicleNumber, "policyNumber", policyNumberInput.getText().toString());
        dataManager.saveDocumentData(vehicleNumber, "insuranceExpiryDate", insuranceExpiryDateInput.getText().toString());

        Toast.makeText(this, "Document details saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
