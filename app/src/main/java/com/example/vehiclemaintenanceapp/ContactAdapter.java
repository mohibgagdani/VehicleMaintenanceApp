package com.example.vehiclemaintenanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<EmergencyContact> contacts;
    private ContactListener listener;

    public interface ContactListener {
        void onCallClick(String phone);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public ContactAdapter(List<EmergencyContact> contacts, ContactListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyContact contact = contacts.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhone());
        holder.editButton.setOnClickListener(v -> listener.onEditClick(position));
        holder.callButton.setOnClickListener(v -> listener.onCallClick(contact.getPhone()));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone;
        MaterialButton editButton, callButton, deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            editButton = itemView.findViewById(R.id.editButton);
            callButton = itemView.findViewById(R.id.callButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
