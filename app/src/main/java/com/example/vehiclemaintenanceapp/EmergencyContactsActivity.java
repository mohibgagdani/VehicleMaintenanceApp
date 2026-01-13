package com.example.vehiclemaintenanceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class EmergencyContactsActivity extends AppCompatActivity {
    private static final int CALL_PERMISSION_REQUEST = 100;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private DataManager dataManager;
    private String vehicleNumber;
    private View emptyView;
    private String pendingCallNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        vehicleNumber = getIntent().getStringExtra("vehicleNumber");
        dataManager = new DataManager(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.contactsRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        MaterialButton addContactButton = findViewById(R.id.addContactButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("vehicleNumber", vehicleNumber);
            startActivity(intent);
        });

        loadContacts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    private void loadContacts() {
        List<EmergencyContact> contacts = dataManager.getEmergencyContacts(vehicleNumber);
        if (contacts.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ContactAdapter(contacts, new ContactAdapter.ContactListener() {
                @Override
                public void onCallClick(String phone) {
                    makeCall(phone);
                }

                @Override
                public void onEditClick(int position) {
                    Intent intent = new Intent(EmergencyContactsActivity.this, AddContactActivity.class);
                    intent.putExtra("vehicleNumber", vehicleNumber);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(int position) {
                    contacts.remove(position);
                    dataManager.saveEmergencyContacts(vehicleNumber, contacts);
                    loadContacts();
                    Toast.makeText(EmergencyContactsActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void makeCall(String phone) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) 
                != PackageManager.PERMISSION_GRANTED) {
            pendingCallNumber = phone;
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingCallNumber != null) {
                    makeCall(pendingCallNumber);
                    pendingCallNumber = null;
                }
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
