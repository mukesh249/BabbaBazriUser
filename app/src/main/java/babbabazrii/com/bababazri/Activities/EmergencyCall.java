package babbabazrii.com.bababazri.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;


public class EmergencyCall extends AppCompatActivity {

    Context mContext;
    Toolbar toolbar_emergency;
    RequestCode.TransitionType type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        Window window = getWindow();
//
//// clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//// finally change the color
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_emergency_call);
        final LinearLayout call = (LinearLayout) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "+91 8377830830"));
                startActivity(intent);
            }
        });
        toolbar_emergency = (Toolbar)findViewById(R.id.toolbar_emergency);
        setSupportActionBar(toolbar_emergency);
//        type = (RequestCode.TransitionType)getIntent().getSerializableExtra(RequestCode.KEY_ANIM_TYPE);
//        title = getIntent().getExtras().getString(RequestCode.KEY_TITLE);

//        initAnimation();
//        getWindow().setAllowEnterTransitionOverlap(false);

        getSupportActionBar().setTitle("Emergency Call");
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
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(EmergencyCall.this)) {
            if (SharedPrefManager.getLangId(EmergencyCall.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(EmergencyCall.this,RequestCode.LangId));
            } else {
                Toast.makeText(EmergencyCall.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(EmergencyCall.this,RequestCode.LangId, langval);
    }

//    private void initAnimation(){
//        switch (type){
//            case FadeJava:{
//                Fade enterTransition = new Fade();
//                enterTransition.setDuration(300);
//                getWindow().setEnterTransition(enterTransition);
//                break;
//            }
//            case FadeXML:{
//                Transition enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
//                getWindow().setEnterTransition(enterTransition);
//                break;
//            }
//        }
//    }

}
