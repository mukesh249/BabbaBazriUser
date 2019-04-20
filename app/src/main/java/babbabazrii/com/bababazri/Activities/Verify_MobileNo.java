package babbabazrii.com.bababazri.Activities;

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

public class Verify_MobileNo extends AppCompatActivity implements WebCompleteTask {

    PinView otp_veriMoblieNO;
    Button btn_resend__veriMoblieNO,btn_veriMoblieNO;
    Toolbar toolbar_Verifiy_Mobile_no;
    public Boolean isSignupComplete,mobileVerified;
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
        setContentView(R.layout.activity_verify__mobile_no);
        toolbar_Verifiy_Mobile_no =(Toolbar)findViewById(R.id.toolbar_Verifiy_Mobile_no);
        setSupportActionBar(toolbar_Verifiy_Mobile_no);
        setTitle(getString(R.string.virify_mobile));

        otp_veriMoblieNO = (PinView)findViewById(R.id.opt_verifiyMobileno);
        btn_resend__veriMoblieNO = (Button)findViewById(R.id.resend_verifiyMobileno);
        btn_veriMoblieNO = (Button)findViewById(R.id.verifiy_btn_verifiyMobileno);

//        otp_veriMoblieNO.setText(String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getRegPeopleOtp()));

        btn_veriMoblieNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veriMoblieNO();
            }
        });
//        smsVerifyCatcher.setFilter("<regexp>");
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
//                otp_veriMoblieNO.setText(val);//set code in edit text
//                //then you can send verification code to server
//            }
//        });


        btn_resend__veriMoblieNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendveriMoblieNO();
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
    private void veriMoblieNO() {
        if (TextUtils.isEmpty(otp_veriMoblieNO.getText())){
            otp_veriMoblieNO.setError(getString(R.string.otp_required));
            otp_veriMoblieNO.requestFocus();

        }else {
            HashMap objectNew = new HashMap();
            objectNew.put("peopleId", SharedPrefManager.getInstance(this).getSignPeopleId());
            objectNew.put("otp",otp_veriMoblieNO.getText().toString());
            objectNew.put("firebaseToken",SharedPrefManager.getInstance(this).getFireBaseToken());
            new WebTask(Verify_MobileNo.this, WebUrls.BASE_URL+WebUrls.verifiyMobileNo_Api, objectNew, Verify_MobileNo.this, RequestCode.CODE_VerifiyMobileNo_Api, 1);
        }

    }
    private void ResendveriMoblieNO() {
        HashMap objectNew = new HashMap();
        objectNew.put("peopleId", SharedPrefManager.getInstance(this).getSignPeopleId());
        objectNew.put("type","signup");
        new WebTask(Verify_MobileNo.this, WebUrls.BASE_URL+WebUrls.verifiy_resend_api, objectNew, Verify_MobileNo.this, RequestCode.CODE_Resend_VerifiyMobileNo_Api, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode==RequestCode.CODE_VerifiyMobileNo_Api) {
            Log.d("Verifiy MobileNO:Rspns ",response);
            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.optJSONObject("success");
                JSONObject data = success.optJSONObject("data");

                String id = data.optString("id");
                String userId = data.optString("userId");
                String firebaseToken = data.optString("firebaseToken");
                JSONObject userObject = data.getJSONObject("user");

                String adminApproval = userObject.optString("adminApproval");
                String realm = userObject.optString("realm");
                String fullName = userObject.optString("fullName");
                String firstName = userObject.optString("firstName");
                String lastName = userObject.optString("lastName");

                JSONObject addressObj = userObject.optJSONObject("address");
                addressObj.optString("address");
                JSONObject locationObj = addressObj.optJSONObject("location");
                locationObj.optString("lat");
                locationObj.optString("lng");


                String mobile = userObject.optString("mobile");
                mobileVerified = userObject.getBoolean("mobileVerified");
                isSignupComplete = userObject.getBoolean("isSignupComplete");

                JSONObject signupOtp = userObject.optJSONObject("signupOtp");
                Integer otp = signupOtp.optInt("otp");
                Log.d("otp_signup", String.valueOf(otp));
                String access_token = data.optString("access_token");

                JSONObject msg = success.optJSONObject("msg");
                String replyCode = msg.getString("replyCode");
                String replyMessage = msg.getString("replyMessage");

                if (mobileVerified)
                {
                    Intent intent = new Intent(Verify_MobileNo.this, Login.class);
                    Toast.makeText(Verify_MobileNo.this, R.string.sign_up_successfully, Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Verify_MobileNo.this, R.string.mobile_not_verified, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (taskcode==RequestCode.CODE_Resend_VerifiyMobileNo_Api) {
            Log.d("Verifiy Mobile Resend: ",response);
            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                JSONObject success = jsonObject.optJSONObject("success");
                JSONObject data = success.optJSONObject("data");

                data.optString("adminApproval");
                data.optString("realm");
                String id = data.optString("id");
                String fullName = data.optString("fullName");
                String firstName = data.optString("firstName");
                String lastName = data.optString("lastName");
                Boolean mobileVerified = data.optBoolean("mobileVerified");


                JSONObject signupOtp = data.optJSONObject("signupOtp");
                Integer otp = signupOtp.optInt("otp");
                Log.d("otp_signup", String.valueOf(otp));


//                if (mobileVerified)
//                {
//                    Intent intent = new Intent(Verify_MobileNo.this, Login.class);
                    Toast.makeText(Verify_MobileNo.this, R.string.otp_sent, Toast.LENGTH_SHORT).show();
//                    otp_veriMoblieNO.setText(otp+"");
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(getApplication(), "Something Worng", Toast.LENGTH_SHORT).show();
//                }

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
        if (SharedPrefManager.getIslagChange(Verify_MobileNo.this)) {
            if (SharedPrefManager.getLangId(Verify_MobileNo.this, RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Verify_MobileNo.this,RequestCode.LangId));
            } else {
                Toast.makeText(Verify_MobileNo.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Verify_MobileNo.this,RequestCode.LangId, langval);
    }
}
