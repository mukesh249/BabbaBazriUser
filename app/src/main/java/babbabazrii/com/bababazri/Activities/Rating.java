package babbabazrii.com.bababazri.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class Rating extends AppCompatActivity implements WebCompleteTask{
    TextView support_btn,src,des,price;
    Button submit_btn;
    RatingBar rating;
    RequestCode.TransitionType type;
    String toolbarTile,orderRequestId;
    ImageView bckBtn;
    CircleImageView DriverImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        submit_btn = (Button)findViewById(R.id.rating_submit_btn);
        support_btn = (TextView) findViewById(R.id.support_rating);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        bckBtn = (ImageView)findViewById(R.id.exitbtn);
        src = (TextView)findViewById(R.id.src_address_rating);
        des = (TextView)findViewById(R.id.des_tv);
        price = (TextView)findViewById(R.id.price_tv);
        DriverImg = (CircleImageView)findViewById(R.id.pic);

        Bundle b = getIntent().getExtras();
        price.setText(" "+b.getString("PRICE",""));
        src.setText(b.getString("SRC",""));
        des.setText(b.getString("DES",""));
        orderRequestId = b.getString("PRO_ID","");
        Glide.with(this)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo))
                .load(b.getString("DRI_IMG",""))
                .into(DriverImg);


        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rating.this,User_Profile.class));
                finish();
            }
        });

//        type = (RequestCode.TransitionType)getIntent().getSerializableExtra(RequestCode.KEY_ANIM_TYPE);
//        toolbarTile = getIntent().getExtras().getString(RequestCode.KEY_TITLE);
//
////        initAnimation();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setAllowEnterTransitionOverlap(false);
//        }

        support_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rating.this,Support.class));
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                volleyFloatRequest();
                HashMap objectNew = new HashMap();
                objectNew.put("orderRequestId",orderRequestId);
                objectNew.put("rating",rating.getRating()+"");

                new WebTask(Rating.this, WebUrls.BASE_URL+WebUrls.Rating_Api,objectNew,Rating.this,RequestCode.CODE_Rating,1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Rating.this,User_Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void initAnimation(){

        switch (type){
            case ExplodeJava:{
                Explode enterTransition = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    enterTransition = new Explode();
                    enterTransition.setDuration(300);
                    getWindow().setEnterTransition(enterTransition);
                }

                break;
            }
            case ExplodeXML:{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Transition enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
                    getWindow().setEnterTransition(enterTransition);
                }
                break;
            }

        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        Log.d("response",response);
        if (taskcode == RequestCode.CODE_Rating){
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.getJSONObject("success");
                JSONObject dataObj = successObj.optJSONObject("data");
                String rating = dataObj.optString("rating");

                JSONObject msgObj = successObj.optJSONObject("msg");
                String replyCode = msgObj.optString("replyCode");
                String replyMessage = msgObj.optString("replyMessage");
                startActivity(new Intent(Rating.this,User_Profile.class));
                finish();
            }catch (Exception e){


            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Rating.this)) {
            if (SharedPrefManager.getLangId(Rating.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Rating.this,RequestCode.LangId));
            } else {
                Toast.makeText(Rating.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Rating.this,RequestCode.LangId, langval);
    }
}
