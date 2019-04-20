package babbabazrii.com.bababazri.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import babbabazrii.com.bababazri.Activities.Notification;
import babbabazrii.com.bababazri.Activities.Order_Confirm;
import babbabazrii.com.bababazri.Activities.SearchingDropLocation;
import babbabazrii.com.bababazri.Activities.Tracking;
import babbabazrii.com.bababazri.Activities.User_Profile;
import babbabazrii.com.bababazri.Adapters.Material_List_Adapter;
import babbabazrii.com.bababazri.Adapters.PlaceAutocompleteAdapter;
import babbabazrii.com.bababazri.Adapters.Vehicle_List_Adapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.Material_list_Model;
import babbabazrii.com.bababazri.models.NearbyDrivers_Model;
import babbabazrii.com.bababazri.models.PlaceInfo;
import babbabazrii.com.bababazri.models.RequestListWapper;
import babbabazrii.com.bababazri.models.Vehicle_Model;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,
       GoogleApiClient.ConnectionCallbacks, WebCompleteTask {

    private static final String TAG = "Home";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    static Home mInstance;
    public static double distance;
    String image_string;

    Map<String, String> v_map= new HashMap<>();

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );

    private ArrayList<String> listdata = new ArrayList<String>();
    public static ArrayList<String> matrialId = new ArrayList<String>();
    public static ArrayList<String> matrialName = new ArrayList<String>();
    public static ArrayList<String> vehiclelId = new ArrayList<String>();
    public static ArrayList<String> vehicleName = new ArrayList<String>();
    ArrayList<NearbyDrivers_Model> nearbyDriversModelArrayList = new ArrayList<>();
    public static Double lat, lng;
    public static LatLng latLng_current_location,latLng_c,destination_latlng,source_latlng,defaultLatLng;
    public static String source_address,destination_address,add_c,sor_add,driver_id;

    //Widgets
    private AutoCompleteTextView mSearch_et;
    private ImageView imgGps, mInfo;
    LinearLayout tractor_lin, truck_lin, troli_lin;
    Boolean isClick = true;

    ImageView truckimag, troliimag, tractorimag;
    Animation bounce_in, bounce_out;
    Button book_later_btn, book_now_btn;

    public static boolean vehicle_isclick=false;
    public static String vehical_id;
    public static Boolean nearbyLatLng_isclick=false;
    public static JSONObject locationObject;

    //vars
    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 15f;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    public static double lat_double_current, log_double_current;
    ArrayList<Marker> marker_data = new ArrayList<>();
    public static ArrayList<Material_list_Model> material_list_models= new ArrayList<>();
    SupportMapFragment mMapFragment;
    public int ic_marker;
    NotificationBadge badgeCount;
    Integer badgeValue;
    Address address;
    public static TextView textView,home_time;
    RecyclerView recyclerView_vehicle;
    Vehicle_List_Adapter vehicleAdapter;
    ArrayList<Vehicle_Model> vehicle_list = new ArrayList<>();

    Handler h = new Handler();
    int delay = 50 * 1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;

    public static String id_stinrg,quantity_stinrg,unit_stinrg,unitPrice_stinrg,totalTime_stinrg,totalKM_stinrg,materialId_string;
    LinearLayout home_time_liner_lay;
    //Searching item
    public static String drop_address_stirng;
    public static LatLng drop_address_latlng;
    ArrayList<RequestListWapper> requestListWapperArrayList = new ArrayList<>();
    JSONArray jsonArrayO;
    ArrayList<String> imagvihiel = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSearch_et = (AutoCompleteTextView) view.findViewById(R.id.input_search);
        imgGps = (ImageView) view.findViewById(R.id.ic_gps);
        tractor_lin = (LinearLayout) view.findViewById(R.id.tractor_lin);
        truck_lin = (LinearLayout) view.findViewById(R.id.truck_lin);
        troli_lin = (LinearLayout) view.findViewById(R.id.troli_lin);
        tractorimag = (ImageView) view.findViewById(R.id.tractor_imag);
        truckimag = (ImageView) view.findViewById(R.id.truck_img);
        troliimag = (ImageView) view.findViewById(R.id.troli_img);
        book_later_btn = (Button) view.findViewById(R.id.book_later_btn);
        book_now_btn = (Button) view.findViewById(R.id.book_now_btn);
        mInfo = (ImageView) view.findViewById(R.id.place_info);
        bounce_in = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        bounce_out = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_out);
        home_time_liner_lay = (LinearLayout)view.findViewById(R.id.home_time_liner_lay);
        textView = view.findViewById(R.id.ic_clear);
        recyclerView_vehicle = (RecyclerView)view.findViewById(R.id.vehicle_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView_vehicle.setHasFixedSize(true);
        recyclerView_vehicle.setLayoutManager(linearLayoutManager);
        home_time = (TextView)view.findViewById(R.id.home_time);


        v_map.put("Truck", "ट्रक");
        v_map.put("Dumper", "डम्पर");
        v_map.put("Trolla", "ट्रोला");
        v_map.put("Pick Up", "पिक अप");
        v_map.put("Tractor Trolly", "ट्रेक्टर ट्रॉली");
//        home_time.setText(SharedPrefManager.getInstance(getActivity()).getOrderTime());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchingDropLocation.class));
            }
        });
        home_time_liner_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Tracking.class));
                TrakMethod(requestListWapperArrayList.get(0));
            }
        });

        setHasOptionsMenu(true);
        nearbyLatLng_isclick =false;
//        LocationServiceOn_Off();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (locationManager==null){
            showSettingsAlert();
        }else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                Log.i("About GPS", "GPS is Enabled in your devide");
                // Toast.makeText(ctx,"enable",Toast.LENGTH_SHORT).show();
            } else {
                //showAlert

                showSettingsAlert();
//            context.startActivity(new Intent(context, DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }



        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
//        if (SharedPrefManager.getIslagChange(Constants.ISLang)) {
//            if (SharedPrefManager.getLangId(Constants.LangId).compareTo("hindi") == 0) {
//                setLangRecreate(SharedPrefManager.getLangId(Constants.LangId));
//            } else {
//                setLangRecreate(SharedPrefManager.getLangId(Constants.LangId));
//            }
//        } else {
            fetchGetMaterials();
            fetchGetVehicles();
//        }

        initMap();

//        mInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG,"onClick: clicked place info");
//                try{
//                    if (mMarker.isInfoWindowShown()){
//                        mMarker.hideInfoWindow();
//                        Log.d(TAG,"onClick: place info" + mPlace.toString());
//                    }else {
//                        mMarker.showInfoWindow();
//                    }
//
//                }catch (NullPointerException e){
//                    Log.d(TAG,"onClick: NullPointerException " + e.getMessage());
//                }
//            }
//        });
        Log.d(TAG,"Authorization: "+SharedPrefManager.getInstance(getActivity()).getRegPeopleId());
            book_later_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jsonArrayO!=null && jsonArrayO.length()>0){
                        Toast.makeText(getContext(), R.string.already_have_order,Toast.LENGTH_SHORT).show();
                    }else {
                        User_Profile.getInstance().ToolbarHide();
                        Fragment home = new Book_Later();
                        FragmentManager FM = getFragmentManager();
                        FM.beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.content_frame, home)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });

        mInstance = this;
        return view;
    }

    public void LocationServiceOn_Off(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (locationManager==null){
            showSettingsAlert();
        }else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                Log.i("About GPS", "GPS is Enabled in your devide");
                // Toast.makeText(ctx,"enable",Toast.LENGTH_SHORT).show();
            } else {
                //showAlert

                showSettingsAlert();
//            context.startActivity(new Intent(context, DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(R.string.gps_setting);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.gps_setting_menu);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
//         Inflate the menu; this adds items to the action bar if it is present.
//         inflater.inflate(R.menu.user__profile, menu);
        inflater.inflate(R.menu.user__profile, menu);
        View menuITEM  = menu.findItem(R.id.notification).getActionView();
        badgeCount = (NotificationBadge) menuITEM.findViewById(R.id.count_noti);

        menuITEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menu.findItem(R.id.notification));
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity());
            Intent intent = new Intent(getActivity(),Notification.class);
            intent.putExtra(RequestCode.KEY_ANIM_TYPE,RequestCode.TransitionType.SlideJava);
            intent.putExtra(RequestCode.KEY_TITLE,R.string.notification);
            startActivity(intent);
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_f);
        mMapFragment.onResume();
        mMapFragment.getMapAsync(Home.this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

//            Toast.makeText(this.getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        try {
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.my_map_style));
            if (!isSuccess){
                Log.e("ERROR","Map style load failed!!!!");
            }
        }catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }
        mMap = googleMap;
        mMap.clear();
        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    /*-------------------------------Get Device Locaiton---------------------------------*/
    public void getDeviceLocation() {

//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                lat_double_current = location.getLatitude();
//                log_double_current = location.getLongitude();
//                locationObject = new JSONObject();
//                try {
//                    locationObject.put("lat",lat_double_current);
//                    locationObject.put("lng",log_double_current);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                latLng_current_location = new LatLng(lat_double_current, log_double_current) ;
//                add_c = getAddress(lat_double_current,log_double_current,add_c);
//                //  SharedPreferences sharedPreferencesReg = getContext().getSharedPreferences("LatLng", Context.MODE_PRIVATE);
////                moveCamera(new LatLng(lat_double_current, log_double_current), DEFAULT_ZOOM, "My Location");
//                init();
//            }
//        });
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        Task<Location> location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Found location");
                    Location currentLocation = (Location) task.getResult();
//                            SharedPrefManager.getInstance(getActivity()).storeLatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    try{
                        lat_double_current = currentLocation.getLatitude();
                        log_double_current = currentLocation.getLongitude();
                        locationObject = new JSONObject();
                        try {
                            locationObject.put("lat",lat_double_current);
                            locationObject.put("lng",log_double_current);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        latLng_current_location = new LatLng(lat_double_current, log_double_current) ;
                        add_c = getAddress(lat_double_current,log_double_current,add_c);
                        textView.setText(add_c);
                        //  SharedPreferences sharedPreferencesReg = getContext().getSharedPreferences("LatLng", Context.MODE_PRIVATE);
                        moveCamera(new LatLng(lat_double_current, log_double_current), DEFAULT_ZOOM, getString(R.string.my_location));
                        init();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                } else {
                    Log.d(TAG, "onComplete:  current is location null");
                    Toast.makeText(getContext(), R.string.location_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void init() {
        Log.d(TAG, "init: initializing");

        mSearch_et.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this.getActivity(),mGoogleApiClient,
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
        getNearbyDrivers(locationObject);
        imgGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                    mMap.clear();
                    nearbyLatLng_isclick =false;
                    getDeviceLocation();
            }
        });
        SharedPrefManager.getInstance(this.getActivity()).hideSoftKeyBoard(this.getActivity());
    }
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geoLoating");
        String searchString = mSearch_et.getText().toString();
        Geocoder geocoder = new Geocoder(this.getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOexception" + e.getMessage());
        }

        if (list.size()>0){
            address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
            //Toast.makeText(getContext(),address.toString(),Toast.LENGTH_LONG).show();
//            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM
//            ,address.getAddressLine(0));
        }
    }

//    private void moveCamera(LatLng latLng,float zoom,PlaceInfo placeInfo){
//        Log.d(TAG,"moveCamera: moving the camera to: lat:" +latLng.latitude +",lng:" +latLng.longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
//        mMap.clear();
//
//        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
//
//        if (placeInfo != null){
//            try {
//                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
//                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
//                        "Website: " + placeInfo.getWebsiteuri() + "\n" +
//                        "Price Rating: " + placeInfo.getRating() + "\n" +
//                        "Name: " + placeInfo.getName() + "\n" ;
//
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(latLng)
//                        .title(placeInfo.getName())
//                        .snippet(snippet);
//
//                mMarker = mMap.addMarker(markerOptions);
//
//                mMap.addMarker(markerOptions);
//
//            }catch (NullPointerException e){
//                Log.e(TAG,"moveCamera: NullPointerException" + e.getMessage());
//            }
//        }else {
//            mMap.addMarker(new MarkerOptions().position(latLng));
//        }
//
//        SharedPrefManager.getInstance(this.getActivity()).hideSoftKeyBoard(this.getActivity());
//
//    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ",lng:" + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

//        if (!title.equals("My Location"))
//        {
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
        mMap.addMarker(options);
//        }
        mSearch_et.setText("");
        SharedPrefManager.getInstance(this.getActivity()).hideSoftKeyBoard(this.getActivity());

    }

    /*-------------------------------get Materials id and name---------------------------------*/
    private void fetchGetMaterials() {
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL + WebUrls.getMaterials_Name_Id,objectNew,Home.this,RequestCode.CODE_fetchGetMaterials,3);
    }
    /*-------------------------------get Vehicles id and name---------------------------------*/
    private void fetchGetVehicles() {
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL + WebUrls.getVehicles_Name_Id,objectNew,Home.this,RequestCode.CODE_fetchGetVehicles,3);
    }

    /*-------------------------------google places API autocomplete suggestions*/
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SharedPrefManager.getInstance(getActivity()).hideSoftKeyBoard(getActivity());

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
            latLng_c = mPlace.getLatLng();
            lat = latLng_c.latitude;
            lng = latLng_c.longitude;
            Log.d(TAG,"OnResult: place LatLng details " + lat + ","+ lng);
//            moveCamera(latLng,DEFAULT_ZOOM,"My Drop Location");
            nearByDriversWithLatLng(latLng_c,"Drop Location");
            places.release();

        }
    };

    public void nearByDriversWithLatLng(LatLng latLng,String address) {

        latLng_c = latLng;
        mMap.clear();
        nearbyDriversModelArrayList.clear();
        marker_data.clear();
        HashMap objectNew = new HashMap();

        textView.setText(address);
        locationObject = new JSONObject();
        try {
            locationObject.put("lat",latLng.latitude);
            locationObject.put("lng",latLng.longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nearbyLatLng_isclick=true;
        if (vehicle_isclick == false && Material_List_Adapter.material_isclick == false){

            Log.d(TAG,"nearByDriversWithLatLng"+vehicle_isclick + Material_List_Adapter.material_isclick);

            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api +
                    "location=" + locationObject,
                    objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);
        }else if (vehicle_isclick == false && Material_List_Adapter.material_isclick==true){
            Log.d(TAG,"nearByDriversWithLatLng"+vehicle_isclick +","+ Material_List_Adapter.material_isclick +
                    "\n+ loacation"+locationObject +
                    "\n+ Material Id: "+ Material_List_Adapter.meterial_id);

            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api + "location=" + locationObject +
                    "&materialId=" + Material_List_Adapter.meterial_id ,
                    objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);

        }else if (vehicle_isclick == true && Material_List_Adapter.material_isclick==true ){
            Log.d(TAG,"nearByDriversWithLatLng"+vehicle_isclick + Material_List_Adapter.material_isclick +
                    "\n+ loacation"+locationObject +
                    "\nvehical_id: "+vehical_id +
                    "\n+ Material Id: "+Material_List_Adapter.meterial_id);
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api +
                    "location=" + locationObject +
                    "&materialId=" + Material_List_Adapter.meterial_id +
                    "&vehicleTypeId=" + vehical_id ,
                    objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);
        }else if (vehicle_isclick == true && Material_List_Adapter.material_isclick==false ){
            Log.d(TAG,"nearByDriversWithLatLng"+vehicle_isclick + Material_List_Adapter.material_isclick +
                    "\n+ loacation"+locationObject + vehical_id);

            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api + "location=" + locationObject +
                    "&vehicleTypeId=" + vehical_id ,
                    objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);
        }
    }
    @Override
    public void onStart() {
        super.onStart();

//        if (SharedPrefManager.getIslagChange(Constants.ISLang)) {
//            if (SharedPrefManager.getLangId(Constants.LangId).compareTo("hindi") == 0) {
//                setLangRecreate(SharedPrefManager.getLangId(Constants.LangId));
//            } else {
//                setLangRecreate(SharedPrefManager.getLangId(Constants.LangId));
//            }
//        } else {
            updateHotCountn();
            CheckOrder_On_List();
            vehicle_isclick=false;
//        home_time.setText(SharedPrefManager.getInstance(getActivity()).getOrderTime());
            Material_List_Adapter.material_isclick=false;
            User_Profile.getInstance().ToolbarShow();
//        }
    }
    @Override
    public void onResume() {
        updateHotCountn();
        if (locationObject==null){
            getDeviceLocation();
        }
        mGoogleApiClient.connect();
        //start handler as activity become visible

        h.postDelayed(runnable = new Runnable() {
            public void run() {
//                home_time.setText(SharedPrefManager.getInstance(getActivity()).getOrderTime());
                if (jsonArrayO!=null && jsonArrayO.length()>0){
                    timeCalculate();
                    h.postDelayed(runnable, delay);
                }

            }
        }, delay);
        super.onResume();
    }
    @Override
    public void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mMapFragment.onPause();

        Log.d(TAG, "onPause");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapFragment.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    public static Home getInstance() {
        return mInstance;
    }

//Showing Materials and Vehicles Nearby Current Location-----------------------------Methods----------
    public void getNearbyDriversVehicleLatLng(JSONObject locationObject, String vehical_id) {
    Log.d(TAG,"getNearbyDriversVehicleLatLng method: Vehicle Id"+ vehical_id+"\nLocation:" + locationObject);
    if (locationObject == null){
        showSettingsAlert();
    }else {
        mMap.clear();
        nearbyDriversModelArrayList.clear();
        marker_data.clear();
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api + "location="+locationObject+"&vehicleTypeId=" + vehical_id, objectNew, Home.this, RequestCode.CODE_GET_NearbyDriversVehicle, 3);
    }

    }
    public void getNearbyDrivers(JSONObject locationObject) {
        Log.d(TAG,"getNearbyDrivers method"+locationObject);
        if ( mMap != null){
            mMap.clear();
        }
        nearbyDriversModelArrayList.clear();
        marker_data.clear();
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api +
                "location="+locationObject,
                objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);
    }
    public void getNearbyDriversMaterial(JSONObject locationObject,String material_string) {
        Log.d(TAG,"getNearbyDriversMaterial method"+locationObject+material_string);
           if ( mMap != null){
               mMap.clear();
           }

           if (locationObject == null){
               showSettingsAlert();
           }
           else {
               nearbyDriversModelArrayList.clear();
               marker_data.clear();
               HashMap objectNew = new HashMap();
               new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api +
                       "location="+locationObject+
                       "&materialId="+material_string,
                       objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);
           }

    }
    public void getNearbyDriversVehicle(JSONObject locationObject,String material_string) {
        Log.d(TAG,"getNearbyDriversVehicle method"+locationObject+material_string);
        if ( mMap != null){
            mMap.clear();
        }
        if (locationObject == null){
            showSettingsAlert();
        }else {
            nearbyDriversModelArrayList.clear();
            marker_data.clear();
            HashMap objectNew = new HashMap();
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api +
                    "location="+locationObject +
                    "&vehicleTypeId=" + material_string,
                    objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);
        }

    }
    public void getNearbyDriversVehicleWithMaterial(JSONObject locationObject,String vehicle_id,String material__name ) {
        Log.d(TAG,"getNearbyDriversVehicleWithMaterial method : vehicle_id:-" + vehicle_id +
                "\n meterial id: " + material__name +
                "\n location: " + locationObject);
        if ( mMap != null){
            mMap.clear();
        }

        if (locationObject==null){
            showSettingsAlert();
        }
        else {
            nearbyDriversModelArrayList.clear();
            marker_data.clear();
            HashMap objectNew = new HashMap();
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.nearByDrivers_api +
                    "location="+locationObject+
                    "&vehicleTypeId=" + vehicle_id +
                    "&materialId=" + material__name,
                    objectNew, Home.this, RequestCode.CODE_GET_NearbyDrivers, 3);
        }

    }
    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("OnComplete response:", response);
        if (taskcode == RequestCode.CODE_GET_NearbyDrivers) {
            Log.d(TAG,"Get nearbyDrivers: "+response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");

                JSONArray jsonArray = successObj.optJSONArray("data");
                if (jsonArray.length()>0){
                    Log.d(TAG,"Not Null nearbyDrivers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        NearbyDrivers_Model nearbyDriversModel = new NearbyDrivers_Model();
                        JSONObject o = jsonArray.optJSONObject(i);
                        nearbyDriversModel.setAdminApproval(o.optString("adminApproval"));
                        nearbyDriversModel.setRealm(o.optString("realm"));
                        nearbyDriversModel.setPassword(o.optString("password"));
                        nearbyDriversModel.setMaterialId(o.optString("materialId"));
                        nearbyDriversModel.setMaterialTypeId(o.optString("materialTypeId"));
                        nearbyDriversModel.setFirstName(o.optString("firstName"));
                        nearbyDriversModel.setLastName(o.optString("lastName"));
                        nearbyDriversModel.setMobile(o.optString("mobile"));
                        nearbyDriversModel.setOwnerId(o.optString("ownerId"));
                        nearbyDriversModel.setAddress(o.optString("address"));
                        nearbyDriversModel.setDriverLicense(o.optString("driverLicense"));
                        nearbyDriversModel.setPoliceVerify(o.optString("policeVerify"));
                        nearbyDriversModel.setAadharCard(o.optString("aadharCard"));
                        nearbyDriversModel.setImage(o.optString("image"));
                        nearbyDriversModel.setCreatedAt(o.optString("createdAt"));
                        nearbyDriversModel.setVehicleId(o.optString("vehicleId"));
                        nearbyDriversModel.setVehicleTypeId(o.optString("vehicleTypeId"));
                        nearbyDriversModel.setValidate(o.optString("validate"));
                        nearbyDriversModel.setThrowss(o.optString("throws"));
                        nearbyDriversModel.setIsAvailable(o.optString("isAvailable"));
                        JSONObject locationObj = o.optJSONObject("location");
                        String latm = null,lngm=null;
                        if (o.optJSONObject("location")!=null){
                            latm =locationObj.optString("lat");
                            lngm  = locationObj.optString("lng");
                            nearbyDriversModel.setLat(locationObj.optString("lat"));
                            nearbyDriversModel.setLng(locationObj.optString("lng"));
                        }

                        nearbyDriversModel.setOrderQuantity(o.optString("orderQuantity"));
                        nearbyDriversModel.setUnit(o.optString("unit"));
                        nearbyDriversModel.setPrice(o.optString("price"));
                        nearbyDriversModel.setId(o.optString("id"));

                        nearbyDriversModel.setDriver_image(o.getJSONObject("vehicleType").optString("driveImage"));
                        nearbyDriversModel.setVehicleType_name(o.getJSONObject("vehicleType").optString("name"));
                        JSONObject materialObj = o.optJSONObject("material");
                        if (o.optJSONObject("material")!=null)
                        {
                            nearbyDriversModel.setMat_name(materialObj.optString("name"));
                            nearbyDriversModel.setMat_id(materialObj.optString("id"));
                        }

                        JSONObject materialTypeObj = o.optJSONObject("materialType");
                        if (o.optJSONObject("materialType")!=null){
                            nearbyDriversModel.setMat_ty_name(materialTypeObj.optString("name"));
                            nearbyDriversModel.setMat_ty_id(materialTypeObj.optString("id"));
                        }

                        imagvihiel.add(nearbyDriversModel.getDriver_image());
                        nearbyPlaceMaker(new LatLng(Double.valueOf(latm), Double.valueOf(lngm)),
                                nearbyDriversModel.getDriver_image(),
//                                WebUrls.BASE_URL+nearbyDriversModel.getDriver_image()
                                nearbyDriversModel.getVehicleType_name()
                        );
                        nearbyDriversModelArrayList.add(nearbyDriversModel);
                    }
                }
                else {
                    Log.d(TAG,"Null nearbyDrivers");
                    if (nearbyLatLng_isclick==true){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_c, DEFAULT_ZOOM));
                        MarkerOptions option = new MarkerOptions()
                                .position(latLng_c)
                                .title("My Current location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                        mMap.addMarker(option);
                    }else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_current_location, DEFAULT_ZOOM));
                        MarkerOptions option_current = new MarkerOptions()
                                .position(latLng_current_location)
                                .title("My Current location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                        mMap.addMarker(option_current);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (taskcode == RequestCode.CODE_fetchGetMaterials){
            matrialId.clear();
            matrialName.clear();
            material_list_models.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray = successObj.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.optJSONObject(i);
                    Material_list_Model item = new Material_list_Model();
                    item.setMaterial_name(obj.optString("name"));
                    item.setMatrial_id(obj.optString("id"));
                    matrialId.add(obj.optString("id"));
                    matrialName.add(obj.optString("name"));
                    material_list_models.add(item);
                }
//                    User_Profile.adapter.notifyDataSetChanged();
                User_Profile.adapter = new Material_List_Adapter(getContext(),Home.material_list_models);
                User_Profile.materiallist.setAdapter(User_Profile.adapter);
                Log.d(TAG, "On Response : Material Id:-" + matrialId + "\n Materials Name" + matrialName+ "\n Materialslist Name" + material_list_models);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (taskcode == RequestCode.CODE_fetchGetVehicles){
            vehiclelId.clear();
            vehicleName.clear();
            vehicle_list.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray = successObj.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.optJSONObject(i);
                    Vehicle_Model item = new Vehicle_Model();
                    item.setVehicle_id(obj.optString("id"));

//                    if (SharedPrefManager.getLangId(getContext(),RequestCode.LangId).compareTo("") != 0){
//                        if (SharedPrefManager.getLangId(getContext(),RequestCode.LangId).compareTo("hi")==0){
//
//                            for (Map.Entry<String, String> entry : v_map.entrySet()) {
////                                System.out.println(entry.getKey() + "=" + entry.getValue());
//                                if (obj.optString("name").equals(entry.getKey())){
//                                    item.setVehicle_name(entry.getValue());
//                                }
//                            }
//
//                        }else {
//                            item.setVehicle_name(obj.optString("name"));
//                        }
//                    }else if (Locale.getDefault().getLanguage().compareTo("hi")==0){
//                        for (Map.Entry<String, String> entry : v_map.entrySet()) {
////                            System.out.println(entry.getKey() + "=" + entry.getValue());
//                            if (obj.optString("name").equals(entry.getKey())){
//                                item.setVehicle_name(entry.getValue());
//                            }
//                        }
//                    }else {
                        item.setVehicle_name(obj.optString("name"));
//                    }

                    item.setVehicle_image(obj.optString("image"));
                    item.setVehicle_click_image(obj.optString("clickImage"));
                    vehiclelId.add(obj.optString("id"));
                    vehicleName.add(obj.optString("name"));
                    vehicle_list.add(item);
                }
                vehicleAdapter = new Vehicle_List_Adapter(getContext(),vehicle_list);
                recyclerView_vehicle.setAdapter(vehicleAdapter);
                Log.d(TAG, "On Response : Vehicles Id:-" + vehiclelId + "\n Vehicles Name" + vehicleName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_updateHotCountn){
            try{
                Log.d(TAG,"Response Badge Count: "+response);
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONObject dataObj = successObj.optJSONObject("data");
                badgeValue = dataObj.optInt("badgeCount");

                JSONObject msg = successObj.optJSONObject("msg");
                String replyCode = msg.optString("replyCode");
                String replyMessage = msg.optString("replyMessage");
                if (badgeCount==null)return;
                if (replyCode.equals(replyMessage)){
                    if (badgeValue == 0){
                        badgeCount.setVisibility(View.GONE);
                    }else {
                        badgeCount.setVisibility(View.VISIBLE);
                        badgeCount.setText(badgeValue.toString());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (taskcode == RequestCode.CODE_GetCurrentOrder){
            try {
                requestListWapperArrayList.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                jsonArrayO = successObj.optJSONArray("data");
                if (jsonArrayO != null && jsonArrayO.length()>0){
                    for (int i = 0; i < jsonArrayO.length(); i++) {
                        JSONObject o = jsonArrayO.optJSONObject(i);
                        RequestListWapper item = new RequestListWapper();
                        item.setId(o.optString("id"));
                        item.setOrderDate(o.optString("orderDate"));
                        item.setDeliveryDate(o.optString("deliveryDate"));
                        item.setOrderQuantity(o.optString("orderQuantity"));
                        item.setUnit(o.optString("unit"));
                        item.setRatingDone(o.optBoolean("isRatingDone"));

                        item.getDriver_profile();
                        item.setP_formatedAddress(o.optJSONObject("pickupAddress").optString("formattedAddress"));
                        item.setSrc_location(o.optJSONObject("pickupAddress").optJSONObject("location")+"");
                        item.setC_formatedAddress(o.optJSONObject("customerAddress").optString("formattedAddress"));
                        item.setLatlng(o.optJSONObject("customerAddress").optJSONObject("location")+"");
                        item.setDeliveryStatus(o.optString("deliveryStatus"));
                        item.setBookingStatus(o.optString("bookingStatus"));
                        item.setPrice(o.optString("price"));
                        item.setDeliveryDistance(o.optString("deliveryDistance"));
                        item.setUnitPrice(o.optString("unitPrice"));

                        JSONObject material = o.optJSONObject("material");
                        if (o.optJSONObject("material")!=null)
                            item.setMaterial_name(material.optString("name"));

                        JSONObject materialType = o.optJSONObject("materialType");
                        if (o.optJSONObject("materialType")!=null)
                            item.setMaterialType_name(materialType.optString("name"));

                        item.setDriverId(o.optString("driverId"));

                        JSONObject priceInfo = o.optJSONObject("priceInfo");
                        if (o.optJSONObject("priceInfo")!=null){
                            item.setPrice_get(priceInfo.optString("price"));
                            item.setLoading_get(priceInfo.optString("loading"));
                            item.setUnloading_get(priceInfo.optString("unloading"));
                            item.setLoadKM_get(priceInfo.optString("loadKM"));
                            item.setUnloadKM_get(priceInfo.optString("unloadKM"));
                            item.setDriverSalary_get(priceInfo.optString("driverSalary"));
                            item.setDriverDailyExp_get(priceInfo.optString("driverDailyExp"));
                            item.setHelper_get(priceInfo.optString("helper"));
                            item.setRoyalty_get(priceInfo.optString("royalty"));
                            item.setTollTax(priceInfo.optString("tollTax"));
                            item.setDriverSaving_get(priceInfo.optString("driverSaving"));
                            item.setNightStay_get(priceInfo.optString("nightStay"));
                            item.setMinCharge_get(priceInfo.optString("minCharge"));
                            item.setTotalPrice_get(priceInfo.optString("totalPrice"));
                            item.setTyres_get(priceInfo.optString("tyres"));
                            item.setDays_get(priceInfo.optString("days"));
                            item.setTotalKM_get(priceInfo.optString("totalKM"));
                        }

                        if (o.optJSONObject("vehicle")!=null && o.optJSONObject("driver")!=null){
                            item.setTruckNumber(o.optJSONObject("vehicle").optString("truckNumber"));
                            item.setVehicleTpye_name(o.optJSONObject("vehicle").optJSONObject("vehicleType").optString("name"));
                            item.setVehicleTpye_image(o.optJSONObject("vehicle").optJSONObject("vehicleType").optString("image"));
                            image_string = o.optJSONObject("driver").optString("profileImage");
                            item.setDriver_profile(o.optJSONObject("driver").optString("profileImage"));
                            item.setDriver_fullname(o.optJSONObject("driver").optString("fullName"));
                            item.setVehicleTypeId(o.optJSONObject("vehicle").optString("vehicleTypeId"));
                            item.setDriver_mobile(o.optJSONObject("driver").optString("mobile"));
                            item.setRating(o.optJSONObject("driver").optJSONObject("rating").optString("avgRating"));

                        }else {
                            item.setTruckNumber(" ");
                            item.setVehicleTpye_name(" ");
                            item.setDriver_fullname(" ");
                            item.setDriver_mobile("");
                            item.setRating("");
                        }

                        JSONObject messageObj = successObj.optJSONObject("msg");
                        String replyCode = messageObj.optString("replyCode");
                        String replyMessage = messageObj.optString("replyMessage");
                        requestListWapperArrayList.add(item);
                        if (replyCode.equalsIgnoreCase(replyMessage)){
                            Home_time();
                        }
                    }


                }else {
                    home_time_liner_lay.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_Direction_Api){
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
                    JSONObject durationObj = legsObj.optJSONObject("duration");
//                    duration = durationObj.optString("text");
                    home_time.setText(durationObj.optString("text"));
                    home_time_liner_lay.setVisibility(View.VISIBLE);
//                    getSupportActionBar().setTitle(distance + " away or " + duration + " left");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void nearbyPlaceMaker(final LatLng latLng_s, String title,String driver_image) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng_s.latitude + ",lng:" + latLng_s.longitude);

//        final Bitmap[] bmp = new Bitmap[1];
//        Thread thread = new Thread(new Runnable(){
//            @Override
//            public void run(){
//                URL url ;
//                try {
//                    url = new URL(WebUrls.BASE_URL+title);
//                    bmp[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MarkerOptions options = new MarkerOptions()
//                                .position(latLng_s)
//                                .title(title)
//                                .snippet("Book Now")
//                                .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(bmp[0])))
//                .icon(BitmapFactory.decodeStream((InputStream)url.getContent()))
                                ;
//                        Marker marker= mMap.addMarker(options);
//                        marker_data.add(marker);
//                    }
//                });
//            }
//        });
//        thread.start();
        //-------------

//
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
//        Canvas canvas1 = new Canvas(bmp);
//
//// paint defines the text color, stroke width and size
//        Paint color = new Paint();
//        color.setTextSize(35);
//        color.setColor(Color.BLACK);
//
//// modify canvas
//        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
//                R.drawable.location_c), 0,0, color);
//        canvas1.drawText("User Name!", 30, 40, color);
//
//// add marker to Map
//        mMap.addMarker(new MarkerOptions()
//                .position(latLng_s)
//                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
//                // Specifies the anchor to be at a particular point in the marker image.
//                .anchor(0.5f, 1));

        if (driver_image.equals(getString(R.string.truck))){
            ic_marker = R.drawable.truck_map;
        }else if (driver_image.equals(getString(R.string.dumper))){
            ic_marker = R.drawable.dumper_map;
        }else if (driver_image.equals(getString(R.string.trolla))){
            ic_marker = R.drawable.trolla_map;
        }else if (driver_image.equals(getString(R.string.pick_up))){
            ic_marker = R.drawable.pick_up_map;
        }else if (driver_image.equals(getString(R.string.tractor_trolly))||driver_image.equals(getString(R.string.tractor))){
            ic_marker = R.drawable.tractor_trolly_map;
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(WebUrls.BASE_URL+title);
//            headView.setHeadImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
            MarkerOptions options = new MarkerOptions()
                    .position(latLng_s)
                    .snippet("Book Now")
//                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(BitmapFactory.decodeStream((InputStream)url.getContent()))))
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(ic_marker)))
                    ;
            Marker marker= mMap.addMarker(options);
            marker_data.add(marker);
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }
//        MarkerOptions options = new MarkerOptions()
//                                .position(latLng_s)
//                                .title(title)
//                                .snippet("Book Now")
//                                //.anchor(0.5f, 1)
//                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(ic_marker)))
////                .icon(getMarkerBitmapFromView( ic_marker))
//                                ;
//                        Marker marker= mMap.addMarker(options);
//                        marker_data.add(marker);

        if (nearbyLatLng_isclick==true){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_c, DEFAULT_ZOOM));
//            mSearch_et.setText(getAddress(latLng_c.latitude,latLng_c.longitude,add_c));
            MarkerOptions option = new MarkerOptions()
                    .position(latLng_c)
                    .title("My Drop location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
            mMap.addMarker(option);
        }else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_current_location, DEFAULT_ZOOM));
//            mSearch_et.setText(getAddress(latLng_current_location.latitude,latLng_current_location.longitude,add_c));
            MarkerOptions option_current = new MarkerOptions()
                    .position(latLng_current_location)
                    .title("My Current location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
            mMap.addMarker(option_current);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (jsonArrayO!=null && jsonArrayO.length()>0){
                    Toast.makeText(getContext(), R.string.already_have_order,Toast.LENGTH_SHORT).show();
                }else {
                    for (int i=0;i<marker_data.size();i++){
                        if (marker.equals(marker_data.get(i))){
                            updateBottomSheetContent(marker,i,latLng_s);
                            break;
                        }
                    }
                }
                return true;
            }
        });
        SharedPrefManager.getInstance(this.getActivity()).hideSoftKeyBoard(this.getActivity());
    }
    private void updateBottomSheetContent(Marker marker,int pos,LatLng latLng_so) {
        Log.d(TAG,"updateBottomSheetContent: Called");

        if (nearbyLatLng_isclick){
            destination_latlng = latLng_c;
            destination_address = textView.getText().toString();
        }else {
            destination_latlng = latLng_current_location;
            destination_address = add_c;
        }


        View view = getLayoutInflater().inflate(R.layout.nearby_bottom_sheet, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);
        dialog.show();
        TextView product_tv = view.findViewById(R.id.product_name_bs);
        TextView product_price_chart = view.findViewById(R.id.price_chart_booknow);
        TextView type_tv = view.findViewById(R.id.type_bs);
        TextView quantity_tv = view.findViewById(R.id.quantity_bs);
        TextView unit_tv = view.findViewById(R.id.unit_bs);
        TextView price_tv = view.findViewById(R.id.price_bs);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button btn_booknow = (Button)view.findViewById(R.id.btn_booknow);

        NearbyDrivers_Model nearbyDriversModel = new NearbyDrivers_Model();
        nearbyDriversModel = this.nearbyDriversModelArrayList.get(pos);

        source_latlng = new LatLng(Double.valueOf(nearbyDriversModel.getLat()),Double.valueOf(nearbyDriversModel.getLng()));
        source_address=getAddress(Double.valueOf(nearbyDriversModel.getLat()),Double.valueOf(nearbyDriversModel.getLng()),sor_add);;
        String Product =  nearbyDriversModel.getMat_name();
        String Type =  nearbyDriversModel.getMat_ty_name();
        String Quantity =  nearbyDriversModel.getOrderQuantity();
        String Price =  nearbyDriversModel.getPrice();
        String Unit = nearbyDriversModel.getUnit();
        driver_id = nearbyDriversModel.getId();

//        product_price_chart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               startActivity(new Intent(getContext(), Price_Chart.class));
//            }
//        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"updateBottomSheetContent: Button Canecl");
                dialog.dismiss();
            }
        });
        btn_booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"updateBottomSheetContent: Button Book now");
//                distance = distanceCalculateMethod(source_latlng.latitude,source_latlng.longitude,
//                                                    destination_latlng.latitude,destination_latlng.longitude);
                Intent intent = new Intent(getActivity(), Order_Confirm.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        product_tv.setText(String.format("%s: ", Product));
        type_tv.setText(Type);
        quantity_tv.setText(Quantity);
        unit_tv.setText(Unit);
        price_tv.setText(Price);
        quantity_stinrg=quantity_tv.getText().toString();
        unit_stinrg = unit_tv.getText().toString();
        id_stinrg = nearbyDriversModel.getVehicleTypeId();
        unitPrice_stinrg = price_tv.getText().toString();
        materialId_string = nearbyDriversModel.getMat_id();
    }

    /*--------------------------------Distance Calculate Method---------------------------------------------*/
//    private double distanceCalculateMethod(double latitude1, double longitude1, double latitude2, double longitude2) {
//
//        double theta = longitude1-longitude2;
//        double dist = Math.sin(deg2rad(latitude1))
//                *Math.sin(deg2rad(latitude2))
//                +Math.cos(deg2rad(latitude1))
//                *Math.cos(deg2rad(latitude2))
//                *Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        return (dist);
//    }
//
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }
//
//    private double deg2rad(double deg) {
//        return (deg * Math.PI/180.0);
//    }
    /*--------------------------------Distance Calculate Method---------------------------------------------*/

    /*get Address from latlng method*/
    public String getAddress(double lat, double lng,String add) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Connect", "failed ");
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Connect", "Connected ");
        Log.d("onConnected", Boolean.toString(mGoogleApiClient.isConnected()));
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    public void updateHotCountn(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL+WebUrls.GetBadgeCount_Api,objectNew,Home.this,RequestCode.CODE_updateHotCountn,3);
//        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
//        StringRequest stringRequest=new StringRequest(Request.Method.GET,WebUrls.BASE_URL+WebUrls.GetBadgeCount_Api, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                try{
//                    Log.d(TAG,"Response Badge Count: "+response);
//                    JSONObject jsonObject = null;
//                    jsonObject = new JSONObject(response);
//                    JSONObject successObj = jsonObject.optJSONObject("success");
//                    JSONObject dataObj = successObj.optJSONObject("data");
//                    badgeValue = dataObj.optInt("badgeCount");
//
//                    JSONObject msg = successObj.optJSONObject("msg");
//                    String replyCode = msg.optString("replyCode");
//                    String replyMessage = msg.optString("replyMessage");
//                    if (badgeCount==null)return;
//                    if (replyCode.equals(replyMessage)){
//                        if (badgeValue == 0){
//                            badgeCount.setVisibility(View.GONE);
//                        }else {
//                            badgeCount.setVisibility(View.VISIBLE);
//                            badgeCount.setText(badgeValue.toString());
//                        }
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error)
//            {
//                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> header_param = new HashMap<>();
//                header_param.put("Authorization", SharedPrefManager.getInstance(getActivity()).getRegPeopleId());
//                return header_param;
//            }
//        };
//
//        requestQueue.add(stringRequest);
    }
    // Convert a view to bitmap
    private Bitmap getMarkerBitmapFromView(int resId) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_maker_layout, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.veh_img);
        ImageView vcl = (ImageView)customMarkerView.findViewById(R.id.book_v);
        if (SharedPrefManager.getLangId(getActivity(),RequestCode.LangId).compareTo("hi") == 0){
            vcl.setImageDrawable(getResources().getDrawable(R.drawable.book_h));
        }else {
            vcl.setImageDrawable(getResources().getDrawable(R.drawable.book));
        }
//        markerImageView.setImageBitmap(resId);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public void CheckOrder_On_List(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL+WebUrls.GetCurrentOder_Api,objectNew,Home.this,RequestCode.CODE_GetCurrentOrder,3);
    }
    public void TrakMethod(RequestListWapper requestListWapper){
        Intent intent = new Intent(getContext(), Tracking.class);
        intent.putExtra("data", requestListWapper);
        startActivity(intent);
    }
    public static LatLng update_src,destination_n;
    private void Home_time(){
        RequestListWapper item =requestListWapperArrayList.get(0);
        String orderRequestId=item.getId();
        try {
            JSONObject object = new JSONObject(item.getSrc_location());
            JSONObject des_obj = new JSONObject(item.getLatlng());
            update_src =new LatLng(object.optDouble("lat"), object.optDouble("lng"));
            destination_n = new LatLng(des_obj.optDouble("lat"), des_obj.optDouble("lng"));
            //animateMarker(location, carMarker);
        } catch (Exception e) {

        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Locations").child(item.getId()+"");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && orderRequestId.equals(item.getId())) {
                    try {
                        Map<String, String> value1 = (Map<String, String>) dataSnapshot.getValue();
                        Log.i("dataSnapshot json", "dataSnapshot" + new JSONObject(value1));
                        JSONObject jsonObject = new JSONObject(value1);
                        Log.d("jsondata", jsonObject + "");
//                        if (jsonObject.optString("jobStatus").compareToIgnoreCase("Pending") == 0) {
//                            Toast.makeText(getActivity(), "product not dispatched yet", Toast.LENGTH_LONG).show();
//
//                        } else if (jsonObject.optString("jobStatus").compareToIgnoreCase("start") == 0) {

                            Log.d(TAG, "Start Trip but AnimateMaker NOt Call");
                            String lat = String.valueOf(jsonObject.optString("currntLatt"));
                            String lng = String.valueOf(jsonObject.optString("currntLong"));
                            update_src = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            timeCalculate();
//                        }
                    } catch (Exception e) {
                        Log.d("exception...", e.toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void timeCalculate() {
        String requestApi = SharedPrefManager.getDirectionsUrl(update_src, destination_n);
        Log.d(TAG, "getDirection requestApi: " + requestApi);
//        volleyStringRequestGet(requestApi);
         HashMap objectNew = new HashMap();
        new WebTask(getActivity(), requestApi, objectNew, Home.this, RequestCode.CODE_Direction_Api, 3);
    }

}
