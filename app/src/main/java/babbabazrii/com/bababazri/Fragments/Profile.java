package babbabazrii.com.bababazri.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Activities.Change_Email;
import babbabazrii.com.bababazri.Activities.Change_Password;
import babbabazrii.com.bababazri.Activities.User_Profile;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends AppCompatActivity implements WebCompleteTask {

    ImageView edit_1,edit_2,edit_3,edit_4,not_verifiy;
    public  static EditText editText_first_name,editText_last_name,editText_mobile,editText_email,editText_password;
    Button logout_btn;
    public static String email_verifiy_or_not;
    SwipeRefreshLayout swipeRefreshLayout;

    private RadioGroup radioLangGroup;
    private RadioButton radioLangButton;
    String ln_string;
    String lang_string;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        User_Profile.getInstance().ToolbarHide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.profile_setting);
//        setHasOptionsMenu(true);
        setTitle(R.string.profile_setting);

        if (SharedPrefManager.getLangId(Profile.this,RequestCode.LangId).compareTo("") != 0){
            lang_string = SharedPrefManager.getLangId(Profile.this,RequestCode.LangId);
        }else if (Locale.getDefault().getLanguage().compareTo("hi")==0){
            lang_string = "hi";
        }else {
            lang_string = "en";
        }
        RadioButton rbu1 =(RadioButton)findViewById(R.id.hi);
        RadioButton rbu2 =(RadioButton)findViewById(R.id.en);

        if(lang_string.equalsIgnoreCase("hi"))
        {
            rbu1.setChecked(true);
        }
        else if(lang_string.equalsIgnoreCase("en")){
            rbu2.setChecked(true);
        }

        radioLangGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioLangGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                if (null != rb) {
//                    Toast.makeText(Profile.this, rb.getText()+"", Toast.LENGTH_SHORT).show();
                    if (rb.getText().toString().compareTo("Hindi")==0){
                        ln_string = "hi";
                        ChangeLanguageMethod();
//                        setLangRecreate(ln_string);
                    }else {
                        ln_string = "en";
                        ChangeLanguageMethod();
//                        setLangRecreate(ln_string);
                    }
                }
            }
        });

//        getActivity().getWindow().setAllowEnterTransitionOverlap(false);
        edit_1 = (ImageView)findViewById(R.id.edit_icon1);
        edit_2 = (ImageView)findViewById(R.id.edit_icon2);
        edit_3 = (ImageView)findViewById(R.id.edit_icon3);
        edit_4 = (ImageView)findViewById(R.id.edit_icon4);
        not_verifiy = (ImageView)findViewById(R.id.not_verfiy);
        logout_btn =(Button)findViewById(R.id.logout_profile);

        editText_mobile = (EditText)findViewById(R.id.profile_mobile_et);
        editText_first_name = (EditText)findViewById(R.id.profile_first_name_et);
        editText_last_name = (EditText)findViewById(R.id.profile_last_name_et);
        editText_email = (EditText)findViewById(R.id.profile_email_et);
        editText_password = (EditText)findViewById(R.id.profile_password_et);


        if (TextUtils.isEmpty(editText_email.getText().toString())){
            edit_3.setVisibility(View.VISIBLE);
            not_verifiy.setVisibility(View.GONE);
        }else{
            edit_3.setVisibility(View.GONE);
            not_verifiy.setVisibility(View.VISIBLE);
        }
//        getActivity().getWindow().setAllowEnterTransitionOverlap(false);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_profile);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                GetUserProfile();
            }
        });
        editText_password.setText("**********");
        editText_mobile.setTag(editText_mobile.getKeyListener());
        editText_mobile.setKeyListener(null);

        editText_last_name.setTag(editText_last_name.getKeyListener());
        editText_last_name.setKeyListener(null);

        editText_first_name.setTag(editText_first_name.getKeyListener());
        editText_first_name.setKeyListener(null);

        editText_email.setTag(editText_email.getKeyListener());
        editText_email.setKeyListener(null);

        editText_password.setTag(editText_password.getKeyListener());
        editText_password.setKeyListener(null);

        edit_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_first_name.requestFocus();
                editText_first_name.setKeyListener((KeyListener) editText_first_name.getTag());
                editText_last_name.setKeyListener((KeyListener)editText_last_name.getTag());
                editText_first_name.requestFocus();
                editText_first_name.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText_first_name, InputMethodManager.SHOW_IMPLICIT);

            }
        });
        edit_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Change_Email.class));
            }
        });
        not_verifiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Change_Email.class));
            }
        });
        edit_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Change_Password.class));
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Profile.getInstance().logoutDialog();
            }
        });
        GetUserProfile();
        if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
////        getActivity().getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        User_Profile.getInstance().ToolbarHide();
//        setHasOptionsMenu(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.profile_setting);
//
//        radioLangGroup = (RadioGroup) view.findViewById(R.id.radio_group);
//        radioLangGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton rb = (RadioButton) group.findViewById(checkedId);
//
//                if (null != rb) {
//                    Toast.makeText(getActivity(), rb.getText()+"", Toast.LENGTH_SHORT).show();
//                    if (rb.getText().toString().compareTo("Hindi")==0){
//                        setLangRecreate("hi");
//                    }else {
//                        setLangRecreate("en");
//                    }
//                }
//            }
//        });
//
////        getActivity().getWindow().setAllowEnterTransitionOverlap(false);
//        edit_1 = (ImageView)view.findViewById(R.id.edit_icon1);
//        edit_2 = (ImageView)view.findViewById(R.id.edit_icon2);
//        edit_3 = (ImageView)view.findViewById(R.id.edit_icon3);
//        edit_4 = (ImageView)view.findViewById(R.id.edit_icon4);
//        not_verifiy = (ImageView)view.findViewById(R.id.not_verfiy);
//        logout_btn =(Button)view.findViewById(R.id.logout_profile);
//
//        editText_mobile = (EditText)view.findViewById(R.id.profile_mobile_et);
//        editText_first_name = (EditText)view.findViewById(R.id.profile_first_name_et);
//        editText_last_name = (EditText) view.findViewById(R.id.profile_last_name_et);
//        editText_email = (EditText)view.findViewById(R.id.profile_email_et);
//        editText_password = (EditText)view.findViewById(R.id.profile_password_et);
//
//
//        if (TextUtils.isEmpty(editText_email.getText().toString())){
//            edit_3.setVisibility(View.VISIBLE);
//            not_verifiy.setVisibility(View.GONE);
//        }else{
//            edit_3.setVisibility(View.GONE);
//            not_verifiy.setVisibility(View.VISIBLE);
//        }
////        getActivity().getWindow().setAllowEnterTransitionOverlap(false);
//        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_profile);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//                GetUserProfile();
//            }
//        });
//        editText_password.setText("**********");
//        editText_mobile.setTag(editText_mobile.getKeyListener());
//        editText_mobile.setKeyListener(null);
//
//        editText_last_name.setTag(editText_last_name.getKeyListener());
//        editText_last_name.setKeyListener(null);
//
//        editText_first_name.setTag(editText_first_name.getKeyListener());
//        editText_first_name.setKeyListener(null);
//
//        editText_email.setTag(editText_email.getKeyListener());
//        editText_email.setKeyListener(null);
//
//        editText_password.setTag(editText_password.getKeyListener());
//        editText_password.setKeyListener(null);
//
//        edit_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editText_first_name.requestFocus();
//                editText_first_name.setKeyListener((KeyListener) editText_first_name.getTag());
//                editText_last_name.setKeyListener((KeyListener)editText_last_name.getTag());
//                editText_first_name.requestFocus();
//                editText_first_name.setFocusableInTouchMode(true);
//
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(editText_first_name, InputMethodManager.SHOW_IMPLICIT);
//
//            }
//        });
//        edit_3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Change_Email.class));
//            }
//        });
//        not_verifiy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Change_Email.class));
//            }
//        });
//        edit_4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Change_Password.class));
//            }
//        });
//        logout_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User_Profile.getInstance().logoutDialog();
//            }
//        });
//        GetUserProfile();
//        return view;
//    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return true;

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.profile,menu);
//        super.onCreateOptionsMenu(menu);
//        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.profile,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_profile){
            UpdateUserName();
        }
        return super.onOptionsItemSelected(item);
    }
    public void GetUserProfile(){
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL+WebUrls.UserProfile_Api,objectNew,Profile.this, RequestCode.CODE_UserProfile,3);
    }
    public void UpdateUserName(){
        HashMap objectNew = new HashMap();
        if (TextUtils.isEmpty(editText_first_name.getText())){
            editText_first_name.setError(getString(R.string.notempty));
            editText_first_name.requestFocus();
        }else if (TextUtils.isEmpty(editText_last_name.getText())){
            editText_last_name.setError(getString(R.string.notempty));
            editText_last_name.requestFocus();
        }
        else {
            objectNew.put("firstName",editText_first_name.getText()+"");
            objectNew.put("lastName",editText_last_name.getText()+"");
            new WebTask(Profile.this, WebUrls.BASE_URL+WebUrls.Update_UserName_Api,objectNew,Profile.this, RequestCode.CODE_UpdateUserName,1);
        }

    }
    public void ChangeLanguageMethod(){
        HashMap objectNew = new HashMap();
        objectNew.put("ln",ln_string);
        new WebTask(this, WebUrls.BASE_URL+WebUrls.ChangeLanguage_Api,objectNew,Profile.this, RequestCode.CODE_ChangeLanguage,1);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_UserProfile){
            Log.d("UserProfile Response: ",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject succesObject = jsonObject.optJSONObject("success");
                JSONObject dataObject = succesObject.optJSONObject("data");

                editText_mobile.setText(dataObject.optString("mobile"));
                editText_first_name.setText(dataObject.optString("firstName"));
                editText_last_name.setText(dataObject.optString("lastName"));
                if (dataObject.getString("email")!=null){
                    editText_email.setText(dataObject.optString("email"));
                    if (dataObject.optBoolean("emailVerified")){
                        edit_3.setVisibility(View.VISIBLE);
                        email_verifiy_or_not = "Email ID verified";
                        not_verifiy.setVisibility(View.GONE);
                    }else {
                        email_verifiy_or_not = "Email ID not verified";
                        edit_3.setVisibility(View.GONE);
                        not_verifiy.setVisibility(View.VISIBLE);
                    }
                }
                editText_password.setText("**********");
                JSONObject msgObject = succesObject.optJSONObject("msg");
                if (msgObject.optString("replyCode").equals(msgObject.optString("replyMessage"))){
                    SharedPrefManager.getInstance(this).storeUserName(editText_first_name.getText()+" "+editText_last_name.getText());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_UpdateUserName){
            Log.d("UpdateUserName Response",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject succesObject = jsonObject.optJSONObject("success");
                JSONObject dataObject = succesObject.optJSONObject("data");

                editText_mobile.setText("+91 "+dataObject.optString("mobile"));
                editText_first_name.setText(dataObject.optString("firstName"));
                editText_last_name.setText(dataObject.optString("lastName"));

                if (dataObject.optString("email")!=null){
                    editText_email.setText(dataObject.optString("email"));
                    if (dataObject.optBoolean("emailVerified")){
                        edit_3.setVisibility(View.VISIBLE);
                        email_verifiy_or_not = "Email ID verified";
                        not_verifiy.setVisibility(View.GONE);
                    }else {
                        edit_3.setVisibility(View.GONE);
                        email_verifiy_or_not = "Email ID not verified";
                        not_verifiy.setVisibility(View.VISIBLE);
                    }
                }
                editText_password.setText("**********");

                JSONObject msgObject = succesObject.optJSONObject("msg");
                if (msgObject.optString("replyCode").equals(msgObject.optString("replyMessage"))){
                    User_Profile.UserName.setText(dataObject.optString("fullName"));
                    Toast.makeText(this, R.string.profile_update_successfully,Toast.LENGTH_LONG).show();
                    SharedPrefManager.getInstance(this).storeUserName(editText_first_name.getText()+" "+editText_last_name.getText());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(taskcode == RequestCode.CODE_ChangeLanguage){
            Log.d("Language_Response",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject succesObject = jsonObject.optJSONObject("success");
                JSONObject dataObject = succesObject.optJSONObject("data");
                if (dataObject.getString("ln")!=null){
                    setLangRecreate(dataObject.getString("ln"));
                }else {
                    Toast.makeText(this, R.string.something_wrong,Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
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
        SharedPrefManager.setLangId(Profile.this,RequestCode.LangId, langval);
        SharedPrefManager.setIslagChange(Profile.this, true);
        startActivity(new Intent(Profile.this, Profile.class));
        finish();
    }
}
