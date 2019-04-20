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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class NewPassword extends AppCompatActivity implements View.OnClickListener ,WebCompleteTask{

    //Widgets
    EditText new_pass_et,new_confim_pass;
//    PinView otp_pin;
    Button new_submit_btn;
    ProgressDialog progressDialog = null;


    //variables
    String new_pass_string,otp_pin_string;
    Integer otp_pin;
    String new_confirm_pass_string;
    String id_stirng;
    Toolbar toolbar_new_Password;

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
        setContentView(R.layout.activity_new_password);

        new_pass_et = (EditText)findViewById(R.id.new_password);
        new_confim_pass = (EditText)findViewById(R.id.new_confirm_password);
//        otp_pin = (PinView)findViewById(R.id.new_otp);
        new_submit_btn = (Button)findViewById(R.id.new_submit_btn);
        toolbar_new_Password =(Toolbar)findViewById(R.id.toolbar_newpassword);
        setSupportActionBar(toolbar_new_Password);
        setTitle(getResources().getString(R.string.new_password));

        progressDialog = new ProgressDialog(this);
        new_submit_btn.setOnClickListener(this);

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
        finishAfterTransition();
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_submit_btn: {
                new_pass_string = new_pass_et.getText().toString().trim();
                new_confirm_pass_string = new_confim_pass.toString().trim();
                otp_pin = SharedPrefManager.getInstance(this).getRegPeopleOtp();
                otp_pin_string=String.valueOf(otp_pin);
                id_stirng = SharedPrefManager.getInstance(this).getSignPeopleId();
                newPasswordServiceCall();
                break;
            }
        }
    }

    private void newPasswordServiceCall() {


        if (TextUtils.isEmpty(new_pass_string)) {
            new_pass_et.setError(getString(R.string.notempty));
            new_pass_et.requestFocus();
        } else if (new_pass_et.getText().toString().compareTo(new_confim_pass.getText().toString())!=0) {
            new_confim_pass.setError(getString(R.string.passs_and_confirm_pass));
            new_confim_pass.requestFocus();
        } else if (new_pass_string.length() < 6) {
            new_pass_et.setError(getString(R.string.password_validation));
            new_pass_et.requestFocus();
        } else {
            HashMap objectNew = new HashMap();
            objectNew.put("id",id_stirng);
            objectNew.put("otp",otp_pin_string);
            objectNew.put("newPassword", new_pass_string);

            new WebTask(NewPassword.this, WebUrls.BASE_URL + WebUrls.new_resetPassword, objectNew, NewPassword.this, RequestCode.CODE_new_resetPassword, 1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode)
    {
        android.util.Log.d("response",response);
        if (taskcode==RequestCode.CODE_new_resetPassword)
        {
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();

                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.getJSONObject("success");
                JSONObject data = success.getJSONObject("data");
                String adminApproval = data.getString("adminApproval");
                String realm = data.getString("realm");
                String id = data.getString("id");
                String fullName = data.getString("fullName");
                String firstName = data.getString("firstName");
                String lastName = data.getString("lastName");
                String address = data.getString("address");
                String mobile = data.getString("mobile");
                String createdAt = data.getString("createdAt");
                String updatedAt = data.getString("updatedAt");
                Boolean mobileVerified = data.getBoolean("mobileVerified");

                JSONObject signupOtp = data.getJSONObject("signupOtp");
                String createdAt_ex = signupOtp.getString("createdAt");
                String expireAt_ex = signupOtp.getString("expireAt");
                String otp_ex = signupOtp.getString("otp");
                Log.d("otp_exp",otp_ex);

                JSONObject passwordOtp = data.getJSONObject("passwordOtp");
                String createdAt_n = signupOtp.getString("createdAt");
                String expireAt_n = signupOtp.getString("expireAt");
                String otp_n = signupOtp.getString("otp");
                Log.d("otp_new",otp_n);

                JSONObject msg = success.getJSONObject("msg");
                String replyCode = msg.getString("replyCode");
                String replyMessage = msg.getString("replyMessage");

//                String access_token = data.getString("access_token");
//                SharedPrefManager.getInstance(getApplicationContext()).storeAccessToken(access_token);

                if (realm.equals("user"))
                {
                    Intent intent = new Intent(NewPassword.this, Login.class);
                    Toast.makeText(NewPassword.this, R.string.success_chage_pass, Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(NewPassword.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(NewPassword.this)) {
            if (SharedPrefManager.getLangId(NewPassword.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(NewPassword.this,RequestCode.LangId));
            } else {
                Toast.makeText(NewPassword.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(NewPassword.this,RequestCode.LangId, langval);
    }
}
