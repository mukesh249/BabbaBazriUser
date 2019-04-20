package babbabazrii.com.bababazri.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Adapters.MyOrderListAdapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Common.Network;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.RequestListWapper;

public class MyOrederList extends AppCompatActivity implements WebCompleteTask{

    Toolbar toolbar_myorderlist;
    private RecyclerView recyclerView;
    private TextView list_empty;
    static ArrayList<RequestListWapper> requestListWapperArrayList = new ArrayList();
    MyOrderListAdapter mAdapter;
    GeometricProgressView progressBar;
    private boolean itShouldLoadMore = true;
    SwipeRefreshLayout swipeRefreshLayout;
    static MyOrederList mInstance;
    String req_id,image_string;
    public static boolean driver_a=false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_oreder_list);
        toolbar_myorderlist = (Toolbar)findViewById(R.id.toolbar_myorderlist);
        recyclerView = (RecyclerView)findViewById(R.id.my_order_list_recycleview);
        setSupportActionBar(toolbar_myorderlist);

        getSupportActionBar().setTitle(R.string.my_order_list);
        progressBar = (GeometricProgressView)findViewById(R.id.my_orderlist_loader);
        mInstance = this;

        list_empty = (TextView)findViewById(R.id.list_empty);

        recyclerView.setHasFixedSize(true);
        requestListWapperArrayList.clear();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.pull_refresh_myorder_list);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestListWapperArrayList.clear();
                swipeRefreshLayout.setRefreshing(false);
                if (!Network.isConnectingToInternet(MyOrederList.this)) {
                    SharedPrefManager.showMessage(MyOrederList.this, getString(R.string.network_error_msg));
                    return;
                } else {
                    fetchMyOrderList();
                }

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if(dy>0){
//                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)){
//                        if (itShouldLoadMore) {
//                            fetchMyOrderList();
//                        }
//                    }
//                }
//            }
//        });
        fetchMyOrderList();

        mAdapter = new MyOrderListAdapter(requestListWapperArrayList,getApplicationContext());
        recyclerView.setAdapter(mAdapter);

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

    private void fetchMyOrderList(){
//        progressBar.setVisibility(View.VISIBLE);
        Log.d("Auth : ", SharedPrefManager.getInstance(MyOrederList.this).getRegPeopleId());
        HashMap objectNew= new HashMap();
        new WebTask(MyOrederList.this, WebUrls.BASE_URL+WebUrls.orderlist_api,objectNew,MyOrederList.this, RequestCode.CODE_MyOrderlist_Api,0);
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (taskcode == RequestCode.CODE_MyOrderlist_Api) {
            try {
                requestListWapperArrayList.clear();
                Log.d("CODE_MyOrderlist_Api: ",response);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray dataArray = successObj.optJSONArray("data");
                if (dataArray !=null && dataArray.length()>0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    list_empty.setVisibility(View.GONE);

//                    requestListWapperArrayList.add(null);
//                    mAdapter.notifyItemInserted(requestListWapperArrayList.size() - 1);
//                    progressBar.setVisibility(View.GONE);
//                    requestListWapperArrayList.remove(requestListWapperArrayList.size() - 1);
//                    mAdapter.notifyItemRemoved(requestListWapperArrayList.size());
//                    int index = requestListWapperArrayList.size();
//                    int end = index + 6;
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject o = dataArray.optJSONObject(i);
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
//                        item.setP_lat(p_locationObj.optString("lat"));
//                        item.setP_lng(p_locationObj.optString("lng"));
                        item.setC_formatedAddress(o.optJSONObject("customerAddress").optString("formattedAddress"));
                        item.setLatlng(o.optJSONObject("customerAddress").optJSONObject("location")+"");
//                        item.setC_lat(c_locationObj.optString("lat"));
//                        item.setC_lng(c_locationObj.optString("lng"));
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
                    }
                    mAdapter.notifyDataSetChanged();
                }else {
                    recyclerView.setVisibility(View.GONE);
                    list_empty.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static MyOrederList getInstance(){
       return mInstance;
    }
    public void RaitingIntent(String id,String price,String src,String des){
        Intent intent = new Intent(MyOrederList.this,Rating.class);
        intent.putExtra(RequestCode.KEY_ANIM_TYPE,RequestCode.TransitionType.ExplodeJava);
        intent.putExtra(RequestCode.KEY_TITLE,"Review & Rating");
        intent.putExtra("PRO_ID",id);
        intent.putExtra("PRICE",price);
        intent.putExtra("SRC",src);
        intent.putExtra("DES",des);
        intent.putExtra("DRI_IMG",WebUrls.BASE_URL+image_string);
        ActivityOptions options= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(this);
        }
        startActivity(intent,options.toBundle());
        finish();
    }
    public void TrakMethod(RequestListWapper requestListWapper){
        Intent intent = new Intent(MyOrederList.this, Tracking.class);
        intent.putExtra("data", requestListWapper);
        startActivity(intent);
    }
//    public void GetPricekMethod(RequestListWapper requestListWapper){
//        Intent intent = new Intent(MyOrederList.this, Price_Chart.class);
//        intent.putExtra("data", requestListWapper);
//        startActivity(intent);
//        finish();
//    }
//        String quantity_stinrg,unit_stinrg,unitPrice_stinrg,vehicletyypeid_stinrg;
//    String dur_tt,dis_tt;
    TextView id_tv,price_tv,loading_tv,unloading_tv,loadKM_tv,unloadKM_tv,driverSalary_tv,driverDailyExp_tv,helper_tv,royalty_tv,tollTax_tv,driverSaving_tv,nightStay_tv,minCharge_tv,totalPrice_tv,vehicleTypeId_tv,tyres_tv,days_tv,totalKM_tv;
    public void updateBottom(RequestListWapper requestListWapper) {
        Log.d("MyO", "updateBottomSheetContent: Called");
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
        Button can = (Button)dialog.findViewById(R.id.cancel_chart);

        price_tv.setText(requestListWapper.getPrice_get());
        loading_tv.setText(requestListWapper.getLoading_get());
        unloading_tv.setText(requestListWapper.getUnloading_get());
        loadKM_tv.setText(requestListWapper.getLoadKM_get());
        unloadKM_tv.setText(requestListWapper.getUnloadKM_get());
        driverSalary_tv.setText(requestListWapper.getDriverSalary_get());
        driverDailyExp_tv.setText(requestListWapper.getDriverDailyExp_get());
        helper_tv.setText(requestListWapper.getHelper_get());
        royalty_tv.setText(requestListWapper.getRoyalty_get());
        tollTax_tv.setText(requestListWapper.getTollTax());
        driverSaving_tv.setText(requestListWapper.getDriverSaving_get());
        nightStay_tv.setText(requestListWapper.getNightStay_get());
        minCharge_tv.setText(requestListWapper.getMinCharge_get());
        totalPrice_tv.setText(requestListWapper.getTotalPrice_get());
        tyres_tv.setText(requestListWapper.getTyres_get());
        days_tv.setText(requestListWapper.getDays_get());
        totalKM_tv.setText(requestListWapper.getTotalKM_get());

        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(MyOrederList.this)) {
            if (SharedPrefManager.getLangId(MyOrederList.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(MyOrederList.this,RequestCode.LangId));
            } else {
                Toast.makeText(MyOrederList.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(MyOrederList.this,RequestCode.LangId, langval);
    }
}

