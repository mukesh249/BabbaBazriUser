package babbabazrii.com.bababazri.Service;

/**
 * Created by Advosoft2 on 2/21/2018.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import babbabazrii.com.bababazri.Activities.Tracking;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class LocationTracker extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0f;
    private Context context;
    String req_id="5b35fc689576f8008cc1519e";

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }


        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            SharedPrefManager.setDouble(RequestCode.SP_NEW_LAT, (location.getLatitude()));
            SharedPrefManager.setDouble(RequestCode.SP_NEW_LONG, (location.getLongitude()));
            Log.e("location", location.toString());
///*
//            JSONObject param = new JSONObject();
//            JSONObject locationObject = new JSONObject();
//
//            try {
//                locationObject.put("lat", location.getLatitude());
//                locationObject.put("lng", location.getLongitude());
//
//                param.put("location", locationObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }*/
            SharedPrefManager.setSharedPrefString(RequestCode.SP_DriverStatus, "start");
            if (!TextUtils.isEmpty(SharedPrefManager.getSharedPrefString(RequestCode.SP_DriverStatus))) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Locations").child(req_id);
                Long tsLong = System.currentTimeMillis() / 1000;
                Map<String, Object> jsonParams = new ArrayMap<>();
                jsonParams.put("currntLatt", location.getLatitude());
                jsonParams.put("currntLong", location.getLongitude());
                jsonParams.put("jobStatus", "start");
                jsonParams.put("timestamp",tsLong);
                mDatabase.updateChildren(jsonParams);

              //  mDatabase.child(MyApplication.getSharedPrefString(Constants.SP_CURRENT_JOB)).child("currntLatt").setValue(location.getLatitude());
               // mDatabase.child(MyApplication.getSharedPrefString(Constants.SP_CURRENT_JOB)).child("currntLong").setValue(location.getLongitude());
            }


            Intent in = new Intent("locationUpdated");
            in.putExtra("location", location);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);


////        updateNotification("Lat: " + g╥etLatitude() + " Long: " + getLongitude());
//
//           /* ApiService.getInstance().makePostCallAuth(context, ApiService.UPDATE_DRIVER_LOCATION, param, new ApiService.OnResponse() {
//                @Override
//                public void onResponseSuccess(JSONObject response) {
////                    MyApplication.showToast(context, "Location Updated");
//                }
//
//                @Override
//                public void onError(VolleyError volleyError) {
//                    MyApplication.showToast(context, "Update Location Failed");
//                }
//            });*/

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean finish = intent.getBooleanExtra("finish", false);
        if (finish) {
            stopForeground(true);
            stopSelf();
        }
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        context = getApplicationContext();
        Intent playIntent = new Intent(getApplicationContext(), Tracking.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                playIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name) + " is running")
                .setContentText(getResources().getString(R.string.app_name) + " is running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(1001,
                notification);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}