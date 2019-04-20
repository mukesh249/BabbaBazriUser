package babbabazrii.com.bababazri.Activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Locale;

import babbabazrii.com.bababazri.Adapters.GooglePlacesAutocompleteAdapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Common.Constants;
import babbabazrii.com.bababazri.Fragments.Home;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class SearchingDropLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
    ,GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{


    protected GoogleApiClient googleApiClient;
    private static final LatLngBounds myBounds = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

    private TextView mPrimaryAddress, mSecondaryAddress;
    private Double mLatitude, mLongitude;
    private EditText myATView;
    private RecyclerView myRecyclerView;
    private LinearLayoutManager myLinearLayoutManager;
    private GooglePlacesAutocompleteAdapter mAutoCompleteAdapter;
    ImageView clearText;
    Toolbar toolbar_searching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_drop_location);
        buildGoogleApiClient();
        myATView = (EditText)findViewById(R.id.autocomplete_tv);
        clearText = (ImageView)findViewById(R.id.clearText);
        mAutoCompleteAdapter =  new GooglePlacesAutocompleteAdapter(this, R.layout.search_row,
                googleApiClient, myBounds, null);
        myRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_search);
        myLinearLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLinearLayoutManager);
        myRecyclerView.setAdapter(mAutoCompleteAdapter);
        clearText.setOnClickListener(this);
        toolbar_searching = (Toolbar)findViewById(R.id.toolbar_searching);
        setSupportActionBar(toolbar_searching);
        getSupportActionBar().setTitle(R.string.enter_drop_location);

        myATView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().equals("")&& googleApiClient.isConnected()){
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if (!googleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(),"Google Api Client not connected",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myRecyclerView.addOnItemTouchListener(
                new Recycler_Listener(this, new Recycler_Listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final GooglePlacesAutocompleteAdapter.AT_Place item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.placeAddress2);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(googleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (!places.getStatus().isSuccess()) {
                                    // Request did not complete successfully
                                    Log.e("", "Place query did not complete. Error: " + places.getStatus().toString());
                                    places.release();
                                    return;
                                }
                                Place place = places.get(0);
                                mLatitude = place.getLatLng().latitude;
                                mLongitude = place.getLatLng().longitude;
//                                mPrimaryAddress.setText(place.getName());
//                                mSecondaryAddress.setText(place.getAddress());
                                Home.getInstance().nearByDriversWithLatLng(new LatLng(mLatitude,mLongitude),place.getName().toString()+" "+place.getAddress().toString());
                                finish();
                                places.release();
                            }
//                                if(places.getCount()==1){
//                                    //Do the things here on Click.....
//                                    Home.getInstance().nearByDriversWithLatLng(places.get(0).getLatLng(),places.get(0).getAddress().toString());
////                                    Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }else {
//                                    Toast.makeText(getApplicationContext(),"OOPs!!! Something went wrong...",Toast.LENGTH_SHORT).show();
//                                }
//                            }
                        });
                        Log.i("TAG", "Clicked: " + item.placeAddress2);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );
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
        onBackPressed();
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v==clearText){
            myATView.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            googleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(googleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(SearchingDropLocation.this)) {
            if (SharedPrefManager.getLangId(SearchingDropLocation.this, RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(SearchingDropLocation.this,RequestCode.LangId));
            } else {
                Toast.makeText(SearchingDropLocation.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(SearchingDropLocation.this,RequestCode.LangId, langval);
    }
}
