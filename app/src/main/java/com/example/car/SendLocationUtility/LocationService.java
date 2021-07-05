package com.example.car.SendLocationUtility;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.car.ConstantsUtility.Constants;
import com.example.car.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocationService extends Service {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private LocationCallback locationCallback = new LocationCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.now();
                Toast.makeText(LocationService.this, Double.toString(latitude)+","+Double.toString(longitude), Toast.LENGTH_LONG).show();
                //Log.d("LOCATION_UPDATE", latitude + ","+longitude);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Location");
                LocationData locationData = new LocationData(locationResult.getLastLocation().getLongitude() ,
                        locationResult.getLastLocation().getLatitude() ,
                        dateTimeFormatter.format(localDateTime));
                databaseReference.setValue(locationData);

            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("MissingPermission")
    private void startLocationService() {
        String channelId = "location_modification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.mipmap.ic_logo);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("this channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

        LocationRequest locationRequest = new  LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID , builder.build());
    }


    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null)
        {
            String action = intent.getAction();
            if(action != null)
            {
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE))
                {
                    startLocationService();
                }
                else
                {
                    if(action.equals(Constants.ACTION_STOP_LOCATION_SERVICE))
                    {
                        stopLocationService();
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }
}
