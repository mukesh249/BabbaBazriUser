package babbabazrii.com.bababazri.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class Select_Vehicle extends AppCompatActivity implements AdapterView.OnItemSelectedListener , WebCompleteTask{

    Spinner select_vehicle_spinner;
    String id_stinrg,quantity_stinrg,unit_stinrg,unitPrice_stinrg,totalTime_stinrg,totalKM_stinrg,materialId_string;
    TextView gst_tv,id_tv,price_tv,loading_tv,unloading_tv,loadKM_tv,unloadKM_tv,driverSalary_tv,driverDailyExp_tv,helper_tv,royalty_tv,tollTax_tv,driverSaving_tv,nightStay_tv,minCharge_tv,totalPrice_tv,vehicleTypeId_tv,tyres_tv,days_tv,totalKM_tv;
    ArrayAdapter aa,ab;
    Bundle bundle;
    Button order_rq;
    Toolbar toolbar_select_vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__vehicle);

        select_vehicle_spinner = (Spinner) findViewById(R.id.select_vehicle_spinner);
        id_tv = (TextView)findViewById(R.id.id_stirng);
        price_tv = (TextView)findViewById(R.id.price);
        loading_tv = (TextView)findViewById(R.id.loading);
        unloading_tv = (TextView)findViewById(R.id.unloading);
        loadKM_tv = (TextView)findViewById(R.id.loadKM);
        unloadKM_tv = (TextView)findViewById(R.id.unloadKM);
        driverSalary_tv = (TextView)findViewById(R.id.driverSalary);
        driverDailyExp_tv = (TextView)findViewById(R.id.driverDailyExp);
        helper_tv = (TextView)findViewById(R.id.helper);
        royalty_tv = (TextView)findViewById(R.id.royalty);
        tollTax_tv = (TextView)findViewById(R.id.tollTax);
        driverSaving_tv = (TextView)findViewById(R.id.driverSaving);
        nightStay_tv = (TextView)findViewById(R.id.nightStay);
        minCharge_tv = (TextView)findViewById(R.id.minCharge);
        totalPrice_tv = (TextView)findViewById(R.id.totalPrice);
        vehicleTypeId_tv = (TextView)findViewById(R.id.vehicleTypeId);
        tyres_tv = (TextView)findViewById(R.id.tyres);
        days_tv = (TextView)findViewById(R.id.days);
        totalKM_tv = (TextView)findViewById(R.id.totalKM);
        order_rq = (Button)findViewById(R.id.confirm_order_req);
        gst_tv = (TextView)findViewById(R.id.gst_tv);

        toolbar_select_vehicle = (Toolbar)findViewById(R.id.toolbar_select_vehicle);
        setSupportActionBar(toolbar_select_vehicle);
        setTitle(R.string.app_name);

        order_rq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAfterBtn();
            }
        });

        quantity_stinrg=After_Request_Btn.quantity_string;
        bundle = getIntent().getExtras();
//        quantity_stinrg = bundle.getString("quantity","");
        unit_stinrg = bundle.getString("unit","");
        unitPrice_stinrg = bundle.getString("unitPrice", "");
        materialId_string = bundle.getString("materialId","");
//        totalTime_stinrg = bundle.getString("totalTime","");
//        totalKM_stinrg = bundle.getString("totalKM","");



        aa = new ArrayAdapter(Select_Vehicle.this, android.R.layout.simple_spinner_item, After_Request_Btn.vehicle_nm);
        ab = new ArrayAdapter(Select_Vehicle.this, android.R.layout.simple_spinner_item, After_Request_Btn.vehicle_id);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        select_vehicle_spinner.setAdapter(aa);
        select_vehicle_spinner.setOnItemSelectedListener(this);
       // select_vehicle_spinner.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        id_stinrg = String.valueOf(ab.getItem(position));
        price_tv.setText("");
        loading_tv.setText("");
        unloading_tv.setText("");
        loadKM_tv.setText("");
        unloadKM_tv.setText("");
        driverSalary_tv.setText("");
        driverDailyExp_tv.setText("");
        helper_tv.setText("");
        royalty_tv.setText("");
        tollTax_tv.setText("");
        driverSaving_tv.setText("");
        nightStay_tv.setText("");
        minCharge_tv.setText("");
        totalPrice_tv.setText("");
        vehicleTypeId_tv.setText("");
        tyres_tv.setText("");
        days_tv.setText("");
        totalKM_tv.setText("");
        gst_tv.setText("");
        Selectvehicle();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void Selectvehicle(){
        HashMap objectNew = new HashMap();
        new WebTask(Select_Vehicle.this, WebUrls.BASE_URL+WebUrls.GetPricing_Api+
                "bookingType="+"book_later"+
                "&materialId="+materialId_string+
                "&quantity="+quantity_stinrg+"&unit="+unit_stinrg+
                "&unitPrice="+unitPrice_stinrg+"&vehicleTypeId="+id_stinrg+
                "&totalTime="+After_Request_Btn.dur_tot+"&totalKM="+After_Request_Btn.dis_tot,objectNew,Select_Vehicle.this, RequestCode.CODE_GetPriceing,0);
    }
    public void fetchAfterBtn() {
        try {

            JSONObject jsonObject = new JSONObject();
            JSONObject location = new JSONObject();
            location.put("lat", After_Request_Btn.lat);
            location.put("lng", After_Request_Btn.lng);
            jsonObject.put("formattedAddress", After_Request_Btn.address_string);
            jsonObject.put("location", location);

            HashMap objectNew = new HashMap();
            objectNew.put("vehicleTypeId",id_stinrg);
            objectNew.put("productId", SharedPrefManager.getInstance(this).getProductid());
            objectNew.put("quantity", quantity_stinrg);
            objectNew.put("unit", unit_stinrg);
            objectNew.put("deliveryDate", After_Request_Btn.date_string);
            objectNew.put("address", jsonObject.toString());
            objectNew.put("totalTime",After_Request_Btn.dur_tot+"");
            objectNew.put("totalKM",After_Request_Btn.dis_tot+"");


            new WebTask(Select_Vehicle.this, WebUrls.BASE_URL + WebUrls.afterBtnplaceOrder_api, objectNew, Select_Vehicle.this, RequestCode.CODE_AfterBtn, 1);
        } catch (Exception e) {

        }
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_GetPriceing){
            Log.d("GetPriceing: ",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONObject dataObj = successObj.optJSONObject("data");

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
                vehicleTypeId_tv.setText(dataObj.optString("vehicleTypeId"));
                tyres_tv.setText(dataObj.optString("tyres"));
                days_tv.setText(dataObj.optString("days"));
                gst_tv.setText(dataObj.optString("gstTaxValue"));
                totalKM_tv.setText(dataObj.optString("totalKM"));

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_AfterBtn) {
            android.util.Log.d("response AfterBtn : ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONObject jsondata = successObj.optJSONObject("data");
                JSONObject orderInsta = jsondata.optJSONObject("orderInst");

                String orderQuantity = orderInsta.optString("orderQuantity");
                String unit = orderInsta.optString("unit");
                String id = orderInsta.optString("id");
                String productId = orderInsta.optString("productId");
                String orderDate = orderInsta.optString("orderDate");
                String deliveryDate = orderInsta.optString("deliveryDate");
//                String ownerId = orderInsta.getString("ownerId");
                String price = orderInsta.optString("price");
                String bookingStatus =orderInsta.optString("bookingStatus");
                String address = orderInsta.optString("address");
                String deliveryDistance = orderInsta.optString("deliveryDistance");


                JSONObject msgObj = successObj.optJSONObject("msg");
                String replyCode = msgObj.optString("replyCode");
                String replyMessage = msgObj.optString("replyMessage");
                if (replyCode.equals(replyMessage)) {
                    Toast.makeText(Select_Vehicle.this, R.string.order_request_success, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Select_Vehicle.this,User_Profile.class));
                    finish();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Select_Vehicle.this)) {
            if (SharedPrefManager.getLangId(Select_Vehicle.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Select_Vehicle.this,RequestCode.LangId));
            } else {
                Toast.makeText(Select_Vehicle.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Select_Vehicle.this,RequestCode.LangId, langval);
    }
}
