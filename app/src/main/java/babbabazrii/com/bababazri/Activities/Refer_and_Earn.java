package babbabazrii.com.bababazri.Activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class Refer_and_Earn extends AppCompatActivity implements View.OnClickListener {
    TextView tv_title,tv_share_link,tv_referal_code;
    Toolbar toolbar_refer;
    RequestCode.TransitionType type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and__earn);

        toolbar_refer = (Toolbar)findViewById(R.id.toolbar_refer);
        setSupportActionBar(toolbar_refer);

//        type = (RequestCode.TransitionType)getIntent().getSerializableExtra(RequestCode.KEY_ANIM_TYPE);
//        title = getIntent().getExtras().getString(RequestCode.KEY_TITLE);

//        initAnimation();
//        getWindow().setAllowEnterTransitionOverlap(false);

        getSupportActionBar().setTitle(R.string.refer);

        //toolbar back button color and icon change
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_black);
        upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        tv_title = (TextView)findViewById(R.id.refer_earn_title);
        tv_title.setText(Html.fromHtml("Invite"));
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tv_share_link = (TextView)findViewById(R.id.share_link_tv);
        tv_share_link.setOnClickListener(this);
        tv_referal_code = (TextView)findViewById(R.id.refer_code);

    }

    @Override
    public boolean onSupportNavigateUp() {
//        finishAfterTransition();
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_link_tv:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,tv_referal_code.getText());
                startActivity(Intent.createChooser(share,R.string.app_name+""));
                break;
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Refer_and_Earn.this)) {
            if (SharedPrefManager.getLangId(Refer_and_Earn.this,RequestCode.LangId).compareTo("") != 0) {
            setLangRecreate(SharedPrefManager.getLangId(Refer_and_Earn.this,RequestCode.LangId));
            } else {
                Toast.makeText(Refer_and_Earn.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Refer_and_Earn.this,RequestCode.LangId, langval);
    }
//    private void initAnimation(){
//
//        switch (type){
//            case SlideJava:{
//                Slide enterTransition = new Slide();
//                enterTransition.setDuration(300);
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
}
