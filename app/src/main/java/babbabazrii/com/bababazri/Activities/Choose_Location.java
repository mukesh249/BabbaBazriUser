package babbabazrii.com.bababazri.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import babbabazrii.com.bababazri.Adapters.PlaceAutocompleteAdapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.PlaceInfo;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Choose_Location extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "Choose Location";
    private static String add;

    ImageView currentlocation_refressh;
    AutoCompleteTextView mSearch_et;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    private PlaceInfo mPlace;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double lat_double,log_double;
    TextView textView;
    ProgressDialog progressDialog=null;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__location);
        //mContext = this;

        mSearch_et = (AutoCompleteTextView) findViewById(R.id.input_search_ch);
        toolbar = (Toolbar)findViewById(R.id.toolbar_choose_l);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.choose_location);
        toolbar.setTitleTextColor(Color.BLACK);

        progressDialog = new ProgressDialog(Choose_Location.this);
        requestPremission();


        currentlocation_refressh = (ImageView)findViewById(R.id.currentlocation_refressh);
        textView = (TextView)findViewById(R.id.current_location_tv);

        mSearch_et = (AutoCompleteTextView)findViewById(R.id.input_search);

        currentlocation_refressh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void getDeviceLocation() {
        progressDialog.show();
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
//            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(Choose_Location.this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null){
//                        textView.setText(location.toString());
//                    }
//                }
//            });
            final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(this, new OnCompleteListener<Location>(){
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Log.d(TAG,"onComplete: Found location");
                        Location currentLocation = (Location)task.getResult();

                        SharedPrefManager.getInstance(Choose_Location.this).storeLatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        SharedPreferences sharedPreferencesReg = getSharedPreferences("LatLng", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
//                        editor.putString("LAT", String.valueOf());
//                        editor.putString("LNG", String.valueOf());
//                        editor.commit();
                        getAddress(Double.valueOf(sharedPreferencesReg.getString("LAT","")),Double.valueOf(sharedPreferencesReg.getString("LNG","")));

                        textView.setText(add);

                    }
                    else {
                        Log.d(TAG,"onComplete:  current is location null");
                        Toast.makeText(Choose_Location.this, R.string.location_not_found, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private void requestPremission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
        getDeviceLocation();
        init();
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(Choose_Location.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getFeatureName();
//            add = add + "\n" + obj.getPhone();

            Log.v(TAG, "Address: " + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void init(){
        Log.d(TAG,"init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        mSearch_et.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,
                LAT_LNG_BOUNDS,null);
        mSearch_et.setAdapter(mPlaceAutocompleteAdapter);

        mSearch_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //excuting our method for searching
                    geoLocate();
                }
                return false;
            }
        });
//        imgGps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked gps icon");
//                getDeviceLocation();
//            }
//        });
        SharedPrefManager.getInstance(this).hideSoftKeyBoard(this);
    }
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geoLoating");
        String searchString = mSearch_et.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOexception" + e.getMessage());
        }

        if (list.size()>0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
            //Toast.makeText(getContext(),address.toString(),Toast.LENGTH_LONG).show();
            //moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }


        /*
           -------------------------------google places API autocomplete suggestions
     */

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SharedPrefManager.getInstance(getApplicationContext()).hideSoftKeyBoard(Choose_Location.this);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient,placeId);
            placeResult.setResultCallback(mUpdatePlaceDitailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDitailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()){
                Log.d(TAG,"onResult: place query did not complete successfully."+ places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {
                mPlace = new PlaceInfo();
                mPlace.setAddress(place.getAddress().toString());
//                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setName(place.getName().toString());
                mPlace.setRating(place.getRating());
                mPlace.setWebsiteuri(place.getWebsiteUri());

                Log.d(TAG,"OnResult: place details " + mPlace.toString());

            }catch (NullPointerException e){
                Log.d(TAG,"OnResult: NullPointerException " + e.getMessage());
            }

            LatLng latlng = mPlace.getLatLng();
            String lat = String.valueOf(latlng.latitude);
            String lng = String.valueOf(latlng.longitude);

            Log.d(TAG,"OnResult: place lat lng details " + lat + ","+ lng);
            SharedPrefManager.getInstance(Choose_Location.this).storeAddress(mPlace.getName());
            SharedPrefManager.getInstance(Choose_Location.this).storeLatLngNearBy(latlng.latitude,latlng.longitude);

            moveToItemNearBy(mPlace.getLatLng(),mPlace);
            places.release();
        }
    };


    private void moveToItemNearBy(LatLng latLng,PlaceInfo placeInfo){
        User_Profile.getInstance().ReloadMethod();
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Choose_Location.this)) {
            if (SharedPrefManager.getLangId(Choose_Location.this, RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Choose_Location.this,RequestCode.LangId));
            } else {
                Toast.makeText(Choose_Location.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
            }
        }
    }
    public void setLangRecreate(String langval) {
        Locale locale;
        Configuration config = getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPrefManager.setLangId(Choose_Location.this,RequestCode.LangId, langval);
    }
}
