package babbabazrii.com.bababazri.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class SelectLanguage extends AppCompatActivity {
    TextView english_tv,hindi_tv;
    String select_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        english_tv = (TextView)findViewById(R.id.english_tv_select_lang);
        hindi_tv = (TextView)findViewById(R.id.hindi_tv_select_lang);

        english_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                english_tv.setBackground(getResources().getDrawable(R.drawable.btn_request));
//                hindi_tv.setBackground(getResources().getDrawable(R.drawable.btn_price));
//                english_tv.setTextColor(getResources().getColor(R.color.white));
//                hindi_tv.setTextColor(getResources().getColor(R.color.textcolor));

                select_lang = english_tv.getText().toString();
                if (select_lang!=null ){
                    setLangRecreate("en");
                }else {
                    Toast.makeText(SelectLanguage.this, R.string.select_lang,Toast.LENGTH_SHORT).show();
                }
            }
        });
        hindi_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                english_tv.setBackground(getResources().getDrawable(R.drawable.btn_price));
//                hindi_tv.setBackground(getResources().getDrawable(R.drawable.btn_request));
//                hindi_tv.setTextColor(getResources().getColor(R.color.white));
//                english_tv.setTextColor(getResources().getColor(R.color.textcolor));
                select_lang = hindi_tv.getText().toString();
                if (select_lang!=null ){
                    setLangRecreate("hi");
                }else {
                    Toast.makeText(SelectLanguage.this,R.string.select_lang,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setLangRecreate(String langval) {
        Locale locale;
        Configuration config = getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPrefManager.setLangId(SelectLanguage.this, RequestCode.LangId, langval);
        Intent intent = new Intent(SelectLanguage.this,Login.class);
        startActivity(intent);
        finish();
    }
}