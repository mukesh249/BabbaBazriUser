package babbabazrii.com.bababazri;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class SharedPrefManager {

    private static final String SHARE_PREF_NAME = "fcmsharedprefdemo";
    private static final String KEY_ACCESS_TOKEN = "fcmtoken";
    private static String islagChange;

    private static Context mCtx;
    private static SharedPrefManager mInstance;
    public static SharedPreferences sp;
    private SharedPrefManager(Context context) {
        mCtx = context;
        sp = mCtx.getSharedPreferences("BabbaBazri", Context.MODE_PRIVATE);
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }
    // firebae stroe token
    public boolean storeFireBaseToken(String token) {
        Log.d("fcm token", token);
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.commit();
        return true;
    }

    public String getFireBaseToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }

    // Store Regestration as User, Agent and Owner.
    public boolean storeRegAs(String reg_as) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_as", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("key_reg", reg_as);
        editor.commit();
        return true;
    }
    public String getRegAs() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_as", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("key_reg", "");
    }
    // Store People Id Regestration as User, Agent and Owner.
    public boolean storeRegPeopleId(String reg_as) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_people_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("key_reg_people_id", reg_as);
        editor.commit();
        return true;
    }
    public String getRegPeopleId() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_people_id", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("key_reg_people_id", "");
    }

    //Store Otp Regestration as User, Agent and Owner.
    public boolean storeRegPeopleOtp(Integer otp) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_people_otp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putInt("key_reg_people_otp", otp);
        editor.commit();
        return true;
    }

    public Integer getRegPeopleOtp() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_people_otp", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getInt("key_reg_people_otp", 0);
    }
    //Store Access Token Regestration as User, Agent and Owner.
    public boolean storeAccessToken(String access_token) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_access_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("key_reg_access_token", access_token);
        editor.commit();
        return true;
    }
    public String getAccessToken() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_access_token", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("key_reg_access_token", "");
    }
    public boolean storeFbAccessToken(String access_token) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_fb_access_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("key_fb_reg_access_token", access_token);
        editor.commit();
        return true;
    }

    public String getFbAccessToken() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("reg_fb_access_token", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("key_fb_reg_access_token", "");
    }
    public static void showMessage(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showMessageOtp(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    public void hideSoftKeyBoard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            //check if no view has focus.
            View v = activity.getCurrentFocus();
            if (v == null)
                return;
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void storeIsLoggedIn(Boolean  isLoggedIn) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putBoolean("key", isLoggedIn);
        editor.commit();
    }

    public boolean getIsLoggedIn(boolean default_value) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getBoolean("key", default_value);
    }
    public void storeIsLoggedInG(Boolean  isLoggedIn) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putBoolean("key", isLoggedIn);
        editor.commit();
    }
    public boolean getIsLoggedInG(boolean default_value) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("login", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getBoolean("key", default_value);
    }

    public boolean storeFbname(String name) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("fb_name_key", name);
        editor.commit();
        return true;
    }

    public String getfbname() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_name", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("fb_name_key", "");
    }

    public boolean storeFbemail(String email) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_email", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("fb_email_key", email);
        editor.commit();
        return true;
    }

    public String getfbemail() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_email", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("fb_email_key", "");
    }

    public boolean storeFbid(String id) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("fb_id_key", id);
        editor.commit();
        return true;
    }

    public String getFbid() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_id", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("fb_id_key", "");
    }

    public boolean storeFbimage(String image) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_image", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("fb_image_key", image);
        editor.commit();
        return true;
    }

    public String getFbimage() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("fb_image", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("fb_image_key", "");
    }

    public boolean storeLatLng(double lat,double lng) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("LAT", String.valueOf(lat));
        editor.putString("LNG", String.valueOf(lng));
        editor.commit();
        return true;
    }

    public void storeLatLngNearBy(double lat,double lng) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("LatLngNearBy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("LATn", String.valueOf(lat));
        editor.putString("LNGn", String.valueOf(lng));
        editor.commit();

    }

    public void storeDropLocation(double lat,double lng) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("DropLocation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("LATdl", String.valueOf(lat));
        editor.putString("LNGdl", String.valueOf(lng));
        editor.commit();

    }

    public void storeProductId(String id) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("product_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("product_id_key", id);
        editor.commit();
    }

    public String getProductid() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("product_id", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("product_id_key", "");
    }

    public void storeProductName(String id) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("product_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("product_name_key", id);
        editor.commit();
    }

    public String getProductName() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("product_name", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("product_name_key", "");
    }



    public void storeSubPoductId(String id) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("sub_product_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("sub_product_id_key", id);
        editor.commit();
    }

    public String getSubPoductid() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("sub_product_id", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("sub_product_id_key", "");
    }

    public void storeSubPoductName(String id) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("sub_product_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("sub_product_name_key", id);
        editor.commit();
    }

    public String getSubPoductName() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("sub_product_name", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("sub_product_name_key", "");
    }

    public String getLatLng() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("LatLng", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("fb_image_key", "");
    }

    public static void setDouble(String name, Double value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, value.toString());
        editor.commit();
    }
    public static void setSharedPrefString(String preffConstant, String stringValue) {
        if (!TextUtils.isEmpty(stringValue)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(preffConstant, stringValue);
            editor.commit();
        }
    }
    public static String getSharedPrefString(String preffConstant) {
        String stringValue = "";
        stringValue = sp.getString(preffConstant, "");
        return stringValue;
    }
    public static Boolean getIslagChange(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(islagChange, false);
    }

    public static void setIslagChange(Context context, Boolean isUserLogin) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(islagChange, isUserLogin);
        editor.commit();
    }

    public static void setLangId(Context ctx,String preffConstant, String stringValue){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("Lang",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preffConstant,stringValue);
        editor.commit();
    }
    public static String getLangId(Context ctx,String preffConstant){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("Lang",Context.MODE_PRIVATE);
        return sharedPreferences.getString(preffConstant,"");
    }

//    public static void setLangId(String preffConstant, String stringValue) {
//        if (!TextUtils.isEmpty(stringValue)) {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString(preffConstant, stringValue);
//            editor.commit();
//        }
//    }
//    public static String getLangId(String preffConstant) {
//        String stringValue = "";
//        stringValue = sp.getString(preffConstant, "");
//        return stringValue;
//    }



    public static void setUserID(String type ,String userId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type,userId);
        editor.commit();
    }
    public static String getUserID(String type) {
        String e = sp.getString(type,"");
        return e;
    }
    public static void setUserTypeID(String userType, String id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userType,id);
        editor.commit();
    }
    public static String getUserTypeID(String userType){
        String e = sp.getString(userType,"");
        return e;

    }
    public static void setBookNowDistance(String preffConstant, String stringValue) {
        if (!TextUtils.isEmpty(stringValue)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(preffConstant, stringValue);
            editor.commit();
        }
    }
    public static String getBookNowDistance(String preffConstant) {
        String stringValue = "";
        stringValue = sp.getString(preffConstant, "");
        return stringValue;
    }
    public static void setBookNowDuration(String preffConstant, String stringValue) {
        if (!TextUtils.isEmpty(stringValue)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(preffConstant, stringValue);
            editor.commit();
        }
    }
    public static String getBookNowDuraiton(String preffConstant) {
        String stringValue = "";
        stringValue = sp.getString(preffConstant, "");
        return stringValue;
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String alternative = "alternatives=true";
        String key = "key="+"AIzaSyB7tIYFdkzHflflwJOdiX8LUrPSILU_-T4";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + alternative+ "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.e("url", url);

        return url;
    }
    // Store People Id Regestration as User, Agent and Owner.
    public boolean storeSignPeopleId(String reg_as) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("sign_people_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("key_sign_people_id", reg_as);
        editor.commit();
        return true;
    }
    public String getSignPeopleId() {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("sign_people_id", Context.MODE_PRIVATE);
        return sharedPreferencesReg.getString("key_sign_people_id", "");
    }
    public void storeUserName(String fullName) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("UserName" ,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName_Key",fullName);
        editor.commit();
    }
    public String getUserName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("UserName",Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserName_Key","");
    }

    public void storeOrderTime(String time){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("Order_Time",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Order_Time_Key",time);
        editor.commit();
    }
    public String getOrderTime(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("Order_Time",Context.MODE_PRIVATE);
        return sharedPreferences.getString("Order_Time_Key","");
    }

    public void storeAddress(String name) {
        SharedPreferences sharedPreferencesReg = mCtx.getSharedPreferences("Address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesReg.edit();
        editor.putString("Address_key", name);
        editor.commit();
    }
    public String getAddress(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("Address",Context.MODE_PRIVATE);
        return sharedPreferences.getString("Address_key","");
    }

}
