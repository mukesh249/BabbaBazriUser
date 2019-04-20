package babbabazrii.com.bababazri.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import babbabazrii.com.bababazri.SharedPrefManager;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myfirebaseid","Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        Common.currentToken = refreshedToken;
        storeToken(refreshedToken);
    }
    private void storeToken(String token){
        SharedPrefManager.getInstance(getApplicationContext()).storeFireBaseToken(token);
    }
}
