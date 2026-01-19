package com.example.vehiclemaintenanceapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServiceHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private DataManager dataManager;
    private String vehicleNumber;
    private View emptyView;
    private TextView monthlyTotalText, monthYearText;
    private Calendar selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        dataManager = new DataManager(this);
        selectedMonth = Calendar.getInstance();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.serviceRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        monthlyTotalText = findViewById(R.id.monthlyTotalText);
        monthYearText = findViewById(R.id.monthYearText);
        LinearLayout monthSelectorLayout = findViewById(R.id.monthSelectorLayout);
        MaterialButton addServiceButton = findViewById(R.id.addServiceButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addServiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddServiceActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });

        monthSelectorLayout.setOnClickListener(v -> showMonthPicker());

        loadServices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServices();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadServices();
    }

    private void loadServices() {
        List<ServiceRecord> records = dataManager.getServiceRecords(vehicleNumber);
        if (records.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ServiceAdapter(records, position -> {
                Intent intent = new Intent(this, AddServiceActivity.class);
                intent.putExtra("vehicleNumber", vehicleNumber);
                intent.putExtra("position", position);
                startActivity(intent);
            }, position -> deleteService(position));
            recyclerView.setAdapter(adapter);
        }

        calculateMonthlyTotal(records);
    }

    private void calculateMonthlyTotal(List<ServiceRecord> records) {
        double monthlyTotal = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        monthYearText.setText(monthYearFormat.format(selectedMonth.getTime()));

        for (ServiceRecord record : records) {
            try {
                Date date = sdf.parse(record.getDate());
                Calendar recordCal = Calendar.getInstance();
                recordCal.setTime(date);
                if (recordCal.get(Calendar.MONTH) == selectedMonth.get(Calendar.MONTH) &&
                    recordCal.get(Calendar.YEAR) == selectedMonth.get(Calendar.YEAR)) {
                    monthlyTotal += Double.parseDouble(record.getCost());
                }
            } catch (ParseException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        monthlyTotalText.setText("₹" + String.format(Locale.getDefault(), "%.2f", monthlyTotal));
    }

    private void showMonthPicker() {
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedMonth.set(Calendar.YEAR, year);
            selectedMonth.set(Calendar.MONTH, month);
            loadServices();
        }, selectedMonth.get(Calendar.YEAR), selectedMonth.get(Calendar.MONTH), 1);
        picker.show();
    }

    private void deleteService(int position) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Service")
            .setMessage("Are you sure you want to delete this service record?")
            .setPositiveButton("Delete", (dialog, which) -> {
                List<ServiceRecord> records = dataManager.getServiceRecords(vehicleNumber);
                records.remove(position);
                dataManager.saveServiceRecords(vehicleNumber, records);
                Toast.makeText(this, "Service deleted", Toast.LENGTH_SHORT).show();
                loadServices();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}
