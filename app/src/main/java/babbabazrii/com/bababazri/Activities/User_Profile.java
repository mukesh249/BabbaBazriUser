package babbabazrii.com.bababazri.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Adapters.Material_List_Adapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Common.SessionManagement;
import babbabazrii.com.bababazri.Fragments.After_Search_Item;
import babbabazrii.com.bababazri.Fragments.Home;
import babbabazrii.com.bababazri.Fragments.Profile;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,WebCompleteTask{

    private static final String TAG = "MainActivity";
    Animation bounce_in, bounce_out;
    Fragment fragment = null;
    static User_Profile mInstance;
    private ArrayList<String> listdata = new ArrayList<String>();
    Double lat, lng;
    public static ArrayList<String> matrialName_ar = new ArrayList<String>();
    public static ArrayList<String> matrialId_ar = new ArrayList<String>();
    public static ArrayList<String> vehicleName_ar = new ArrayList<String>();
    public static ArrayList<String> vehicleId_ar = new ArrayList<String>();
    public static String meterial_name,meterial_id;
    SessionManagement session;
    public static RecyclerView materiallist;
    public static Material_List_Adapter adapter;
    public static TextView UserName;
    CircleImageView UserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager.getInstance(this).hideSoftKeyBoard(this);
        setContentView(R.layout.activity_user__profile);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mInstance = this;
//        setupwindowAnimation();

        matrialName_ar = Home.matrialName;
        matrialId_ar = Home.matrialId;
        vehicleName_ar = Home.vehicleName;
        vehicleId_ar = Home.vehiclelId;
        session = new SessionManagement(getApplicationContext());
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        }

        //Animation
        bounce_in = AnimationUtils.loadAnimation(User_Profile.this, R.anim.bounce);
        bounce_out = AnimationUtils.loadAnimation(User_Profile.this, R.anim.bounce_out);


        materiallist = (RecyclerView)findViewById(R.id.material_list_recycleview);
        materiallist.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        materiallist.setLayoutManager(linearLayoutManager);
//        if (SharedPrefManager.getIslagChange(User_Profile.this)) {
//            if (SharedPrefManager.getLangId(User_Profile.this,RequestCode.LangId).compareTo("") != 0) {
//                setLangRecreate(SharedPrefManager.getLangId(User_Profile.this,RequestCode.LangId));
//            } else {
//                setLangRecreate("en");
//            }
//        } else {
            Fragment home = new Home();
            FragmentManager FM = getSupportFragmentManager();
            FM.beginTransaction().replace(R.id.content_frame, home).commit();
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        UserName = (TextView)headerview.findViewById(R.id.name_name_hd);
        UserProfile = (CircleImageView)findViewById(R.id.profile_hd);
        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
//                if (!(findFragmentById(R.id.content_frame) instanceof Profile)){
                    startActivity(new Intent(User_Profile.this,Profile.class));
//                    Fragment profile = new Profile();
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.content_frame,profile).addToBackStack(null).commit();
//                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }
//    private void setupwindowAnimation(){
//        Slide slide = new Slide();
//        slide.setSlideEdge(Gravity.LEFT);
//        slide.setDuration(300);
//        getWindow().setReenterTransition(slide);
//        getWindow().setExitTransition(slide);
//        getWindow().setAllowReturnTransitionOverlap(false);
//    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content_frame, new Book_Later())
//                .commit();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (f instanceof Home){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if(doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.back_again, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
//        else if (f instanceof Profile){
//            recreate();
//        }
        else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(User_Profile.this)) {
            if (SharedPrefManager.getLangId(User_Profile.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(User_Profile.this,RequestCode.LangId));
            } else {
                Toast.makeText(User_Profile.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//            if (SharedPrefManager.getLangId(User_Profile.this,RequestCode.LangId).compareTo("") != 0) {
//                if (Locale.getDefault().getLanguage().compareTo(SharedPrefManager.getLangId(User_Profile.this,RequestCode.LangId))==0){
//                    setLangRecreate(SharedPrefManager.getLangId(User_Profile.this,RequestCode.LangId));
//                }else{
//                    Intent intent = new Intent(User_Profile.this,Splash.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//
//            } else {
//                Toast.makeText(User_Profile.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
//            }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        UserName.setText(SharedPrefManager.getInstance(this).getUserName());
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.book_ride ) {
            fragment = new Home();
                Home.material_list_models.clear();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.refer_earn) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            Intent intent = new Intent(User_Profile.this,Refer_and_Earn.class);
            startActivity(intent);
//            intent.putExtra(RequestCode.KEY_ANIM_TYPE,RequestCode.TransitionType.SlideJava);
//            intent.putExtra(RequestCode.KEY_TITLE,"Refer and Earn");
//            startActivity(intent,options.toBundle());
        } else if (id == R.id.emergencycont) {
//            EmergencyDialogBox();
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            Intent intent = new Intent(User_Profile.this,EmergencyCall.class);
            startActivity(intent);
//            intent.putExtra(RequestCode.KEY_ANIM_TYPE,RequestCode.TransitionType.FadeJava);
//            intent.putExtra(RequestCode.KEY_TITLE,"Emergency Call");
//            startActivity(intent,options.toBundle());
        } else if (id == R.id.support) {
            Intent intent = new Intent(User_Profile.this,Support.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(User_Profile.this,About.class);
            startActivity(intent);
        } else if (id == R.id.logout){
            logoutDialog();
        }else if (id == R.id.my_order_list){
            Intent intent = new Intent(User_Profile.this,MyOrederList.class);
            startActivity(intent);
        }
//        else if (id == R.id.language){
//            Language_PopUp();
//        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static Context mContext;

    public static User_Profile getInstance(){
        return mInstance;
    }
    public void ReloadMethod(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.content_frame, new After_Search_Item())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

//    public void ReloadProfile(){
//        startActivity(new Intent(User_Profile.this,Profile.class));
//        finish();
////        Fragment profile = new Profile();
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        fragmentManager.beginTransaction().replace(R.id.content_frame,profile).addToBackStack(null).commitAllowingStateLoss();
//    }

    public void ReloadHome(){
        startActivity(new Intent(User_Profile.this,User_Profile.class));
        finish();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
////                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
//                .replace(R.id.content_frame, new Home())
//                .addToBackStack(null)
//                .commitAllowingStateLoss();

    }
    public void ToolbarHide(){
        materiallist.setVisibility(View.GONE);
    }
    public void ToolbarShow(){
        materiallist.setVisibility(View.VISIBLE);
    }
    public void logoutDialog(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.you_want_logout);
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogoutMethod();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void LogoutMethod(){
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL + WebUrls.logout_Api,objectNew,User_Profile.this, RequestCode.CODE_Logout_Api,2);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_Logout_Api){
            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONObject dataObj = successObj.optJSONObject("data");
                String count = dataObj.optString("count");


                JSONObject msg = successObj.optJSONObject("msg");
                String replyCode = msg.optString("replyCode");
                String replyMessage = msg.optString("replyMessage");
                if (replyCode.equals(replyMessage)){
                    Intent intent = new Intent(User_Profile.this,Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    session.logoutUser();
                    startActivity(intent);
                    finish();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void RefreshHome(){
        fragment = new Home();
        Home.material_list_models.clear();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

//    public void Language_PopUp(){
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(User_Profile.this);
//// Get the layout inflater
//            LayoutInflater inflater = getLayoutInflater();
//            AlertDialog pass_ad;
//// Inflate and set the layout for the dialog
//// Pass null as the parent view because its going in the dialog
//// layout
//            View view =inflater.inflate(R.layout.language_popup, null);
//            RadioGroup radioLangGroup = (RadioGroup) view.findViewById(R.id.radio_group);
//            radioLangGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
//
//                    if (null != rb) {
////                        Toast.makeText(getActivity(), rb.getText()+"", Toast.LENGTH_SHORT).show();
//                        if (rb.getText().toString().compareTo("Hindi")==0){
//                            setLangRecreate("hi");
//                        }else {
//                            setLangRecreate("en");
//                        }
//                    }
//                }
//            });
//            builder.setView(view);
//            pass_ad = builder.create();
//            pass_ad.setCanceledOnTouchOutside(true);
//            pass_ad.show();
//    }

    public void setLangRecreate(String langval) {
        Locale locale;
        Configuration config = getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPrefManager.setLangId(User_Profile.this,RequestCode.LangId, langval);
        if (SharedPrefManager.getIslagChange(User_Profile.this)) {
            SharedPrefManager.setIslagChange(User_Profile.this, false);
            startActivity(new Intent(User_Profile.this, User_Profile.class));
            finish();
        }
    }
}
