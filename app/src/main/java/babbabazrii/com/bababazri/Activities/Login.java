package babbabazrii.com.bababazri.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Common.Network;
import babbabazrii.com.bababazri.Common.NetworkChangeReceiver;
import babbabazrii.com.bababazri.Common.SessionManagement;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class Login extends AppCompatActivity implements WebCompleteTask, View.OnClickListener {

    WebSettings webSettings;
    RelativeLayout netLayout;
    LinearLayout mainlayout;
    EventBus bus = EventBus.getDefault();
    NetworkChangeReceiver myReceiver = new NetworkChangeReceiver();


    private static final String TAG = "FACELOG";
    Button fb_login_btn,loginbtn;
    TextView signuptv,change_tv;
    Context context;
    EditText et_mobile_no,et_password;
    String mobile_string,password_stirng,firebase_token;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private static final int F_REQ_CODE = 9002;
    String fb_access_token,id,name,email,profilePicUrl;
    boolean isProgress = true;
    ProgressDialog progressDialog = null;
    CheckBox forgot_pass;
    SignInButton gp_login_btn;
    // Session Manager Class
    SessionManagement session;
    ImageView logo_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
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
        setContentView(R.layout.activity_login);

        fb_login_btn = (Button) findViewById(R.id.fb_login_btn);
        loginbtn = (Button)findViewById(R.id.login_btn);
        signuptv = (TextView)findViewById(R.id.signup_btn);
        et_mobile_no = (EditText)findViewById(R.id.et_mobileno);
        et_password = (EditText)findViewById(R.id.et_password);
        forgot_pass = (CheckBox)findViewById(R.id.forgotpassword);
        gp_login_btn = (SignInButton) findViewById(R.id.google_login_bn);
        logo_login = (ImageView)findViewById(R.id.logo_login);
        change_tv = (TextView)findViewById(R.id.change_lang_btn);


        context = this;
        Network.activityname = "webview";

        progressDialog = new ProgressDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.
                    CONNECTIVITY_ACTION));
        }
        if (!bus.isRegistered(this)) {
            Log.w("-------", " register bus 1111 ");
            bus.register(this);
        }

        mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
        netLayout = (RelativeLayout) findViewById(R.id.neterror);
        if (Network.isConnectingToInternet(context)) {
            mainlayout.setVisibility(View.VISIBLE);
            netLayout.setVisibility(View.GONE);
            Log.w("------", "on create if net ");
            mAuth = FirebaseAuth.getInstance();
            webshow();
        } else {
            Log.w("------", "on create else no net ");
            mainlayout.setVisibility(View.GONE);
            netLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Subscribe
    public void onEvent(String name) {
        if (name.equalsIgnoreCase("webview") && netLayout.getVisibility() == View.VISIBLE) {
            netLayout.setVisibility(View.GONE);
            mainlayout.setVisibility(View.VISIBLE);
            Log.w("------", "on event ");
            webshow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unregisterReceiver(myReceiver);
        }
//        mAuth.addAuthStateListener(mAuthLisenter);
    }


    private void webshow() {

        progressDialog = new ProgressDialog(this);
//        isLocationServiceEnabled();
        // Session Manager
        session = new SessionManagement(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        if (session.isLoggedIn()){
            if (progressDialog != null)
                progressDialog.dismiss();
            Intent intent = new Intent(Login.this, User_Profile.class);
//            Toast.makeText(getApplication(), R.string.login_success, Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
//        if (SharedPrefManager.getLangId(Constants.LangId).compareTo("hi")==0){
//            setLangRecreate(SharedPrefManager.getLangId(Constants.LangId));
//        }else {
//            setLangRecreate(SharedPrefManager.getLangId(Constants.LangId));
//        }

        mCallbackManager = CallbackManager.Factory.create();
        fb_login_btn.setOnClickListener(this);
        gp_login_btn.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
        signuptv.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);
        change_tv.setOnClickListener(this);
    }

//        fb_login_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.change_lang_btn:
                Intent intent = new Intent(Login.this,SelectLanguage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.fb_login_btn:
                fbLoginMethod();
                break;
            case R.id.google_login_bn:
                gPlusLoginMethod();
                break;
            case R.id.login_btn:
                loginMethod();
                break;
            case R.id.signup_btn:
                signUpMethod();
                break;
            case R.id.forgotpassword:
                boolean checked = ((CheckBox)v).isChecked();
                if (checked)
                    forgotPassMethod();
                else
                    Toast.makeText(Login.this,"Not checked Forgot Password",Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void forgotPassMethod() {
        Intent intent = new Intent(Login.this, ForgotPassword.class);
//        Pair[] pairs = new Pair[3];
//        pairs[0] = new Pair<View,String>(logo_login,"logoTransition");
//        pairs[1] = new Pair<View,String>(et_mobile_no,"firsetTransition");
//        pairs[2] = new Pair<View,String>(loginbtn,"btnTransition");

//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);

        startActivity(intent);
    }

    private void signUpMethod() {
        Intent intent = new Intent(Login.this,Signup.class);
//        Pair[] pairs = new Pair[3];
//        pairs[0] = new Pair<View,String>(logo_login,"logoTransition");
//        pairs[1] = new Pair<View,String>(et_mobile_no,"firsetTransition");
//        pairs[2] = new Pair<View,String>(loginbtn,"btnTransition");

//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);

        startActivity(intent);
    }

    private void loginMethod() {
        mobile_string = et_mobile_no.getText().toString().trim();
        password_stirng = et_password.getText().toString().trim();
        loginServiceMethod();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser!=null)
//        {
//            updateUI();
//        }
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        progressDialog.show();
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    fb_login_btn.setEnabled(true);
                    updateUI();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    fb_login_btn.setEnabled(true);
//                            updateUI();
                }

                // ...
            }
        });
    }
    private void updateUI() {
        if (progressDialog!=null)
        progressDialog.dismiss();

        Intent main = new Intent(Login.this, User_Profile.class);
        Toast.makeText(Login.this,"Your are Loged In with Facebook", Toast.LENGTH_LONG).show();
        startActivity(main);
        finish();

    }
    private void gPlusLoginMethod() {

    }
    private void fbLoginMethod() {
        fb_login_btn.setEnabled(false);
        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d(TAG,"facebook: onSuccess" + loginResult);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.e("response: ", response + "");
                                try {
                                    id = object.optString("id").toString();
                                    email = object.optString("email").toString();
                                    name = object.optString("name").toString();
                                    profilePicUrl = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large";
                                    fb_access_token = loginResult.getAccessToken().getToken();

                                    SharedPrefManager.getInstance(Login.this).storeFbid(id);
                                    SharedPrefManager.getInstance(Login.this).storeFbname(name);
                                    SharedPrefManager.getInstance(Login.this).storeFbemail(email);
                                    SharedPrefManager.getInstance(Login.this).storeFbimage(profilePicUrl);
                                    SharedPrefManager.getInstance(Login.this).storeFbAccessToken(fb_access_token);

                                    Log.d("imageFB", profilePicUrl);
                                    Log.d("FB_ID:", id);
                                    Log.d("FB_NAME:", name);
                                    Log.d("FB_EMAIL",email);
                                    Log.d("fb_access_token",fb_access_token);


//                                            checkFBLogin(id, email, name, profilePicUrl);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                                        finish();
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,friends,likes,hometown,education,work");
                request.setParameters(parameters);
                request.executeAsync();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"facebook: onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG,"facebook: onError");
            }
        });
    }
    public void loginServiceMethod() {
        if (TextUtils.isEmpty(mobile_string)) {
            et_mobile_no.setError(getString(R.string.notempty));
            et_mobile_no.requestFocus();
        } else if (TextUtils.isEmpty(password_stirng)) {
            et_password.setError(getString(R.string.notempty));
            et_password.requestFocus();
        }else if (mobile_string.length()>10||mobile_string.length()<10) {
            et_mobile_no.setError(getString(R.string.mobile_no_not_valid));
            et_mobile_no.requestFocus();
        } else if (password_stirng.length() < 6) {
            et_password.setError(getString(R.string.password_validation));
            et_password.requestFocus();
        } else {
            firebase_token = SharedPrefManager.getInstance(this).getFireBaseToken();
            HashMap objectNew = new HashMap();
            objectNew.put("realm", "user");
            objectNew.put("mobile", mobile_string);
            objectNew.put("password", password_stirng);
            objectNew.put("firebaseToken", firebase_token);
            objectNew.put("ln",SharedPrefManager.getLangId(this,RequestCode.LangId));

            session.createLoginSession(mobile_string, firebase_token);
            new WebTask(Login.this, WebUrls.BASE_URL + WebUrls.login_api, objectNew, Login.this, RequestCode.CODE_Login, 1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        progressDialog.show();

        if (taskcode==RequestCode.CODE_Login)
        {
            Log.d("response",response);
            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.optJSONObject("success");
                JSONObject data = success.optJSONObject("data");
                String id = data.optString("id");
                Integer ttl = data.getInt("ttl");
                String created = data.optString("created");
                String userId = data.optString("userId");
                String firebaseToken=data.optString("firebaseToken");

                JSONObject user = data.optJSONObject("user");
                String realm = user.optString("realm");
                String user_id = user.optString("id");
                String mobile = user.optString("mobile");
                String user_createdAt = user.optString("updatedAt");
                String user_updatedAt = user.optString("updatedAt");
                Boolean mobileVerified = user.getBoolean("mobileVerified");
                String firstName = user.optString("firstName");
                String lastName = user.optString("lastName");
                String fullName = user.optString("fullName");
//                JSONArray documents = user.getJSONArray("documents");

                JSONObject msg = success.optJSONObject("msg");
                String replyCode = msg.optString("replyCode");
                String replyMessage = msg.optString("replyMessage");

                String access_token = data.optString("access_token");
                SharedPrefManager.getInstance(getApplicationContext()).storeUserName(fullName);
                SharedPrefManager.getInstance(getApplicationContext()).storeAccessToken(access_token);
                SharedPrefManager.getInstance(getApplicationContext()).storeRegPeopleId(id);
                if (id.equals(access_token) && realm.equals("user"))
                {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    Intent intent = new Intent(Login.this, User_Profile.class);
                    Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplication(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        forgot_pass.setChecked(false);
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void setLangRecreate(String langval) {
        Locale locale;
        Configuration config = getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPrefManager.setLangId(Login.this,RequestCode.LangId, langval);
        //   MyApplication.setIslagChange(ProfileActivity.this, true);
//         Objects.requireNonNull(getActivity()).recreate();
        //Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new Profile()).commit();
    }


//    private GoogleApiClient googleApiClient;
//    final static int REQUEST_LOCATION = 199;
//    // Location service is on or NOt
//    public boolean isLocationServiceEnabled() {
//        this.setFinishOnTouchOutside(true);
//
//        // Todo Location Already on  ... start
//        final LocationManager manager = (LocationManager) Login.this.getSystemService(Context.LOCATION_SERVICE);
//        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Login.this)) {
//            Toast.makeText(Login.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        // Todo Location Already on  ... end
//
//        if (!hasGPSDevice(Login.this)) {
//            Toast.makeText(Login.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Login.this)) {
//            Log.e("keshav", "Gps already enabled");
//            Toast.makeText(Login.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
//            enableLoc();
//            return false;
//        } else {
//            Log.e("keshav", "Gps already enabled");
//            Toast.makeText(Login.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return true;
//    }
//    private boolean hasGPSDevice(Context context) {
//        final LocationManager mgr = (LocationManager) context
//                .getSystemService(Context.LOCATION_SERVICE);
//        if (mgr == null)
//            return false;
//        final List<String> providers = mgr.getAllProviders();
//        if (providers == null)
//            return false;
//        return providers.contains(LocationManager.GPS_PROVIDER);
//    }
//
//    private void enableLoc() {
//        Log.e("enableLoc", "Gps already enabled");
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(Login.this)
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                        @Override
//                        public void onConnected(Bundle bundle) {
//
//                        }
//
//                        @Override
//                        public void onConnectionSuspended(int i) {
//                            googleApiClient.connect();
//                        }
//                    })
//                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
//                        @Override
//                        public void onConnectionFailed(ConnectionResult connectionResult) {
//
//                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
//                        }
//                    }).build();
//            googleApiClient.connect();
//
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(30 * 1000);
//            locationRequest.setFastestInterval(5 * 1000);
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//
//            builder.setAlwaysShow(true);
//
//            PendingResult<LocationSettingsResult> result =
//                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//                @Override
//                public void onResult(LocationSettingsResult result) {
//                    final Status status = result.getStatus();
//                    switch (status.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            try {
//                                // Show the dialog by calling startResolutionForResult(),
//                                // and check the result in onActivityResult().
//                                status.startResolutionForResult(Login.this, REQUEST_LOCATION);
//
////                                finish();
//                            } catch (IntentSender.SendIntentException e) {
//                                // Ignore the error.
//                            }
//                            break;
//                        case LocationSettingsStatusCodes.CANCELED:
//                            // The user was asked to change settings, but chose not to
//                            finish();
//                            break;
//
//                    }
//                }
//            });
//
//
//        }
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Login.this)) {
            if (SharedPrefManager.getLangId(Login.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Login.this,RequestCode.LangId));
            } else {
                Toast.makeText(Login.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
            }
        }
    }
}
