package babbabazrii.com.bababazri.Activities;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class Price_Chart extends AppCompatActivity {
    TextView id_tv,price_tv,loading_tv,unloading_tv,loadKM_tv,unloadKM_tv,driverSalary_tv,driverDailyExp_tv,helper_tv,royalty_tv,tollTax_tv,driverSaving_tv,nightStay_tv,minCharge_tv,totalPrice_tv,vehicleTypeId_tv,tyres_tv,days_tv,totalKM_tv;
    Button cancel_chart;
    static Price_Chart mInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_price__chart);
//        price_tv = (TextView)findViewById(R.id.price_chart);
//        loading_tv = (TextView)findViewById(R.id.loading__chart);
//        unloading_tv = (TextView)findViewById(R.id.unloading_chart);
//        loadKM_tv = (TextView)findViewById(R.id.loadKM_chart);
//        unloadKM_tv = (TextView)findViewById(R.id.unloadKM_chart);
//        driverSalary_tv = (TextView)findViewById(R.id.driverSalary_chart);
//        driverDailyExp_tv = (TextView)findViewById(R.id.driverDailyExp_chart);
//        helper_tv = (TextView)findViewById(R.id.helper_chart);
//        royalty_tv = (TextView)findViewById(R.id.royalty_chart);
//        tollTax_tv = (TextView)findViewById(R.id.tollTax_chart);
//        driverSaving_tv = (TextView)findViewById(R.id.driverSaving_chart);
//        nightStay_tv = (TextView)findViewById(R.id.nightStay_chart);
//        minCharge_tv = (TextView)findViewById(R.id.minCharge_chart);
//        totalPrice_tv = (TextView)findViewById(R.id.totalPrice_chart);
//        tyres_tv = (TextView)findViewById(R.id.tyres_chart);
//        days_tv = (TextView)findViewById(R.id.days_chart);
//        totalKM_tv = (TextView)findViewById(R.id.totalKM_chart);
//        cancel_chart = (Button)findViewById(R.id.cancel_chart);
//
//        mInstance = this;
//        Selectvehicle();
    }
//    public void Selectvehicle(){
//        HashMap objectNew = new HashMap();
//        new WebTask(Price_Chart.this, WebUrls.BASE_URL+WebUrls.GetPricing_Api+
//                "quantity="+ Home.quantity_stinrg+"&unit="+Home.unit_stinrg+
//                "&unitPrice="+Home.unitPrice_stinrg+"&vehicleTypeId="+Home.id_stinrg+
//                "&totalTime="+Order_Confirm.durationvalue+"&totalKM="+Order_Confirm.distancevalue,objectNew,Price_Chart.this, RequestCode.CODE_GetPriceing,0);
//    }

//    public static Price_Chart getInstance(){
//        return mInstance;
//    }
//    @Override
//    public void onComplete(String response, int taskcode) {
//        if (taskcode == RequestCode.CODE_GetPriceing){
//            Log.d("GetPriceing: ",response);
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                JSONObject successObj = jsonObject.optJSONObject("success");
//                JSONObject dataObj = successObj.optJSONObject("data");
//
//                price_tv.setText(dataObj.optString("price"));
//                loading_tv.setText(dataObj.optString("loading"));
//                unloading_tv.setText(dataObj.optString("unloading"));
//                loadKM_tv.setText(dataObj.optString("loadKM"));
//                unloadKM_tv.setText(dataObj.optString("unloadKM"));
//                driverSalary_tv.setText(dataObj.optString("driverSalary"));
//                driverDailyExp_tv.setText(dataObj.optString("driverDailyExp"));
//                helper_tv.setText(dataObj.optString("helper"));
//                royalty_tv.setText(dataObj.optString("royalty"));
//                tollTax_tv.setText(dataObj.optString("tollTax"));
//                driverSaving_tv.setText(dataObj.optString("driverSaving"));
//                nightStay_tv.setText(dataObj.optString("nightStay"));
//                minCharge_tv.setText(dataObj.optString("minCharge"));
//                totalPrice_tv.setText(dataObj.optString("totalPrice"));
//                vehicleTypeId_tv.setText(dataObj.optString("vehicleTypeId"));
//                tyres_tv.setText(dataObj.optString("tyres"));
//                days_tv.setText(dataObj.optString("days"));
//                totalKM_tv.setText(dataObj.optString("totalKM"));
//
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Price_Chart.this)) {
            if (SharedPrefManager.getLangId(Price_Chart.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Price_Chart.this,RequestCode.LangId));
            } else {
                Toast.makeText(Price_Chart.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Price_Chart.this,RequestCode.LangId, langval);
    }
}
