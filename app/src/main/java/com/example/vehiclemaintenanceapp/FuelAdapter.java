package com.example.vehiclemaintenanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.ViewHolder> {
    private List<FuelRecord> records;
    private OnFuelEditListener listener;

    public interface OnFuelEditListener {
        void onEditClick(int position);
    }

    public FuelAdapter(List<FuelRecord> records, OnFuelEditListener listener) {
        this.records = records;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fuel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FuelRecord record = records.get(position);
        holder.fuelDate.setText(record.getDate());
        holder.fuelCost.setText("₹" + record.getCost());
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fuelDate, fuelCost;
        MaterialButton editButton;

        ViewHolder(View itemView) {
            super(itemView);
            fuelDate = itemView.findViewById(R.id.fuelDate);
            fuelCost = itemView.findViewById(R.id.fuelCost);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
