package com.example.vehiclemaintenanceapp;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup themeRadioGroup;
    private RadioButton lightModeRadio, darkModeRadio;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dataManager = new DataManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        lightModeRadio = findViewById(R.id.lightModeRadio);
        darkModeRadio = findViewById(R.id.darkModeRadio);

        if (dataManager.isDarkMode()) {
            darkModeRadio.setChecked(true);
        } else {
            lightModeRadio.setChecked(true);
        }

        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.darkModeRadio) {
                dataManager.saveTheme(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                dataManager.saveTheme(false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        });
    }
}
