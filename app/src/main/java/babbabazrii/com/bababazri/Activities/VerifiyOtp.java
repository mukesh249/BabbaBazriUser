package babbabazrii.com.bababazri.Activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
//import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
//import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class VerifiyOtp extends AppCompatActivity implements WebCompleteTask {

    private static final String TAG = "VerifiyOtp";
    Button verifiy_btn,resend_btn;
    PinView pinView_txt;
    String  reg_as,reg_people_id,otp_string;
    int reg_people_otp;
    boolean isProgress = true;
    TextView resend_tv;
    ProgressDialog progressDialog = null;
    Context context;
    Toolbar toolbar_verifiy_Otp;
//    SmsVerifyCatcher smsVerifyCatcher;

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
        setContentView(R.layout.activity_verifiy_otp);

        //Button
        verifiy_btn = (Button)findViewById(R.id.verifiy_btn);
        resend_btn = (Button)findViewById(R.id.resend);
        toolbar_verifiy_Otp =(Toolbar)findViewById(R.id.toolbar_verifiyotp);
        setSupportActionBar(toolbar_verifiy_Otp);
        setTitle(getString(R.string.verifiy_otp));
        //PinView
        pinView_txt = (PinView)findViewById(R.id.pinView);
//        pinView_txt.setText(String.valueOf(SharedPrefManager.getInstance(this).getRegPeopleOtp()));

        //TextView
        resend_tv = (TextView) findViewById(R.id.resend_tv);

        //Regestration as
        reg_as = SharedPrefManager.getInstance(this).getRegAs();
        reg_people_id = SharedPrefManager.getInstance(this).getSignPeopleId();

        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                resend_tv.setVisibility(View.VISIBLE);
//                resend_btn.setEnabled(false);
                ResendOtp();
//                new CountDownTimer(300000,1000){
//
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//
//                        // resend.setText("" + String.format(FORMAT, TimeUnit));
//                        int seconds = (int) (millisUntilFinished / 1000);
//                        int minutes = seconds / 60;
//                        seconds = seconds % 60;
//                        resend_tv.setText(" " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        resend_tv.setText("Resend");
//                        resend_tv.setVisibility(View.GONE);
//                        resend_btn.setEnabled(true);
//                    }
//                }.start();
            }

        });

//        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
//            @Override
//            public void onSmsCatch(String message) {
////                String code = parseCode(message);//Parse verification code
//                Log.d("message:",message );
//                Pattern pattern = Pattern.compile("(\\d{4})");
//                Matcher matcher = pattern.matcher(message);
//                String val = "";
//                if (matcher.find()) {
//                    System.out.println(matcher.group(1));
//
//                    val = matcher.group(1);
//                }
//                pinView_txt.setText(val);//set code in edit text
//                //then you can send verification code to server
//            }
//        });

        verifiy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_people_otp = SharedPrefManager.getInstance(VerifiyOtp.this).getRegPeopleOtp();
                otp_string = String.valueOf(reg_people_otp);
                VerifiyMobileNo();
            }
        });
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
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    private void VerifiyMobileNo() {

        if (reg_people_id.equalsIgnoreCase("")) {
            Toast.makeText(this,"Id Not Found",Toast.LENGTH_LONG).show();
        } else {
            HashMap objectNew = new HashMap();
            new WebTask(VerifiyOtp.this, WebUrls.BASE_URL+WebUrls.checkResetOtp+"?peopleId="+reg_people_id+"&otp="+pinView_txt.getText().toString(), objectNew, VerifiyOtp.this, RequestCode.CODE_checkResetOtp, 0);
        }
    }
    private void ResendOtp(){
        HashMap objectNew = new HashMap();
        objectNew.put("peopleId", SharedPrefManager.getInstance(this).getSignPeopleId());
        objectNew.put("type","reset");
        new WebTask(VerifiyOtp.this, WebUrls.BASE_URL+WebUrls.verifiy_resend_api, objectNew, VerifiyOtp.this, RequestCode.CODE_Resend_VerifiyMobileNo_Api, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (taskcode==RequestCode.CODE_checkResetOtp) {
            try {
                Log.d(TAG,"response: "+response);
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
                Log.d("otp_exp_VerifiyOtp",otp_ex);

                JSONObject passwordOtp = data.optJSONObject("passwordOtp");
                String createdAt_n = passwordOtp.optString("createdAt");
                String expireAt_n = passwordOtp.optString("expireAt");
                Integer otp_n = passwordOtp.optInt("otp");
                Log.d("otp_new_VerifiyOtp", String.valueOf(otp_n));
                SharedPrefManager.getInstance(getApplicationContext()).storeRegPeopleOtp(otp_n);
//                JSONObject msg = success.getJSONObject("msg");
//                String replyCode = msg.getString("replyCode");
//                String replyMessage = msg.getString("replyMessage");

//                String access_token = data.getString("access_token");
//                SharedPrefManager.getInstance(getApplicationContext()).storeRegPeopleId(id);
//                SharedPrefManager.getInstance(getApplicationContext()).storeRegPeopleOtp(otp_n);
                if (realm.equals("user")) {
                    Intent intent = new Intent(VerifiyOtp.this, NewPassword.class);
                    Toast.makeText(VerifiyOtp.this, R.string.verify_success, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else {
                    Toast.makeText(VerifiyOtp.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (taskcode==RequestCode.CODE_Resend_VerifiyMobileNo_Api) {
            try {
                Log.d(TAG,"response: "+response);
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.optJSONObject("success");
                JSONObject data = success.optJSONObject("data");
                String adminApproval = data.optString("adminApproval");
                String realm = data.optString("realm");
                data.optString("id");
                data.optString("fullName");
                data.optString("firstName");
                data.optString("lastName");
                data.optJSONObject("address").optString("address");
                data.optJSONObject("address").optJSONObject("location").optString("lat");
                data.optJSONObject("address").optJSONObject("location").optString("lng");
                data.optString("mobile");
                data.optString("createdAt");
                data.optString("updatedAt");
                data.optBoolean("mobileVerified");

                JSONObject signupOtp = data.optJSONObject("signupOtp");
                String createdAt_ex = signupOtp.optString("createdAt");
                String expireAt_ex = signupOtp.optString("expireAt");
                String otp_ex = signupOtp.optString("otp");
                Log.d("otp_exp_Resend_VrfyOtp",otp_ex);

                JSONObject passwordOtp = data.optJSONObject("passwordOtp");
                String createdAt_n = passwordOtp.optString("createdAt");
                String expireAt_n = passwordOtp.optString("expireAt");
                Integer otp_n_v = passwordOtp.getInt("otp");
                Log.d("otp_new_Resend_VrfyOtp", String.valueOf(otp_n_v));

                JSONObject msg = success.getJSONObject("msg");
                String replyCode = msg.optString("replyCode");
                String replyMessage = msg.optString("replyMessage");

                if (realm.equals("user"))
                {
//                            Intent intent = new Intent(VerifiyOtp.this, NewPassword.class);
                    Toast.makeText(VerifiyOtp.this,R.string.otp_sent, Toast.LENGTH_SHORT).show();
//                    pinView_txt.setText(otp_n_v+"");
//                            startActivity(intent);
                }else {
                    Toast.makeText(VerifiyOtp.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
//        smsVerifyCatcher.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
//        smsVerifyCatcher.onStop();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(VerifiyOtp.this)) {
            if (SharedPrefManager.getLangId(VerifiyOtp.this, RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(VerifiyOtp.this,RequestCode.LangId));
            } else {
                Toast.makeText(VerifiyOtp.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(VerifiyOtp.this,RequestCode.LangId, langval);
    }
}
