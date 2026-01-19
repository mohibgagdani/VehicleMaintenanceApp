package com.example.vehiclemaintenanceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class FuelTrackerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FuelAdapter adapter;
    private DataManager dataManager;
    private String vehicleNumber;
    private View emptyView;
    private TextView totalCostText, monthlyCostText, monthYearText;
    private Calendar selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_tracker);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        dataManager = new DataManager(this);
        selectedMonth = Calendar.getInstance();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.fuelRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        totalCostText = findViewById(R.id.totalCostText);
        monthlyCostText = findViewById(R.id.monthlyCostText);
        monthYearText = findViewById(R.id.monthYearText);
        LinearLayout monthSelectorLayout = findViewById(R.id.monthSelectorLayout);
        MaterialButton addFuelButton = findViewById(R.id.addFuelButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addFuelButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFuelActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });

        monthSelectorLayout.setOnClickListener(v -> showMonthPicker());

        loadFuelRecords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFuelRecords();
    }

    private void loadFuelRecords() {
        List<FuelRecord> records = dataManager.getFuelRecords(vehicleNumber);
        if (records.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new FuelAdapter(records, position -> {
                Intent intent = new Intent(this, AddFuelActivity.class);
                intent.putExtra("vehicleNumber", vehicleNumber);
                intent.putExtra("position", position);
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }

        double total = 0;
        double monthly = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        monthYearText.setText(monthYearFormat.format(selectedMonth.getTime()));

        for (FuelRecord record : records) {
            double cost = Double.parseDouble(record.getCost());
            total += cost;

            try {
                Date date = sdf.parse(record.getDate());
                Calendar recordCal = Calendar.getInstance();
                recordCal.setTime(date);
                if (recordCal.get(Calendar.MONTH) == selectedMonth.get(Calendar.MONTH) &&
                    recordCal.get(Calendar.YEAR) == selectedMonth.get(Calendar.YEAR)) {
                    monthly += cost;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        totalCostText.setText("₹" + String.format(Locale.getDefault(), "%.2f", total));
        monthlyCostText.setText("₹" + String.format(Locale.getDefault(), "%.2f", monthly));
    }

    private void showMonthPicker() {
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedMonth.set(Calendar.YEAR, year);
            selectedMonth.set(Calendar.MONTH, month);
            loadFuelRecords();
        }, selectedMonth.get(Calendar.YEAR), selectedMonth.get(Calendar.MONTH), 1);
        picker.show();
    }
}
