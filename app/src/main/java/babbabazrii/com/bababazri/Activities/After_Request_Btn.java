package babbabazrii.com.bababazri.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import babbabazrii.com.bababazri.Adapters.PlaceAutocompleteAdapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.PlaceInfo;
import babbabazrii.com.bababazri.models.SearchItemBinder;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class After_Request_Btn extends AppCompatActivity implements View.OnClickListener, WebCompleteTask, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    TextView date, unit_tv;
    EditText quantity_tv;
    Spinner unit_spinner;
    String unit_string;
    DatePickerDialog datePickerDialog;
    Toolbar toolbar;
    Button submit;
    ImageView cleart;
    private ArrayList<String> listdata = new ArrayList<String>();
    AutoCompleteTextView mSearch_et;
    private GoogleApiClient mGoogleApiClient_after_srch_btn;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );
    private PlaceInfo mPlace;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final String TAG = "Order Request";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    ProgressDialog progressDialog = null;
    private static String address;
    LatLng latLng_drop_location;
    SearchItemBinder feed;
    String distance,duration;
    double distancevalue,durationvalue;
    public static double dis_tot,dur_tot;
    public static String address_string,date_string,lat, lng,quantity_string;
    public static ArrayList<String> vehicle_nm = new ArrayList<>();
    public static ArrayList<String> vehicle_id = new ArrayList<>();
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window window = getWindow();
//
//// clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//// finally change the color
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusBarColor));
//        }
        SharedPrefManager.getInstance(this).hideSoftKeyBoard(After_Request_Btn.this);
        setContentView(R.layout.activity_after__request__btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar_after_request);
        setSupportActionBar(toolbar);

        unit_tv = (TextView)findViewById(R.id.aft_btn_unit);
        feed = (SearchItemBinder)getIntent().getSerializableExtra("data_after");

        unit_tv.setText(feed.getAft_s_unit_show());
        getSupportActionBar().setTitle(SharedPrefManager.getInstance(this).getProductName());
        toolbar.setTitleTextColor(Color.BLACK);

        progressDialog = new ProgressDialog(After_Request_Btn.this);
        mSearch_et = (AutoCompleteTextView) findViewById(R.id.drop_location);
        requestPremission();

        date = (TextView) findViewById(R.id.date);
        cleart =(ImageView)findViewById(R.id.cleart);
        cleart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch_et.setText("");
            }
        });
        quantity_tv = (EditText) findViewById(R.id.aft_btn_quantity_et);
        submit = (Button) findViewById(R.id.aft_btn_btn_submit);
        submit.setOnClickListener(this);
        unit_spinner = (Spinner) findViewById(R.id.aft_btn_unit_spinner);
        unit_spinner.setOnItemSelectedListener(this);
        Bundle bundle = getIntent().getExtras();

        listdata = bundle.getStringArrayList("data");
        Log.d("Spiner list on Activity",listdata+"");

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listdata);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        unit_spinner.setAdapter(aa);
        date.setOnClickListener(this);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void requestPremission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        init();
    }
    public void timeCalculate(LatLng source_LatLng,LatLng destination_LatLng){

        String requestApi = SharedPrefManager.getDirectionsUrl(source_LatLng,destination_LatLng);
        Log.d(TAG,"getDirection requestApi: "+requestApi);
        HashMap objectNew = new HashMap();
        new WebTask(After_Request_Btn.this, requestApi, objectNew, After_Request_Btn.this, RequestCode.CODE_Direction_Api, 3);
    }
    public void vehicle_and_estimatePrice(){
        Log.d(TAG,"vehicle_and_estimatePrice: ");
        date_string = date.getText().toString();
        address_string = mSearch_et.getText().toString();
        HashMap objectNew = new HashMap();
        if (TextUtils.isEmpty(quantity_tv.getText())){
            quantity_tv.setError(getString(R.string.notempty));
            quantity_tv.requestFocus();
        }else if (TextUtils.isEmpty(date_string)){
            Toast.makeText(this,R.string.select_date,Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(address_string)){
            mSearch_et.setError("Select Address");
            mSearch_et.requestFocus();
        }
        else if (status.equals("OVER_QUERY_LIMIT")){
          //  Toast.makeText(this,status,Toast.LENGTH_LONG).show();
        } else {
            quantity_string = quantity_tv.getText().toString();
            new WebTask(After_Request_Btn.this, WebUrls.BASE_URL+WebUrls.GetByUnitQty_Api+"quantity="+quantity_tv.getText()+"&unit="+feed.getAft_s_unit(), objectNew, After_Request_Btn.this, RequestCode.CODE_GetByUnitQty, 3);
        }

    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mGoogleApiClient_after_srch_btn = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearch_et.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient_after_srch_btn,
                LAT_LNG_BOUNDS, null);
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
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOexception" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
//            Toast.makeText(getContext(),address.toString(),Toast.LENGTH_LONG).show();
            lat = String.valueOf(address.getLatitude());
            lng = String.valueOf(address.getLongitude());
//            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unit_string = unit_spinner.getSelectedItem().toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                datePikerMethod();
                break;
            case R.id.aft_btn_btn_submit:
                vehicle_and_estimatePrice();
                break;

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void datePikerMethod() {
        //Calendar class instance and get current year , month and day from calendar;
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);//current year;
        int month = c.get(Calendar.MONTH);//current month;
        int day = c.get(Calendar.DAY_OF_MONTH);//current day;

        //date picker dialog;
        datePickerDialog = new DatePickerDialog(After_Request_Btn.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year_d, int monthOfYear, int dayOfMonth) {
                // set day of month , month and year value in the  text
                date.setText(year_d + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
        datePickerDialog.show();
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (taskcode == RequestCode.CODE_Direction_Api){
            Log.d(TAG, "Response code: " + response);
            try {

                 JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray jsonArray = jsonObject.optJSONArray("routes");
                status = jsonObject.optString("status");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject routeObj = jsonArray.optJSONObject(i);

                    JSONArray legsArray= routeObj.optJSONArray("legs");
                    JSONObject legsObj = legsArray.optJSONObject(0);
                    JSONObject distanceObj = legsObj.optJSONObject("distance");
                    distance = distanceObj.optString("text");
                    distancevalue = distanceObj.optDouble("value");
                    dis_tot = distancevalue/1000;
                    //SharedPrefManager.setBookNowDistance("Distance",distance);
                    JSONObject durationObj = legsObj.optJSONObject("duration");
                    duration = durationObj.optString("text");
                    durationvalue = (durationObj.optDouble("value"));
                    dur_tot = durationvalue/3600;
                    //SharedPrefManager.setBookNowDuration("Duration",duration);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_GetByUnitQty){
            vehicle_id.clear();
            vehicle_nm.clear();
            Log.d(TAG, "GetByUnitQty_Response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsondata = successObj.optJSONArray("data");
                if (jsondata.length()>0 && jsondata!=null){
                    for (int i=0;i<jsondata.length();i++){
                        JSONObject Obj = jsondata.optJSONObject(i);
                        vehicle_id.add(Obj.optString("id"));
                        vehicle_nm.add(Obj.optString("name"));
                    }

                    Intent intent =new Intent(After_Request_Btn.this,Select_Vehicle.class);

                    intent.putExtra("quantity",quantity_tv.getText());
                    intent.putExtra("unit",feed.getAft_s_unit());
                    intent.putExtra("unitPrice",feed.getAft_s_price());
                    intent.putExtra("materialId",feed.getAft_s_mat_id());
                    startActivity(intent);
                }else {
                    Toast.makeText(this, R.string.data_not_found,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
       /*
           -------------------------------google places API autocomplete suggestions
     */
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SharedPrefManager.getInstance(getApplicationContext()).hideSoftKeyBoard(After_Request_Btn.this);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient_after_srch_btn, placeId);
            placeResult.setResultCallback(mUpdatePlaceDitailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDitailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: place query did not complete successfully." + places.getStatus().toString());
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
                Log.d(TAG, "OnResult: place details " + mPlace.toString());

            } catch (NullPointerException e) {
                Log.d(TAG, "OnResult: NullPointerException " + e.getMessage());
            }

            latLng_drop_location = mPlace.getLatLng();
            lat = String.valueOf(latLng_drop_location.latitude);
            lng = String.valueOf(latLng_drop_location.longitude);

            timeCalculate(mPlace.getLatLng(),new LatLng(Double.valueOf(feed.getAft_s_lat()),Double.valueOf(feed.getAft_s_lng())));
            places.release();
        }
    };
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(After_Request_Btn.this)) {
            if (SharedPrefManager.getLangId(After_Request_Btn.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(After_Request_Btn.this,RequestCode.LangId));
            } else {
                Toast.makeText(After_Request_Btn.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(After_Request_Btn.this,RequestCode.LangId, langval);
    }
}
