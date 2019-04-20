package babbabazrii.com.bababazri.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import io.fabric.sdk.android.Fabric;

import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Common.SessionManagement;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;


public class Splash extends AppCompatActivity {
    ImageView logo;
    private static String TAG = "Splash";
    private Boolean mLocationPermissionsGranted = false;
    RelativeLayout mealLayout;
    Context context;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        logo = (ImageView)findViewById(R.id.logo);
        session = new SessionManagement(getApplicationContext());
        YoYo.with(Techniques.FadeIn)
                .duration(4000)
                .repeat(0)
                .playOn(logo);

        mealLayout= (RelativeLayout) findViewById(R.id.splash_linear_layout);
        mealLayout.setBackgroundResource(R.drawable.background);

//        try{
//            if (SharedPrefManager.getIslagChange(Splash.this)){
//                setLangRecreate(SharedPrefManager.getLangId(RequestCode.LangId));
//            }else {
                Log.d("Lang_key",SharedPrefManager.getLangId(Splash.this,RequestCode.LangId));
                if (SharedPrefManager.getLangId(Splash.this,RequestCode.LangId).compareTo("") != 0){
                    setLangRecreate(SharedPrefManager.getLangId(Splash.this,RequestCode.LangId));
                }else if (Locale.getDefault().getLanguage().compareTo("hi")==0){
                    setLangRecreate("hi");
                }else {
                    setLangRecreate("en");
                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        if (isServicesOK()){
            if (getLocationPermission()){
                    initApp();
            }
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, RequestCode.ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private boolean getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE,
        };

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
       &&ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = false");
                ActivityCompat.requestPermissions(this, permissions, RequestCode.LOCATION_PERMISSION_REQUEST_CODE);
                // Permission Denied
                Toast.makeText(Splash.this, "Some Permission is Denied, please allow permission for that the app can work.", Toast.LENGTH_SHORT)
                        .show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions, RequestCode.LOCATION_PERMISSION_REQUEST_CODE);
                Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = false");
            }
        }
        else
        {
            mLocationPermissionsGranted = true;
            Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = true");
        }
        return mLocationPermissionsGranted;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case RequestCode.LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            finish();
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    initApp();
                    //initialize our map
                }
            }
        }
    }
    private void initApp() {

            int splash_time_out = 4000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!session.isLoggedIn()){
                        Intent intent = new Intent(Splash.this, SelectLanguage.class);
//            Toast.makeText(getApplication(), R.string.login_success, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(Splash.this,Login.class);
//                        Pair[] pairs = new Pair[1];

//                        pairs[0]= new Pair<View,String>(logo,"logoTransition");
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this,pairs);
                        startActivity(intent);
                        finish();
                    }

                }
            }, splash_time_out);
    }
    public void setLangRecreate(String langval) {
        Locale locale;
        Configuration config = getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPrefManager.setLangId(Splash.this,RequestCode.LangId, langval);
    }
}
