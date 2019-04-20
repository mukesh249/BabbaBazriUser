package babbabazrii.com.bababazri.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import babbabazrii.com.bababazri.Fragments.Profile;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class Change_Password extends AppCompatActivity implements WebCompleteTask {
    Toolbar toolbar_change;
    EditText cr_pass,new_pass,confirm_new_password;
    Button btn_save,btn_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        cr_pass = (EditText) findViewById(R.id.current_password);
        new_pass = (EditText)findViewById(R.id.new_password_changepassword);
        confirm_new_password = (EditText)findViewById(R.id.confirm_new_password_changepassword);
        btn_save = (Button)findViewById(R.id.change_save_btn);
        btn_cancel = (Button)findViewById(R.id.change_cancel_btn);

        toolbar_change = (Toolbar)findViewById(R.id.toolbar_change);
        setSupportActionBar(toolbar_change);
        setTitle(getString(R.string.change_password));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                User_Profile.getInstance().ReloadProfile();
//                startActivity(new Intent(Change_Password.this,Profile.class));
                finish();
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
        finish();
        return true;
    }

    public void ChangePassword(){
        HashMap objectNew = new HashMap();
        if (TextUtils.isEmpty(cr_pass.getText())) {
            cr_pass.setError(getString(R.string.notempty));
            cr_pass.requestFocus();
        }else if (new_pass.length() < 6) {
            new_pass.setError(getString(R.string.password_validation));
            new_pass.requestFocus();
        }else if (TextUtils.isEmpty(new_pass.getText())){
            new_pass.setError(getString(R.string.notempty));
            new_pass.requestFocus();
        } else if (!new_pass.getText().toString().equals(confirm_new_password.getText().toString())){
                confirm_new_password.setError(getString(R.string.new_and_confirm_not_match));
                confirm_new_password.requestFocus();
        }else {
                objectNew.put("oldPassword",cr_pass.getText()+"");
                objectNew.put("newPassword",new_pass.getText()+"");
                new WebTask(Change_Password.this, WebUrls.BASE_URL+WebUrls.ChangsePassword_Api,objectNew,Change_Password.this, RequestCode.CODE_ChangePassword,1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_ChangePassword){
            try {
                Log.d("ChangePassword Response",response);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObject = jsonObject.getJSONObject("success");
                JSONObject msgObject = successObject.optJSONObject("msg");
                if (msgObject.optString("replyCode").equals(msgObject.optString("replyMessage"))){
                    Toast.makeText(Change_Password.this, R.string.change_password_success,Toast.LENGTH_LONG).show();
//                    User_Profile.getInstance().ReloadProfile();
                    startActivity(new Intent(Change_Password.this,Profile.class));
                    finish();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(Change_Password.this)) {
            if (SharedPrefManager.getLangId(Change_Password.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Change_Password.this,RequestCode.LangId));
            } else {
                Toast.makeText(Change_Password.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Change_Password.this,RequestCode.LangId, langval);
    }
}
