package babbabazrii.com.bababazri.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.models.Demo_User;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Demo_login extends AppCompatActivity {

    Button signIn_btn,signUp_btn;
    LinearLayout rootLayout;
    private AlertDialog waitingDialog;
    Context mContext;

    //firebase var
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Arkhip_font.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build());
        setContentView(R.layout.activity_demo_login);

        //init firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        waitingDialog = new ProgressDialog(Demo_login.this,R.style.Custom);
        //init View
        signIn_btn = (Button)findViewById(R.id.demo_signin_btn);
        signUp_btn = (Button)findViewById(R.id.demo_signup_btn);
        rootLayout = (LinearLayout) findViewById(R.id.root_layout_demo);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInDialog();
            }
        });


    }

    private void showSignInDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please Use Email To Sign In");

        LayoutInflater inflater = LayoutInflater.from(this);
        View singin_layout = inflater.inflate(R.layout.demo_signin,null);

        final MaterialEditText editEmail = singin_layout.findViewById(R.id.edtEmail_in);
        final MaterialEditText editPassword = singin_layout.findViewById(R.id.edtPassword_in);

        dialog.setView(singin_layout);

        //set Buttons
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //set disable button Sign In if is processing
                signIn_btn.setEnabled(false);
                //check validation
                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter your Email address", Snackbar.LENGTH_LONG).show();
                    signIn_btn.setEnabled(true);
                    return;
                }
                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter your password", Snackbar.LENGTH_LONG).show();
                    signIn_btn.setEnabled(true);
                    return;
                }
                if (editPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "Password is too short!!!", Snackbar.LENGTH_LONG).show();
                    signIn_btn.setEnabled(true);
                    return;
                }

                waitingDialog.show();
                //Login
                auth.signInWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.show();
                                startActivity(new Intent(Demo_login.this,Tracking.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.show();
                                Snackbar.make(rootLayout,"Failed" + e.getMessage(),Snackbar.LENGTH_LONG).show();

                                //active buttons
                                signIn_btn.setEnabled(true);
                            }
                        });

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showSignUpDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign Up");
        dialog.setMessage("Please Use Email to Sign Up");

        LayoutInflater inflater = LayoutInflater.from(this);
        View singup_layout = inflater.inflate(R.layout.demo_register,null);

        final MaterialEditText editEmail = singup_layout.findViewById(R.id.edtEmail);
        final MaterialEditText editPassword = singup_layout.findViewById(R.id.edtPassword);
        final MaterialEditText editName = singup_layout.findViewById(R.id.edtName);
        final MaterialEditText editPhone = singup_layout.findViewById(R.id.edtPhone);

        dialog.setView(singup_layout);

        //set Buttons
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //check validation
                if (TextUtils.isEmpty(editEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter your Email address",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter your password",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (editPassword.getText().toString().length()<6){
                    Snackbar.make(rootLayout,"Password is too short!!!",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editName.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter your Name ",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(editPhone.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter your Phone Number",Snackbar.LENGTH_LONG).show();
                    return;
                }

                //Register new user
                auth.createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //save user to db
                                Demo_User demo_user = new Demo_User();
                                demo_user.setEmail(editEmail.getText().toString());
                                demo_user.setPassword(editPassword.getText().toString());
                                demo_user.setName(editName.getText().toString());
                                demo_user.setPhone(editPhone.getText().toString());

                                //user email to key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(demo_user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout,"Register success fully",Snackbar.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout,"Failed" + e.getMessage(),Snackbar.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout,"Failed" + e.getMessage(),Snackbar.LENGTH_LONG).show();
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
