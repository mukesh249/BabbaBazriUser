package babbabazrii.com.bababazri.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Adapters.Notification_Adapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.Notification_Model;

public class Notification extends AppCompatActivity implements WebCompleteTask{
    Toolbar toolbar_notification;
    TextView list_emp;
    RecyclerView notification_recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Notification_Model> notificationModelArrayList = new ArrayList<>();
    Notification_Adapter adapter;
    RequestCode.TransitionType type;
    String title;
    static Notification mInstance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification);
        toolbar_notification = (Toolbar)findViewById(R.id.toolbar_notifacation);
        setSupportActionBar(toolbar_notification);

        notification_recyclerView = (RecyclerView)findViewById(R.id.notification_recycleview);
        notification_recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        notification_recyclerView.setLayoutManager(linearLayoutManager);
        list_emp = (TextView)findViewById(R.id.list_empty_noti);

        mInstance=this;
        notificationModelArrayList.clear();
        NotificationMethod();
        NotificationReadMethod();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_refresh_notification);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                NotificationMethod();
                NotificationReadMethod();
            }
        });

        type = (RequestCode.TransitionType)getIntent().getSerializableExtra(RequestCode.KEY_ANIM_TYPE);
        title = getIntent().getExtras().getString(RequestCode.KEY_TITLE);
        getSupportActionBar().setTitle(R.string.notification);


//        initAnimation();
//        getWindow().setAllowEnterTransitionOverlap(false);
        //toolbar back button color and icon change
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_black);
        upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        adapter = new Notification_Adapter(notificationModelArrayList,getApplicationContext());
        notification_recyclerView.setAdapter(adapter);

    }
    @Override
    public boolean onSupportNavigateUp() {
//        finishAfterTransition();
        onBackPressed();
        return true;
    }
    public static Notification getInstance(){
        return mInstance;
    }

    public void PopMessage(String title,String message){
        final View customView =
                LayoutInflater.
                        from(getApplication()).
                        inflate(R.layout.layout_cookie, null);


//        final ProgressBar progressBar = customView.findViewById(R.id.cookiebar_progressbar);

        CookieBar.build(Notification.this)
                .setCustomView(customView)
                .setIcon(R.drawable.notification)
                .setTitle(title)
                .setMessage(message)
                .setDuration(10000)
                .setAction(R.string.track_your_order, new OnActionClickListener() {
                    @Override
                    public void onClick() {
                        startActivity(new Intent(Notification.this,MyOrederList.class));
                        finish();
                    }
                })
                .show();
    }
//    private void initAnimation(){
//
//        switch (type){
//            case SlideJava:{
//                Slide enterTransition = new Slide();
//                enterTransition.setDuration(100);
//                enterTransition.setSlideEdge(Gravity.LEFT);
//                getWindow().setEnterTransition(enterTransition);
//                break;
//            }
//            case SlideXML:{
//                Transition enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
//                getWindow().setEnterTransition(enterTransition);
//                break;
//            }
//
//        }
//    }

    private void NotificationMethod(){
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL+WebUrls.Notification_Api,objectNew,Notification.this, RequestCode.CODE_Notification_Api,0);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_Notification_Api){
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray dataArray = successObj.optJSONArray("data");

                if (dataArray != null && dataArray.length()>0){
                    notification_recyclerView.setVisibility(View.VISIBLE);
                    list_emp.setVisibility(View.GONE);

                    for (int i=0;i<dataArray.length();i++)
                    {
                        JSONObject object = dataArray.getJSONObject(i);
                        Notification_Model item = new Notification_Model();
                        item.setNotification_Title(object.optString("title"));
                        item.setNotification_message(object.optString("body"));
                        item.setNotificatin_createdAt(object.optString("createdAt"));

                        JSONObject msg = successObj.optJSONObject("msg");
                        String replyCode = msg.optString("replyCode");
                        String replyMessage = msg.optString("replyMessage");
//                    if (replyCode.equals(replyMessage)){
//
//                    }
                        notificationModelArrayList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    notification_recyclerView.setVisibility(View.GONE);
                    list_emp.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void NotificationReadMethod(){
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL+WebUrls.NotificationRead_Api,objectNew,Notification.this, RequestCode.CODE_ReadNotification,1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Notification.this)) {
            if (SharedPrefManager.getLangId(Notification.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Notification.this,RequestCode.LangId));
            } else {
                Toast.makeText(Notification.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Notification.this,RequestCode.LangId, langval);
    }
}
