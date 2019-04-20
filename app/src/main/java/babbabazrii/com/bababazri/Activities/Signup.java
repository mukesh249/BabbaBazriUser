package babbabazrii.com.bababazri.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

public class Signup extends AppCompatActivity implements WebCompleteTask,View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Choose Location";
    private static String add;
    Boolean isSignupComplete,mobileVerified;
    String lat,lng;
    AutoCompleteTextView et_address;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    private PlaceInfo mPlace;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    Button signup_submit_btn;
    EditText et_first_name,et_last_name,et_phoneno,et_password,et_confirm_password;
    String first_name_string,last_name_string,phoneno_string,address_string,password_string,confirm_pass_string;
    ProgressDialog progressDialog = null;
    Toolbar toolbar_sign_up;
    JSONObject addressObj=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_signup);
        toolbar_sign_up =(Toolbar)findViewById(R.id.toolbar_signup);
        setSupportActionBar(toolbar_sign_up);
        setTitle(getString(R.string.signup));
        signup_submit_btn = (Button)findViewById(R.id.signup_submit_btn);
        et_first_name = (EditText)findViewById(R.id.et_first_name_sign);
        et_last_name = (EditText)findViewById(R.id.et_last_name_sign);
        et_phoneno = (EditText)findViewById(R.id.et_mobileno_sign);
        et_address = (AutoCompleteTextView) findViewById(R.id.et_address_sign);
        et_password = (EditText)findViewById(R.id.et_password_sign);
        et_confirm_password = (EditText)findViewById(R.id.et_confirm_pass_sign);
        progressDialog = new ProgressDialog(this);
        init();
        signup_submit_btn.setOnClickListener(this);
//toolbar back button color and icon change
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_black);
        upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        finishAfterTransition();
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_submit_btn:
                signupMethod();
        }
    }
    private void signupMethod() {
        first_name_string = et_first_name.getText().toString().trim();
        last_name_string = et_last_name.getText().toString().trim();
        phoneno_string = et_phoneno.getText().toString().trim();
        address_string = et_address.getText().toString().trim();
        password_string = et_password.getText().toString().trim();
        confirm_pass_string = et_confirm_password.getText().toString().trim();


        if (TextUtils.isEmpty(first_name_string)) {
            et_first_name.setError(getString(R.string.notempty));
            et_first_name.requestFocus();
        } else if (TextUtils.isEmpty(last_name_string)) {
            et_last_name.setError(getString(R.string.notempty));
            et_last_name.requestFocus();
        }else if (TextUtils.isEmpty(phoneno_string)) {
            et_phoneno.setError(getString(R.string.notempty));
            et_phoneno.requestFocus();
        }else if (phoneno_string.length()>10||phoneno_string.length()<10) {
            et_phoneno.setError(getString(R.string.mobile_no_not_valid));
            et_phoneno.requestFocus();
        } else if (addressObj == null && TextUtils.isEmpty(address_string)) {
            et_address.setError(getString(R.string.enter_valid_add));
            et_address.requestFocus();
        } else if (TextUtils.isEmpty(password_string)) {
            et_password.setError(getString(R.string.notempty));
            et_password.requestFocus();
        }  else if (TextUtils.isEmpty(confirm_pass_string)) {
            et_confirm_password.setError(getString(R.string.notempty));
            et_confirm_password.requestFocus();
        }  else if (password_string.length() < 6) {
            et_password.setError(getString(R.string.password_validation));
            et_password.requestFocus();
        } else if (password_string.compareTo(confirm_pass_string)!=0){
            et_password.setError(getString(R.string.passs_and_confirm_pass));
            et_password.requestFocus();
        } else {
            try {
                    HashMap objectNew = new HashMap();
                    objectNew.put("realm", "user");
                    objectNew.put("firstName", first_name_string);
                    objectNew.put("lastName", last_name_string);
                    objectNew.put("address", addressObj.toString());
                    objectNew.put("mobile", phoneno_string);
                    objectNew.put("password", password_string);

                    new WebTask(Signup.this, WebUrls.BASE_URL + WebUrls.signup_api, objectNew, Signup.this, RequestCode.CODE_Signup, 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode)
    {
        android.util.Log.d("response",response);
        if (taskcode==RequestCode.CODE_Signup)
        {
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();

                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.optJSONObject("success");
                JSONObject data = success.optJSONObject("data");

                String adminApproval = data.optString("adminApproval");
                String realm = data.optString("realm");
                String id = data.optString("id");
                String fullName = data.optString("fullName");
                String firstName = data.optString("firstName");
                String lastName = data.optString("lastName");

                JSONObject addressObj = data.optJSONObject("address");
                addressObj.optString("address");
                JSONObject locationObj = addressObj.optJSONObject("location");
                locationObj.optString("lat");
                locationObj.optString("lng");


                String mobile = data.optString("mobile");
                String createdAt = data.optString("createdAt");
                String updatedAt = data.optString("updatedAt");
                mobileVerified = data.getBoolean("mobileVerified");
                isSignupComplete = data.getBoolean("isSignupComplete");

                JSONObject signupOtp = data.optJSONObject("signupOtp");
                String createdAt_s = signupOtp.optString("createdAt");
                String expireAt = signupOtp.optString("expireAt");
                Integer otp = signupOtp.optInt("otp");
                Log.d("otp_signup", String.valueOf(otp));


                JSONObject msg = success.optJSONObject("msg");
                String replyCode = msg.getString("replyCode");
                String replyMessage = msg.getString("replyMessage");

                SharedPrefManager.getInstance(getApplicationContext()).storeRegPeopleOtp(otp);
                SharedPrefManager.getInstance(getApplicationContext()).storeSignPeopleId(id);
                if (realm.equals("user"))
                {
                    Intent intent = new Intent(Signup.this, Verify_MobileNo.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(Signup.this,R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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

        et_address.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,
                LAT_LNG_BOUNDS,null);
        et_address.setAdapter(mPlaceAutocompleteAdapter);

        et_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        String searchString = et_address.getText().toString();
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
            SharedPrefManager.getInstance(getApplicationContext()).hideSoftKeyBoard(Signup.this);

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
             lat = String.valueOf(latlng.latitude);
             lng = String.valueOf(latlng.longitude);
            addressObj = new JSONObject();
            try {
                JSONObject locationobj = new JSONObject();
                locationobj.put("lat",lat);
                locationobj.put("lng", lng);
                addressObj.put("address", et_address.getText().toString().trim());
                addressObj.put("location", locationobj);
            }catch (Exception e){
                e.printStackTrace();
            }


            Log.d(TAG,"OnResult: place lat lng details " + lat + ","+ lng);

            places.release();
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Signup.this)) {
            if (SharedPrefManager.getLangId(Signup.this, RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Signup.this,RequestCode.LangId));
            } else {
                Toast.makeText(Signup.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Signup.this,RequestCode.LangId, langval);
    }

}
