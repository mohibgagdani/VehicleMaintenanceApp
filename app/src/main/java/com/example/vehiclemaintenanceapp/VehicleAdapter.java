package com.example.vehiclemaintenanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {
    private List<Vehicle> vehicles;
    private OnVehicleClickListener listener;

    public interface OnVehicleClickListener {
        void onVehicleClick(Vehicle vehicle);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public VehicleAdapter(List<Vehicle> vehicles, OnVehicleClickListener listener) {
        this.vehicles = vehicles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.vehicleType.setText(vehicle.getVehicleType());
        holder.vehicleNumber.setText(vehicle.getVehicleNumber());
        holder.vehicleBrandModel.setText(vehicle.getBrand() + " " + vehicle.getModel());
        holder.itemView.setOnClickListener(v -> listener.onVehicleClick(vehicle));
        holder.editButton.setOnClickListener(v -> listener.onEditClick(position));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleType, vehicleNumber, vehicleBrandModel;
        MaterialButton editButton, deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            vehicleType = itemView.findViewById(R.id.vehicleType);
            vehicleNumber = itemView.findViewById(R.id.vehicleNumber);
            vehicleBrandModel = itemView.findViewById(R.id.vehicleBrandModel);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
