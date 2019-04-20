package babbabazrii.com.bababazri.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener , WebCompleteTask{

    //widgets
    EditText mobileno_et;
    Button submit_forgot_btn;
    ProgressDialog progressDialog = null;
    ImageView logo_login;

    //variables
    String mobile_string;
    Toolbar toolbar_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_forgot_password);

        mobileno_et = (EditText)findViewById(R.id.et_mobileno_forgot);
        submit_forgot_btn = (Button)findViewById(R.id.btn_submit_forgot);
        logo_login = (ImageView)findViewById(R.id.logo_login);
        toolbar_forgot_password =(Toolbar)findViewById(R.id.toolbar_forgotpassword);
        setSupportActionBar(toolbar_forgot_password);
        setTitle(getString(R.string.forgot_password));

        progressDialog = new ProgressDialog(this);
        submit_forgot_btn.setOnClickListener(this);
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
//        finishAfterTransition();
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit_forgot:
                mobile_string = mobileno_et.getText().toString();
                forgotNumberServiceCall();
                break;

        }

    }

    private void forgotNumberServiceCall() {
        if (TextUtils.isEmpty(mobile_string)) {
            mobileno_et.setError(getString(R.string.notempty));
            mobileno_et.requestFocus();
        }else if (mobile_string.length()>10||mobile_string.length()<10) {
            mobileno_et.setError(getResources().getString(R.string.mobile_no_not_valid));
            mobileno_et.requestFocus();
        } else {
            HashMap objectNew = new HashMap();
            objectNew.put("realm", "user");
            objectNew.put("mobile", mobile_string);

            new WebTask(ForgotPassword.this, WebUrls.BASE_URL + WebUrls.resetPassRequest, objectNew, ForgotPassword.this, RequestCode.CODE_ResetPassRequest, 1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode)
    {
        android.util.Log.d("response",response);
        if (taskcode==RequestCode.CODE_ResetPassRequest)
        {
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();


                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.optJSONObject("success");
                JSONObject data = success.optJSONObject("data");
                String adminApproval = data.optString("adminApproval");
                String realm = data.optString("realm");
                String id = data.optString("id");
                String fullName = data.optString("fullName");
                String firstName = data.optString("firstName");
                String lastName = data.optString("lastName");
                String address = data.optJSONObject("address").optString("address");
                String address_lat = data.optJSONObject("address").optJSONObject("location").optString("lat");
                String address_lng = data.optJSONObject("address").optJSONObject("location").optString("lng");
                String mobile = data.optString("mobile");
                String createdAt = data.optString("createdAt");
                String updatedAt = data.optString("updatedAt");
                Boolean mobileVerified = data.optBoolean("mobileVerified");

                JSONObject signupOtp = data.optJSONObject("signupOtp");
                String createdAt_ex = signupOtp.optString("createdAt");
                String expireAt_ex = signupOtp.optString("expireAt");
                String otp_ex = signupOtp.optString("otp");
                Log.d("otp_exp_ForgetPassword",otp_ex);

                JSONObject passwordOtp = data.optJSONObject("passwordOtp");
                String createdAt_n = passwordOtp.optString("createdAt");
                String expireAt_n = passwordOtp.optString("expireAt");
                Integer otp_n = passwordOtp.getInt("otp");
                Log.d("otp_new_ForgetPassword", String.valueOf(otp_n));

                JSONObject msg = success.optJSONObject("msg");
                String replyCode = msg.optString("replyCode");
                String replyMessage = msg.optString("replyMessage");

                SharedPrefManager.getInstance(getApplicationContext()).storeRegPeopleOtp(otp_n);
                SharedPrefManager.getInstance(getApplicationContext()).storeSignPeopleId(id);
                if (realm.equals("user"))
                {
                    Intent intent = new Intent(ForgotPassword.this, VerifiyOtp.class);
                    Toast.makeText(ForgotPassword.this, getString(R.string.otp_sent), Toast.LENGTH_SHORT).show();
                    Pair[] pairs = new Pair[3];
                    pairs[0] = new Pair<View,String>(logo_login,"logoTransition");
                    pairs[1] = new Pair<View,String>(mobileno_et,"firsetTransition");
                    pairs[2] = new Pair<View,String>(submit_forgot_btn,"btnTransition");

//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgotPassword.this,pairs);

                    startActivity(intent);
                }else {
                    Toast.makeText(ForgotPassword.this,R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(ForgotPassword.this)) {
            if (SharedPrefManager.getLangId(ForgotPassword.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(ForgotPassword.this,RequestCode.LangId));
            } else {
                Toast.makeText(ForgotPassword.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(ForgotPassword.this,RequestCode.LangId, langval);
    }


}
