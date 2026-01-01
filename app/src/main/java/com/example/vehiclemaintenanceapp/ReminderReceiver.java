package com.example.vehiclemaintenanceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "vehicle_maintenance_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String vehicleNumber = intent.getStringExtra("vehicleNumber");
        String type = intent.getStringExtra("type");
        
        createNotificationChannel(context);
        
        String title = "";
        String message = "";
        
        if ("service".equals(type)) {
            title = "Service Reminder";
            message = "Service due for vehicle " + vehicleNumber;
        } else if ("document".equals(type)) {
            String docType = intent.getStringExtra("docType");
            title = docType + " Expiry Reminder";
            message = docType + " for vehicle " + vehicleNumber + " is expiring in 7 days";
        }
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        
        NotificationManager notificationManager = 
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if (notificationManager != null) {
            notificationManager.notify(vehicleNumber.hashCode(), builder.build());
        }
    }
    
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Vehicle Maintenance";
            String description = "Reminders for vehicle maintenance";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            
            NotificationManager notificationManager = 
                    context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
