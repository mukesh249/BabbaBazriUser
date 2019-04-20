package babbabazrii.com.bababazri.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.marcoscg.dialogsheet.DialogSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Fragments.Home;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.NearbyDrivers_Model;

public class Order_Confirm extends AppCompatActivity implements View.OnClickListener, WebCompleteTask {
    TextView tv_source,tv_destination;
    TextView cash_onclick,card_onclick,total_price_booknow,price_details;
    Toolbar toolbar_order;
    LatLng source_LatLng,destination_LatLng;

    public static String distance,duration,status;
    public static double distancevalue,durationvalue,total_pr;
    public static double dis_tt,dur_tt;

    ArrayList<NearbyDrivers_Model> nearbyDriversModelArrayList = new ArrayList<>();
    String req_id;
    private String TAG="Order_Confirm";
    static Order_Confirm mInstance;

    TextView id_tv,price_tv,loading_tv,unloading_tv,loadKM_tv,unloadKM_tv,driverSalary_tv,driverDailyExp_tv,helper_tv,royalty_tv,tollTax_tv,driverSaving_tv,nightStay_tv,minCharge_tv,totalPrice_tv,vehicleTypeId_tv,tyres_tv,days_tv,totalKM_tv,gst_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__confirm);

        toolbar_order = (Toolbar)findViewById(R.id.toolbar_order);
        setSupportActionBar(toolbar_order);

        mInstance = this;
        tv_source = (TextView)findViewById(R.id.soure_location);
        tv_destination = (TextView)findViewById(R.id.destination_location);
        cash_onclick = (TextView) findViewById(R.id.cash_on_delivery);
        card_onclick = (TextView) findViewById(R.id.credit_debit);
        total_price_booknow = (TextView)findViewById(R.id.total_price_booknow);
        price_details = (TextView)findViewById(R.id.price_Details);
        price_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status!=null&&status.compareTo("OVER_QUERY_LIMIT")==0){
                    Toast.makeText(Order_Confirm.this,R.string.price_not_get,Toast.LENGTH_SHORT).show();
                }else {
                    updateBottom();
                }
            }
        });
        cash_onclick.setOnClickListener(this);
        cash_onclick.setEnabled(false);
        tv_destination.setText(Home.destination_address);
        tv_source.setText(Home.source_address);

        //---------------------LatLng for Tracking------------
        source_LatLng = Home.source_latlng;
        destination_LatLng = Home.destination_latlng;
        timeCalculate();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cash_on_delivery:
                orderConfirmPopUp();
                break;
        }
    }
    public void timeCalculate(){
        String requestApi = SharedPrefManager.getDirectionsUrl(source_LatLng,destination_LatLng);
        Log.d(TAG,"getDirection_requestApi: "+requestApi);
        HashMap objectNew = new HashMap();
        new WebTask(Order_Confirm.this, requestApi, objectNew, Order_Confirm.this, RequestCode.CODE_Direction_Api, 3);
    }
    private void orderConfirmPopUp() {
        Log.d(TAG,"orderConfirmPopUp: Called");
        final DialogSheet dialogSheet = new DialogSheet(Order_Confirm.this);

        dialogSheet.setTitle(R.string.app_name)
                .setMessage(R.string.order_confirm_cod)
                .setCancelable(true)
                .setPositiveButton(R.string.book_now, new DialogSheet.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestForConfirmOrder();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogSheet.OnNegativeClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Your action
                        dialogSheet.dismiss();
                    }
                })
                .setButtonsColorRes(R.color.black)  // Default color is accent
                .show();
    }

    public static Order_Confirm getInstance(){
        return mInstance;
    }
    public void RequestForConfirmOrder(){
        Log.d(TAG,"RequestForConfirmOrder: Called");

        try {
            JSONObject customerAddress = new JSONObject();
            JSONObject customerlocation = new JSONObject();
            customerlocation.put("lat",Home.destination_latlng.latitude);
            customerlocation.put("lng",Home.destination_latlng.longitude);

            customerAddress.put("formattedAddress",tv_destination.getText());
            customerAddress.put("location",customerlocation);

            JSONObject pickupAddress = new JSONObject();
            JSONObject pickuplocation = new JSONObject();
            pickuplocation.put("lat",Home.source_latlng.latitude);
            pickuplocation.put("lng",Home.source_latlng.longitude);

            pickupAddress.put("formattedAddress",tv_source.getText());
            pickupAddress.put("location",pickuplocation);

            HashMap objectNew = new HashMap();
            objectNew.put("customerAddress",customerAddress.toString());
            objectNew.put("pickupAddress",pickupAddress.toString());
            objectNew.put("driverId",Home.driver_id);
            objectNew.put("totalTime",dur_tt+"");
            objectNew.put("totalKM",dis_tt+"");
            new WebTask(Order_Confirm.this,WebUrls.BASE_URL+WebUrls.bookNow_Api,objectNew,Order_Confirm.this, RequestCode.CODE_BookNow_Api,1);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_BookNow_Api){
            try {
                Log.d(TAG,"Booknow_api" + response);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.getJSONObject("success");
                JSONObject dataObj = successObj.getJSONObject("data");
                String orderQuantity = dataObj.optString("orderQuantity");
                String unit = dataObj.optString("unit");
                String materialId = dataObj.optString("materialId");
                String materialTypeId = dataObj.optString("materialTypeId");
                String vehicleId = dataObj.optString("vehicleId");
                String orderDate = dataObj.optString("orderDate");
                String customerId = dataObj.optString("customerId");
                String driverId = dataObj.optString("driverId");
                String ownerId = dataObj.optString("ownerId");
                String vehicleTypeId = dataObj.optString("vehicleTypeId");
                String deliveryStatus =dataObj.optString("deliveryStatus");
                String bookingStatus = dataObj.optString("bookingStatus");
                String price = dataObj.optString("price");

                JSONObject messageObj = successObj.getJSONObject("msg");
                String replyCode = messageObj.optString("replyCode");
                String replyMessage = messageObj.optString("replyMessage");
                if (replyCode.equals(replyMessage)){
                    Toast.makeText(Order_Confirm.this, R.string.booking_success, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Order_Confirm.this,User_Profile.class));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_Direction_Api){
            Log.d(TAG, "Direction_api: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.optString("status").equals("OVER_QUERY_LIMIT")){
                    status = jsonObject.optString("status");
                }else {
                    JSONArray jsonArray = jsonObject.optJSONArray("routes");

                    ArrayList<String> lkm = new ArrayList<>();
                    ArrayList<Double> dis_val = new ArrayList<>();

                    ArrayList<String> time = new ArrayList<>();
                    ArrayList<Double> time_val = new ArrayList<>();

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject routeObj = jsonArray.optJSONObject(i);

                        JSONArray legsArray= routeObj.optJSONArray("legs");
                        JSONObject legsObj = legsArray.optJSONObject(0);

                        JSONObject distanceObj = legsObj.optJSONObject("distance");
                        JSONObject durationObj = legsObj.optJSONObject("duration");

                        lkm.add(distanceObj.optString("text"));
                        dis_val.add(distanceObj.optDouble("value"));

                        time.add(durationObj.optString("text"));
                        time_val.add(durationObj.optDouble("value"));

                        if (i==(jsonArray.length()-1)){

                            updateBottom();
                        }

                    }

                    int minIndex = dis_val.indexOf(Collections.min(dis_val));

                    distance = lkm.get(minIndex);
                    distancevalue = dis_val.get(minIndex);
                    dis_tt = distancevalue/1000;

                    duration = time.get(minIndex);
                    durationvalue = time_val.get(minIndex);
                    dur_tt = durationvalue/3600;

                    getSupportActionBar().setTitle(distance +" "+ getString(R.string.or) +" " + duration +" "+ getString(R.string.away));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_GetPriceing){
            Log.d("GetPriceing: ",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONObject dataObj = successObj.optJSONObject("data");
                if (dataObj.length()>0 && dataObj!=null){
                    price_tv.setText(dataObj.optString("price"));
                    loading_tv.setText(dataObj.optString("loading"));
                    unloading_tv.setText(dataObj.optString("unloading"));
                    loadKM_tv.setText(dataObj.optString("loadKM"));
                    unloadKM_tv.setText(dataObj.optString("unloadKM"));
                    driverSalary_tv.setText(dataObj.optString("driverSalary"));
                    driverDailyExp_tv.setText(dataObj.optString("driverDailyExp"));
                    helper_tv.setText(dataObj.optString("helper"));
                    royalty_tv.setText(dataObj.optString("royalty"));
                    tollTax_tv.setText(dataObj.optString("tollTax"));
                    driverSaving_tv.setText(dataObj.optString("driverSaving"));
                    nightStay_tv.setText(dataObj.optString("nightStay"));
                    minCharge_tv.setText(dataObj.optString("minCharge"));
                    totalPrice_tv.setText(dataObj.optString("totalPrice"));
                    tyres_tv.setText(dataObj.optString("tyres"));
                    days_tv.setText(dataObj.optString("days"));
                    totalKM_tv.setText(dataObj.optString("totalKM"));
                    gst_tv.setText(dataObj.optString("gstTaxValue"));
                    total_price_booknow.setText(dataObj.optString("totalPrice"));

                    JSONObject messageObj = successObj.getJSONObject("msg");
                    String replyCode = messageObj.optString("replyCode");
                    String replyMessage = messageObj.optString("replyMessage");
                    if (replyCode.equals(replyMessage)){
                        cash_onclick.setEnabled(true);
                    }
                }else {
                    Toast.makeText(Order_Confirm.this,R.string.price_not_get,Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void Selectvehicle(){
        HashMap objectNew = new HashMap();
        new WebTask(Order_Confirm.this, WebUrls.BASE_URL+WebUrls.GetPricing_Api+
                "bookingType="+"book_now"+
                "&materialId="+Home.materialId_string+
                "&quantity="+ Home.quantity_stinrg+"&unit="+Home.unit_stinrg+
                "&unitPrice="+Home.unitPrice_stinrg+"&vehicleTypeId="+Home.id_stinrg+
                "&totalTime="+dur_tt+"&totalKM="+dis_tt,objectNew,Order_Confirm.this, RequestCode.CODE_GetPriceing,0);
    }

    public void updateBottom() {
        Log.d(TAG, "updateBottomSheetContent: Called");
        View view = getLayoutInflater().inflate(R.layout.activity_price__chart, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        price_tv = (TextView)dialog.findViewById(R.id.price_chart);
        loading_tv = (TextView)dialog.findViewById(R.id.loading__chart);
        unloading_tv = (TextView)dialog.findViewById(R.id.unloading_chart);
        loadKM_tv = (TextView)dialog.findViewById(R.id.loadKM_chart);
        unloadKM_tv = (TextView)dialog.findViewById(R.id.unloadKM_chart);
        driverSalary_tv = (TextView)dialog.findViewById(R.id.driverSalary_chart);
        driverDailyExp_tv = (TextView)dialog.findViewById(R.id.driverDailyExp_chart);
        helper_tv = (TextView)dialog.findViewById(R.id.helper_chart);
        royalty_tv = (TextView)dialog.findViewById(R.id.royalty_chart);
        tollTax_tv = (TextView)dialog.findViewById(R.id.tollTax_chart);
        driverSaving_tv = (TextView)dialog.findViewById(R.id.driverSaving_chart);
        nightStay_tv = (TextView)dialog.findViewById(R.id.nightStay_chart);
        minCharge_tv = (TextView)dialog.findViewById(R.id.minCharge_chart);
        totalPrice_tv = (TextView)dialog.findViewById(R.id.totalPrice_chart);
        tyres_tv = (TextView)dialog.findViewById(R.id.tyres_chart);
        days_tv = (TextView)dialog.findViewById(R.id.days_chart);
        totalKM_tv = (TextView)dialog.findViewById(R.id.totalKM_chart);
        gst_tv = (TextView)dialog.findViewById(R.id.gst_tv);
        Button can = (Button)dialog.findViewById(R.id.cancel_chart);
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        if (dis_tt!=0&&dur_tt!=0){
            Selectvehicle();
//        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Order_Confirm.this)) {
            if (SharedPrefManager.getLangId(Order_Confirm.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Order_Confirm.this,RequestCode.LangId));
            } else {
                Toast.makeText(Order_Confirm.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Order_Confirm.this,RequestCode.LangId, langval);
    }
}
