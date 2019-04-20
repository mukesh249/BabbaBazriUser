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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Change_Email extends AppCompatActivity implements WebCompleteTask{
    Toolbar toolbar_change_email;
    EditText editText_email;
    TextView change_email_tv,email_verifiy_or_not;
    Button clear_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__email);

        toolbar_change_email = (Toolbar)findViewById(R.id.toolbar_change_email);
        setSupportActionBar(toolbar_change_email);
        setTitle("Change Email");

        editText_email = (EditText)findViewById(R.id.change_email_et);
        change_email_tv = (TextView)findViewById(R.id.change_email_tv);
        email_verifiy_or_not = (TextView)findViewById(R.id.email_verifiy_or_not);
        clear_txt = (Button)findViewById(R.id.btn_clear);


        email_verifiy_or_not.setText(Profile.email_verifiy_or_not);
        editText_email.append(Profile.editText_email.getText());
        clear_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_email.setText("");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_profile){
            UpdateUserEmail();
        }
        return super.onOptionsItemSelected(item);
    }
    public void UpdateUserEmail(){
        HashMap objectNew = new HashMap();
        if (TextUtils.isEmpty(editText_email.getText())){
            editText_email.setError(getString(R.string.notempty));
            editText_email.requestFocus();
        }
        else {
            objectNew.put("email",editText_email.getText()+"");
            new WebTask(this, WebUrls.BASE_URL+WebUrls.Update_UserEmail_Api,objectNew,Change_Email.this, RequestCode.CODE_UpdateUserEmail,1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_UpdateUserEmail){
            Log.d("UpdateUserName Response",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject succesObject = jsonObject.optJSONObject("success");
                JSONObject dataObject = succesObject.optJSONObject("data");

                if (dataObject.optString("email")!=null){
                    editText_email.setText(dataObject.optString("email"));
                    if (dataObject.optBoolean("emailVerified")){
//                        edit_3.setVisibility(View.VISIBLE);
//                        not_verifiy.setVisibility(View.GONE);
                    }else {
//                        edit_3.setVisibility(View.GONE);
//                        not_verifiy.setVisibility(View.VISIBLE);
                    }
                }
                JSONObject msgObject = succesObject.optJSONObject("msg");
                if (msgObject.optString("replyCode").equals(msgObject.optString("replyMessage"))){
                    Toast.makeText(Change_Email.this,"Go on Your Email and Verifiy!!!",Toast.LENGTH_LONG).show();
//                    User_Profile.getInstance().ReloadProfile();
                    startActivity(new Intent(Change_Email.this,Profile.class));
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
        if (SharedPrefManager.getIslagChange(Change_Email.this)) {
            if (SharedPrefManager.getLangId(Change_Email.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(Change_Email.this,RequestCode.LangId));
            } else {
                Toast.makeText(Change_Email.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
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
        SharedPrefManager.setLangId(Change_Email.this,RequestCode.LangId, langval);
    }
}
