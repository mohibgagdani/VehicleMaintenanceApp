package com.example.vehiclemaintenanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<ServiceRecord> records;
    private OnServiceEditListener listener;
    private OnServiceDeleteListener deleteListener;

    public interface OnServiceEditListener {
        void onEditClick(int position);
    }

    public interface OnServiceDeleteListener {
        void onDeleteClick(int position);
    }

    public ServiceAdapter(List<ServiceRecord> records, OnServiceEditListener listener, OnServiceDeleteListener deleteListener) {
        this.records = records;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRecord record = records.get(position);
        holder.serviceType.setText(record.getType());
        holder.serviceDate.setText(record.getDate());
        holder.serviceCost.setText("₹" + record.getCost());
        holder.serviceNotes.setText(record.getNotes());
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(position);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceType, serviceDate, serviceCost, serviceNotes;
        MaterialButton editButton, deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            serviceType = itemView.findViewById(R.id.serviceType);
            serviceDate = itemView.findViewById(R.id.serviceDate);
            serviceCost = itemView.findViewById(R.id.serviceCost);
            serviceNotes = itemView.findViewById(R.id.serviceNotes);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
