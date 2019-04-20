package babbabazrii.com.bababazri.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.geofire.GeoFire;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.AppStateMonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import babbabazrii.com.bababazri.Api.ApiConfig;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Common.DirectionsJSONParser;
import babbabazrii.com.bababazri.Common.ParserTask;
import babbabazrii.com.bababazri.Common.SessionManagement;
import babbabazrii.com.bababazri.Common.SubString;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.RequestListWapper;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Tracking extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        WebCompleteTask
        , ParserTask.ParserCallback
//        ,GoogleApiClient.ConnectionCallbacks,
//         LocationListener
{

    //Play Services
    private static final int MY_PRESMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;
    private static final int UPDATE_INTERVAL = 5000;
    private static final int FATEST_INTERVAL = 3000;
    private static final int DISPLACEMENT = 10;
    private static final int LOCATION_REQUEST = 1212;
    private static final String TAG = "Tracking";
    public static String src_string, des_string, orderRequestId, duration, price;
    static private Marker carMarker;
    static private GoogleMap tmMap;
    Marker mCurrent;
    MaterialAnimatedSwitch location_switch;
    LatLng source, destination_n, src_latlng, update_src;
    int counter = 2;
    String req_id = "5b35fc689576f8008cc1519e";
    DatabaseReference drivers;
    GeoFire geoFire;
    SupportMapFragment mapFragment;
    RequestListWapper feed;
    TextView driver_name, driver_mobile, trucknumber, vehicle_name, driver_rating, time_left, truck_no_lastFour, matrial_name;
    CircleImageView driver_profile;
    ImageView driver_call, vehicle_img;
    Toolbar toolbar_tracking;
    HashMap<String, String> params;
    int vehicle_icon;
    SessionManagement session;
    Handler h = new Handler();
    int delay = 30 * 1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;
    List points = null;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private EditText editPlace;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //Car animation
    private List<LatLng> polyLineList = new ArrayList<>();
    private ArrayList markerPoints = new ArrayList();
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition, currentPosition;
    private int index, next, vehicle_image;
    private List<HashMap<String, String>> routePoints;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private AppStateMonitor appStateMonitor;


//    Runnable drawPathRunnable = new Runnable() {
//        @Override
//        public void run() {
//           if (index < polyLineList.size()-1){
//               index++;
//               next = index + 1;
//           }
//           if (index < polyLineList.size()-1){
//               startPosition = polyLineList.get(index);
//               endPosition = polyLineList.get(next);
//           }
//           final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,16);
//           valueAnimator.setDuration(2000);
//           valueAnimator.setInterpolator(new LinearInterpolator());
//           valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//               @Override
//               public void onAnimationUpdate(ValueAnimator valueAnimator1) {
//                   v = valueAnimator.getAnimatedFraction();
//                   lng = v*endPosition.longitude+(1-v)*startPosition.longitude;
//                   lat = v*endPosition.latitude + (1-v)*startPosition.latitude;
//                   LatLng newPos = new LatLng(lat,lng);
//                   carMarker.setPosition(newPos);
//                   carMarker.setAnchor(0.5f,0.5f);
//                   carMarker.setRotation(getBearing(startPosition,newPos));
////                   tmMap.moveCamera(CameraUpdateFactory.newCameraPosition(
////                           new CameraPosition.Builder()
////                                   .target(newPos)
////                                   .zoom(15.5f)
////                                   .build()
////                   ));
//               }
//           });
//           valueAnimator.start();
//           handler.postDelayed(this,3000);
//        }
//    };

//    BroadcastReceiver locationUpdated = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Location newLocation = intent.getParcelableExtra("location");
//
//            String lat = String.valueOf(newLocation.getLatitude());
//            String lng = String.valueOf(newLocation.getLongitude());
//            if (!SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LAT).equalsIgnoreCase(lat) ||
//                    !SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LONG).equalsIgnoreCase(lng)) {
//
//                Location location = new Location("");
//                location.setLatitude(Double.parseDouble(lat));
//                location.setLongitude(Double.parseDouble(lng));
//
//                if (TextUtils.isEmpty(SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LAT))) {
//                    /*location.setBearing(bearingBetweenLocations(new LatLng(model.getSource().getLocation().getLat(), (model.getSource().getLocation().getLng())),
//                            new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));*/
//                    location.setBearing(getBearing(new LatLng(26.805085, 75.812576),
//                            new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
//                } else {
//                    location.setBearing(getBearing(new LatLng(Double.parseDouble(SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LAT)), Double.parseDouble(SharedPrefManager.getSharedPrefString(RequestCode .SP_CURRENT_LONG))),
//                            new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
//                }
////                animateMarker(location, carMarker);
//                //Animation
////                ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0,100);
////                polyLineAnimator.setDuration(2000);
////                polyLineAnimator.setInterpolator(new LinearInterpolator());
////                polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
////                    @Override
////                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
////                        List<LatLng> points = greyPolyline.getPoints();
////                        int precentValue = (int)valueAnimator.getAnimatedValue();
////                        int size = points.size();
////                        int newPoints = (int)(size * (precentValue/100.0f));
////                        List<LatLng> p = points.subList(0,newPoints);
////                        blackPolyline.setPoints(p);
////                    }
////                });
////                polyLineAnimator.start();
//
//                SharedPrefManager.setSharedPrefString(RequestCode.SP_CURRENT_LAT, lat);
//                SharedPrefManager.setSharedPrefString(RequestCode.SP_CURRENT_LONG, lng);
//            }
//        }
//    };

//    void animateMarker(final Location destination, final Marker marker) {
//        if (marker != null) {
//            final LatLng startPosition = marker.getPosition();
//            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
//
//            final float startRotation = marker.getRotation();
//
//            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
//            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 0.5f);
//            valueAnimator.setDuration(1000);
//            valueAnimator.setInterpolator(new LinearInterpolator());
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    try {
//                        float v = animation.getAnimatedFraction();
//                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
//                        marker.setPosition(newPosition);
//                        marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
//
//                    } catch (Exception ex) {
//                    }
//                }
//
//            });
//            valueAnimator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18.0f));
////                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18.0f));
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//
//            valueAnimator.start();
//            counter++;
//        }
//    }

    private Polyline blackPolyline, greyPolyline;
    private Polyline polyline;
    private ApiConfig mService;
    Integer dis;

    public static float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);
        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        ButterKnife.bind(this);

//        startService(new Intent(Tracking.this, LocationTracker.class));
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track);
        mapFragment.getMapAsync(Tracking.this);
        feed = (RequestListWapper) getIntent().getSerializableExtra("data");
        driver_name = (TextView) findViewById(R.id.driver_name);
        driver_mobile = (TextView) findViewById(R.id.driver_no);
        trucknumber = (TextView) findViewById(R.id.truck_number);
        vehicle_name = (TextView) findViewById(R.id.vehicle_name);
        driver_profile = (CircleImageView) findViewById(R.id.driver_profile);
        driver_rating = (TextView) findViewById(R.id.driver_rating);
        time_left = (TextView) findViewById(R.id.driver_time_left);
        driver_call = (ImageView) findViewById(R.id.call_img);
        vehicle_img = (ImageView) findViewById(R.id.vehicle_img);
        truck_no_lastFour = (TextView) findViewById(R.id.truck_no);
        matrial_name = (TextView) findViewById(R.id.matrial_name_tracking);
        SubString subEx = new SubString();
        //  session = new SessionManagement(Tracking.this);
        try {
            String last4Digits = subEx.getLastnCharacters(feed.getTruckNumber().toString(), 4);
            String last4DigitsRemove = subEx.removeLastnCharacters(feed.getTruckNumber().toString(), 4);
            trucknumber.setText(last4DigitsRemove);
            truck_no_lastFour.setText(last4Digits);
            driver_name.setText(feed.getDriver_fullname());
            driver_mobile.setText(feed.getDriver_mobile());
            vehicle_name.setText(feed.getVehicleTpye_name());
            matrial_name.setText(feed.getMaterial_name() + " : " + feed.getMaterialType_name());
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo))
                    .load(WebUrls.BASE_URL + feed.getDriver_profile())
                    .into(driver_profile);
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo))
                    .load(WebUrls.BASE_URL + feed.getVehicleTpye_image())
                    .into(vehicle_img);
            float from_value;
            try {
                from_value = Float.parseFloat(feed.getRating());
            } catch (NumberFormatException ex) {
                from_value = 0.0f; // default ??
            }
            driver_rating.setText(from_value + "");


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//        // Callback sample
//        appStateMonitor = AppStateMonitor.create(getApplication());
//        appStateMonitor.addListener(new SampleAppStateListener());
//        appStateMonitor.start();

        src_string = feed.getP_formatedAddress().toString();
        des_string = feed.getC_formatedAddress().toString();
        price = feed.getPrice().toString();
        toolbar_tracking = (Toolbar) findViewById(R.id.toolbar_tracking);
        setSupportActionBar(toolbar_tracking);
        driver_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_action();
            }
        });
        if (feed.getVehicleTypeId().equals("5b84d2cb4921e87a9447a85c")) {
            vehicle_image = R.drawable.truck_map;
        } else if (feed.getVehicleTypeId().equals("5b84d2cb4921e87a9447a85d")) {
            vehicle_image = R.drawable.dumper_map;
        } else if (feed.getVehicleTypeId().equals("5b84d2cb4921e87a9447a85e")) {
            vehicle_image = R.drawable.trolla_map;
        } else if (feed.getVehicleTypeId().equals("5b84d2cb4921e87a9447a85f")) {
            vehicle_image = R.drawable.pick_up_map;
        } else if (feed.getVehicleTypeId().equals("5b84d2cb4921e87a9447a860")) {
            vehicle_image = R.drawable.tractor_trolly_map;
        }
        try {
            JSONObject object = new JSONObject(feed.getSrc_location());
            JSONObject des_obj = new JSONObject(feed.getLatlng());

            source = new LatLng(object.optDouble("lat"), object.optDouble("lng"));
//            src_latlng = source;
            destination_n = new LatLng(des_obj.optDouble("lat"), des_obj.optDouble("lng"));
            //animateMarker(location, carMarker);
        } catch (Exception e) {

        }

        //-----------------------------------Get Driver Location LatLng Position from Firebase Database------------------------

        Log.d(TAG, "Order id:" + feed.getId());
        orderRequestId = feed.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Locations").child(feed.getId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && orderRequestId.equals(feed.getId())) {
                    try {
                        Map<String, String> value1 = (Map<String, String>) dataSnapshot.getValue();
                        Log.i("dataSnapshot json", "dataSnapshot" + new JSONObject(value1));
                        JSONObject jsonObject = new JSONObject(value1);
                        Log.d("jsondata", jsonObject + "");
                        if (jsonObject.optString("jobStatus").compareToIgnoreCase("Pending") == 0) {
                            Toast.makeText(Tracking.this, "product not dispatched yet", Toast.LENGTH_LONG).show();

                        } else if (jsonObject.optString("jobStatus").compareToIgnoreCase("start") == 0) {

                            Log.d(TAG, "Start Trip but AnimateMaker NOt Call");
                            String lat = String.valueOf(jsonObject.optString("currntLatt"));
                            String lng = String.valueOf(jsonObject.optString("currntLong"));
                            update_src = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            if (!SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LAT).equals(lat) ||
                                    !SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LONG).equals(lng)) {
                                Location location = new Location("");
                                location.setLatitude(Double.valueOf(lat));
                                location.setLongitude(Double.valueOf(lng));

                                if (TextUtils.isEmpty(SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LAT))) {
                                    location.setBearing(getBearing(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)),
                                            new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
                                } else {

                                    location.setBearing(getBearing(
                                            new LatLng(Double.parseDouble(SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LAT)),
                                                    Double.parseDouble(SharedPrefManager.getSharedPrefString(RequestCode.SP_CURRENT_LONG))),
                                            new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
                                }

                                animateMarker(location, carMarker);
                                Log.d(TAG, "AnimateMaker Call");
                                SharedPrefManager.setSharedPrefString(RequestCode.SP_CURRENT_LAT, lat);
                                SharedPrefManager.setSharedPrefString(RequestCode.SP_CURRENT_LONG, lng);
                            } else {
                                SharedPrefManager.setSharedPrefString(RequestCode.SP_CURRENT_LAT, lat);
                                SharedPrefManager.setSharedPrefString(RequestCode.SP_CURRENT_LONG, lng);
                            }

                        }
                        else if (jsonObject.optString("jobStatus").compareTo("end") == 0) {
                            // Callback sample
                            appStateMonitor = AppStateMonitor.create(getApplication());
                            appStateMonitor.addListener(new SampleAppStateListener());
                            appStateMonitor.start();
                        }
                    } catch (Exception e) {
                        Log.d("exception...", e.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //  mService = WebUrls.getGoogleAPI();

        //toolbar back button color and icon change
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_black);
        upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        //start handler as activity become visible

        h.postDelayed(runnable = new Runnable() {
            public void run() {
                timeCalculate();
                getDirection();
                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        tmMap.clear();
        //  getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
        tmMap = null;
        finish();
        // onBackPressed();
        return true;
    }

    @Override
    public void onParserResult(List<List<HashMap<String, String>>> result, DirectionsJSONParser parser) {
        for (int i = 0; i < parser.getRouteList().size(); i++) {
            /*if (parser.getRouteList().get(i).getSelectedRoute().equalsIgnoreCase(model.getSelectedRoute())) {
                routePoints = result.get(i);
                showRoute(routePoints);
            }*/
            routePoints = result.get(i);
            Log.d("routePoints", routePoints + "");
            showRoute(routePoints);
        }
    }

    void animateMarker(final Location destination, final Marker marker) {

        if (carMarker != null && tmMap != null) {
//            //  mapFragment.getMapAsync(Tracking.this);
//           /* LatLng startPosition = carMarker.getPosition();
//            LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
//
//            float startRotation = marker.getRotation();
//            LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
//            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 0.15f);
//            valueAnimator.setDuration(1000);
//            valueAnimator.setInterpolator(new LinearInterpolator());
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    try {
//
//                        float v = animation.getAnimatedFraction();
//                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
////                        Log.d("newpos", newPosition + "");
//
//                        carMarker.setPosition(newPosition);
//                        carMarker.setRotation(getBearing(startPosition,new LatLng(destination.getLatitude(),destination.getLongitude())));
////                        carMarker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
//
//
//                    } catch (Exception ex) {
//
//                    }
//                }
//            });
//            valueAnimator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    try {
////                        Log.d("markr pos", marker.getPosition() + "");
////                        Toast.makeText(Tracking.this, "camra update", Toast.LENGTH_SHORT).show();
//                        // CameraUpdate center = CameraUpdateFactory.newLatLng(carMarker.getPosition());
//                        //  CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
//
//
//                        tmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(carMarker.getPosition(), 18.0f));
//                    } catch (Exception e) {
//                        Log.d("exception....", e.toString());
//                    }
//
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//
//            valueAnimator.start();*/

            /*new code*/

            final LatLng startPosition = carMarker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());


            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 0.15f);
            valueAnimator.setDuration(2000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float v = valueAnimator.getAnimatedFraction();
                    double lng = v * endPosition.longitude + (1 - v)
                            * startPosition.longitude;
                    double lat = v * endPosition.latitude + (1 - v)
                            * startPosition.latitude;
                    LatLng newPos = new LatLng(lat, lng);
                    Location location1 = new Location("");
                    location1.setLatitude(startPosition.latitude);
                    location1.setLongitude(startPosition.longitude);


                    Location location2 = new Location("");
                    location2.setLatitude(newPos.latitude);
                    location2.setLongitude(newPos.longitude);
                    float bear = location1.bearingTo(location2);
                    Log.d("distance", location1.distanceTo(location2) + "");
                    Log.d("accuracy", location2.getAccuracy() + "");
                    Log.d("total", location1.distanceTo(location2) - location2.getAccuracy() + "");
                    Log.d("update_bearing", bear + "");

                    if (location1.distanceTo(location2) - location2.getAccuracy() > 5) {
                        carMarker.setPosition(newPos);
                        carMarker.setAnchor(0.5f, 0.5f);
                        Log.d("update_bearing", bear + "");
                        carMarker.setRotation(bear);

                        if (tmMap != null) {
//                            tmMap.animateCamera(CameraUpdateFactory
//                                    .newCameraPosition
//                                            (new CameraPosition.Builder()
//                                                    .target(newPos)
////                                                    .zoom(15.5f)
//                                                    .build()));

//                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                            builder.include(newPos);
//                            builder.include(destination_n);
//                            LatLngBounds bounds = builder.build();
//                            int width = getResources().getDisplayMetrics().widthPixels;
//                            int height = getResources().getDisplayMetrics().heightPixels;
//                            int padding = (int) (width * 0.0);
//                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,height,width,  50);
//
//                            //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(newPos, 50);
//                            tmMap.animateCamera(cu);

                        }
                    }

                }
            });
            valueAnimator.start();
        } else {
//            Toast.makeText(Tracking.this, "map null", Toast.LENGTH_SHORT).show();
        }
    }

// /*   private float getBearing(LatLng startPosition, LatLng endPosition) {
//        double PI = 3.14159;
//        double lat1 = startPosition.latitude * PI / 180;
//        double long1 = startPosition.longitude * PI / 180;
//        double lat2 = endPosition.latitude * PI / 180;
//        double long2 = endPosition.longitude * PI / 180;
//
//        double dLon = (long2 - long1);
//
//        double y = Math.sin(dLon) * Math.cos(lat2);
//        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
//                * Math.cos(lat2) * Math.cos(dLon);
//
//        Double brng = Math.atan2(y, x);
//
//        brng = Math.toDegrees(brng);
//        brng = (brng + 360) % 360;
//
//        return brng.floatValue();
//    }*/

    float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }
        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

    private void getDirection() {
        String requestApi = SharedPrefManager.getDirectionsUrl(update_src, destination_n);
        Log.d(TAG, "getDirection requestApi: " + requestApi);
        HashMap objectNew = new HashMap();
        new WebTask(this, requestApi, objectNew, Tracking.this, RequestCode.CODE_Direction_Api, 3);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");

        try {
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.my_map_style));
            if (!isSuccess) {
                Log.e("ERROR", "Map style load failed!!!!");
            }
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }

        tmMap = googleMap;
//        tmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 18.0f));
        tmMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //tmMap.setOnCameraIdleListener(this);

//        if (markerPoints.size() > 1) {
//            markerPoints.clear();
//            tmMap.clear();
//        }
//
//        // Adding new item to the ArrayList
//        markerPoints.add(source);
//        markerPoints.add(destination_n);
//
//        // Creating MarkerOptions
//        MarkerOptions options = new MarkerOptions();
//
//        // Setting the position of the marker
//        options.position(source);
//        options.position(destination_n);
//
//        if (markerPoints.size() == 1) {
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        } else if (markerPoints.size() == 2) {
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Locations").child(feed.getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Map<String, String> value1 = (Map<String, String>) dataSnapshot.getValue();
                    Log.i("dataSnapshot json", "dataSnapshot" + new JSONObject(value1));
                    JSONObject jsonObject = new JSONObject(value1);
                    Log.d("jsondata", jsonObject + "");
                    if (jsonObject.optString("jobStatus").compareToIgnoreCase("Pending") == 0) {
                        Toast.makeText(Tracking.this, R.string.not_dispatched, Toast.LENGTH_LONG).show();

                    } else if (jsonObject.optString("jobStatus").compareToIgnoreCase("start") == 0) {

                        Log.d(TAG, "onMapReady _dataSnapshot");
                        String lat = String.valueOf(jsonObject.optString("currntLatt"));
                        String lng = String.valueOf(jsonObject.optString("currntLong"));
                        update_src = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                        carMarker = tmMap.addMarker(new MarkerOptions()
                                .position(update_src)
                                .flat(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tracking_vehicle))
                        );

//                        tmMap.animateCamera(CameraUpdateFactory
//                                .newCameraPosition
//                                        (new CameraPosition.Builder()
//                                                .target(update_src)
//                                                .zoom(12.5f)
//                                                .build()));

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(update_src);
                        builder.include(destination_n);
                        LatLngBounds bounds = builder.build();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.17);
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, height, width, padding);
                        tmMap.animateCamera(cu);

                        timeCalculate();
                        getDirection();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tmMap.addMarker(new MarkerOptions()
                        .position(destination_n)
//                .flat(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );
//        tmMap.addMarker(new MarkerOptions().position(destination_n)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                .title(feed.getC_formatedAddress()));

//        tmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 20.0f));
//        LocalBroadcastManager.getInstance(this).registerReceiver(locationUpdated, new IntentFilter("locationUpdated"));
//        tmMap.clear();
//        if (mLocationPermissionsGranted){
//            getDeviceLocation();
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            tmMap.setMyLocationEnabled(true);
//        }
    }
    
//    private void stopLocationUpdate() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
//    }

//    private void displayLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null){
//            if (location_switch.isChecked()){
//                final double latitude = startPosition.latitude;
//                final double longitude = startPosition.longitude;
//
//                //add Marker
//                if (mCurrent != null)
//                    mCurrent.remove();//remove already markered
//                mCurrent = tmMap.addMarker(new MarkerOptions()
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
//                        .position(new LatLng(latitude,longitude))
//                        .title("You"));
//                //move car to this position
//                tmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15.0f));
//                //draw animation raotate marker
////                rotateMarker(mCurrent,-360,tmMap);
//
//                //Update to firebase
////                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
////                    @Override
////                    public void onComplete(String key, DatabaseError error) {
////
////                        //add Marker
////                        if (mCurrent != null)
////                            mCurrent.remove();//remove already markered
////                        mCurrent = tmMap.addMarker(new MarkerOptions()
////                                .position(new LatLng(latitude,longitude))
////                                .title("You Location"));
////                        //move car to this position
////                        tmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15.0f));
////                        //draw animation raotate marker
//////                        rotateMarker(mCurrent,-360,tmMap);
////                    }
////                });
//            }
//
//        }
//        else {
//            Log.d("ERROR: ","can not get your location");
//        }
//    }

//    private void setUpLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//           //Request runtime permission
//            ActivityCompat.requestPermissions(this,new String[]{
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            },MY_PRESMISSION_REQUEST_CODE);
//        }
//        else {
//            if (checkPlayService()){
//                buildGoogleApiClient();
//                createLocationRequest();
////                if (location_switch.isChecked()){
////                    displayLocation();
////                }
//            }
//        }
//    }

//    private void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//
//    private void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }

//    private boolean checkPlayService() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS){
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
//                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RES_REQUEST).show();
//            else {
//                Toast.makeText(getApplicationContext(),"this is not supported", Toast.LENGTH_LONG).show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

//    private void rotateMarker(final Marker mCurrent, final int i, GoogleMap tmMap) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        final float startRotation = mCurrent.getRotation();
//        final long duration = 1500;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float)elapsed/duration);
//                float rot = t*i+(1-t)*startRotation;
//                mCurrent.setRotation(-rot>180?rot/2:rot);
//                if (t<1.0){
//                    handler.postDelayed(this,16);
//                }
//            }
//        });
//    }

//    private void startLocationUpdate() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
////        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(Tracking.this, "connecton fild", Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
////            displayLocation();
////            startLocationUpdate();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//            mGoogleApiClient.connect();
//    }
    //    @Override
//    public void onLocationChanged(Location location) {
//            mLastLocation = location;
//            displayLocation();
//    }


    @Override
    public void onComplete(String response, int taskcode) {

        Log.d("response", response);
        if (taskcode == RequestCode.CODE_Direction_Api) {
            if (response != null) {

                ParserTask parserTask = new ParserTask(this);
                parserTask.execute(response.toString());
            }
        }
//        if (taskcode == RequestCode.CODE_Direction_Api){
//            Log.d(TAG, "Response code: " + response);
//            try {
//                JSONObject jsonObject = new JSONObject(response.toString());
//                JSONArray jsonArray = jsonObject.getJSONArray("routes");
//                int sie=jsonArray.length();
//                for (int i=0;i<jsonArray.length();i++){
//                    JSONObject route = jsonArray.getJSONObject(i);
//                    JSONObject poly = route.getJSONObject("overview_polyline");
//                    String polyline = poly.getString("points");
//                    polyLineList = decodePoly(polyline);
//                }
//
//                //Adjusting Bounds
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                for (LatLng latLng:polyLineList)
//                    builder.include(latLng);
//                LatLngBounds bounds = builder.build();
//
//                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,2);
//                tmMap.animateCamera(mCameraUpdate);
//
//                polylineOptions = new PolylineOptions();
//                polylineOptions.color(Color.GRAY);
//                polylineOptions.width(5);
//                polylineOptions.startCap(new SquareCap());
//                polylineOptions.endCap(new SquareCap());
//                polylineOptions.jointType(JointType.ROUND);
//                polylineOptions.addAll(polyLineList);
//                greyPolyline = tmMap.addPolyline(polylineOptions);
//
//                blackPolylineOptions = new PolylineOptions();
//                blackPolylineOptions.color(Color.rgb(255, 128, 0));
//                blackPolylineOptions.width(10);
//                blackPolylineOptions.startCap(new SquareCap());
//                blackPolylineOptions.endCap(new SquareCap());
//                blackPolylineOptions.jointType(JointType.ROUND);
//                blackPolylineOptions.addAll(polyLineList);
//                blackPolyline = tmMap.addPolyline(blackPolylineOptions);
//
////                tmMap.addMarker(new MarkerOptions()
////                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
////                        .position(source)
////                        .title("Driver Location"));
////
////                tmMap.addMarker(new MarkerOptions()
////                        .position(polyLineList.get(polyLineList.size()-1))
////                        .title("Drop Location"));
//
//                //Animation
//                ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0,100);
//                polyLineAnimator.setDuration(2000);
//                polyLineAnimator.setInterpolator(new LinearInterpolator());
//                polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                        List<LatLng> points = greyPolyline.getPoints();
//                        int precentValue = (int)valueAnimator.getAnimatedValue();
//                        int size = points.size();
//                        int newPoints = (int)(size * (precentValue/100.0f));
//                        List<LatLng> p = points.subList(0,newPoints);
//                        blackPolyline.setPoints(p);
//                    }
//                });
//                polyLineAnimator.start();
//
////                carMarker = tmMap.addMarker(new MarkerOptions().position(source)
////                        .flat(true)
////                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
////                handler = new Handler();
////                index = -1;
////                next = 1;
////                handler.postDelayed(drawPathRunnable,3000);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    }

    public void call_action() {
        String phnum = feed.getDriver_mobile().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    private void showRoute(List<HashMap<String, String>> data) {
        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(update_src);
        builder.include(destination_n);
        points = new ArrayList();
        lineOptions = new PolylineOptions();
        for (int j = 0; j < data.size(); j++) {
            HashMap<String, String> point = data.get(j);
            double lat = Double.parseDouble(point.get("lat").toString());
            double lng = Double.parseDouble(point.get("lng").toString());
            LatLng position = new LatLng(lat, lng);
            points.add(position);
            builder.include(position);
        }
        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(getResources().getColor(R.color.black));
        lineOptions.geodesic(true);
// Drawing polyline in the Google Map for the i-th route
        polyline = tmMap.addPolyline(lineOptions);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.17);
        if (dis<10000){
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, height, width, padding);
            tmMap.animateCamera(cu);
        }

//        startAnim();
    }

    private void timeCalculate() {
        String requestApi = SharedPrefManager.getDirectionsUrl(update_src, destination_n);
        Log.d(TAG, "getDirection requestApi: " + requestApi);
        volleyStringRequestGet(requestApi);
        // HashMap objectNew = new HashMap();
        //new WebTask(Tracking.this, requestApi, objectNew, Tracking.this, RequestCode.CODE_Direction_Api, 0);
    }

    public void volleyStringRequestGet(String requestApi) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestApi, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response code: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("routes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject routeObj = jsonArray.optJSONObject(i);

                        JSONArray legsArray = routeObj.optJSONArray("legs");
                        JSONObject legsObj = legsArray.optJSONObject(0);
                        JSONObject distanceObj = legsObj.optJSONObject("distance");
                        String distance = distanceObj.optString("text");
                        dis = distanceObj.optInt("value");
                        JSONObject durationObj = legsObj.optJSONObject("duration");
                        duration = durationObj.optString("text");
                        time_left.setText(duration + " left");
                        getSupportActionBar().setTitle(distance +" "+ getString(R.string.or) +" " + duration +" "+ getString(R.string.away));
//                        SharedPrefManager.getInstance(Tracking.this).storeOrderTime(duration);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header_param = new HashMap<>();
                header_param.put("Authorization", SharedPrefManager.getInstance(Tracking.this).getRegPeopleId());
                return header_param;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(Tracking.this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        tmMap.clear();
        super.onBackPressed();
    }

    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Tracking.this)) {
            if (SharedPrefManager.getLangId(Tracking.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Tracking.this,RequestCode.LangId));
            } else {
                Toast.makeText(Tracking.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Tracking.this,RequestCode.LangId, langval);
    }

//    private void startAnim(){
//        if(tmMap != null) {
//            MapAnimator.getInstance().animateRoute(tmMap, points);
//        } else {
//            Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void resetAnimation(View view){
//        startAnim();
//    }


    private class SampleAppStateListener implements AppStateListener {

        @Override
        public void onAppDidEnterForeground() {
            //   logAndToast(FOREGROUND);
            Intent intent = new Intent(Tracking.this, Rating.class);
            intent.putExtra("PRICE", feed.getPrice());
            intent.putExtra("SRC", feed.getP_formatedAddress());
            intent.putExtra("DES", feed.getC_formatedAddress());
            intent.putExtra("PRO_ID", feed.getId());
            intent.putExtra("DRI_IMG", WebUrls.BASE_URL + feed.getDriver_profile());
            startActivity(intent);
            finish();

        }

        @Override
        public void onAppDidEnterBackground() {
            //  logAndToast(BACKGROUND);
        }
    }
}
