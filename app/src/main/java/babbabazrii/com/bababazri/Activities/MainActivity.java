package babbabazrii.com.bababazri.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;

import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button logout;
    Context context;
    String fb_access_token,id,name,email,profilePicUrl;
    TextView tv_fb_token,tv_fb_id,tv_fb_name,tv_fb_email;
    ImageView imv_fb_pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = (Button)findViewById(R.id.fblogout);

        tv_fb_name = (TextView)findViewById(R.id.fbname);
        tv_fb_email = (TextView)findViewById(R.id.fbemail);
        tv_fb_token = (TextView)findViewById(R.id.fbaccesstoken);
        imv_fb_pro = (ImageView)findViewById(R.id.fbprofileimage);


        mAuth = FirebaseAuth.getInstance();

        id = SharedPrefManager.getInstance(this).getFbid();
        name = SharedPrefManager.getInstance(this).getfbname();
        email =SharedPrefManager.getInstance(this).getfbemail();
        fb_access_token =SharedPrefManager.getInstance(this).getFbAccessToken();
        profilePicUrl = SharedPrefManager.getInstance(this).getFbimage();


        tv_fb_name.setText(name);
        tv_fb_email.setText(email);
        tv_fb_token.setText(fb_access_token);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                updateUI();
//                if (SharedPrefManager.getInstance(MainActivity.this).getIsLoggedIn(true)){
//                    SharedPrefManager.getInstance(MainActivity.this).storeIsLoggedIn(false);
//                    updateUI();
//                }


            }
        });
        new MainActivity.DownloadImage((ImageView)findViewById(R.id.fbprofileimage)).execute(profilePicUrl);
    }
    private void updateUI() {
        Intent main = new Intent(MainActivity.this, Login.class);
        Toast.makeText(MainActivity.this,"Your are Loged Out", Toast.LENGTH_LONG).show();
        startActivity(main);
        finish();

    }
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
