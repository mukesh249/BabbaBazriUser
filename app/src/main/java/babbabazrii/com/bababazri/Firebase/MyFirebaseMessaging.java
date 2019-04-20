package babbabazrii.com.bababazri.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import babbabazrii.com.bababazri.Fragments.Home;
import babbabazrii.com.bababazri.R;


public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        if (remoteMessage.getNotification() != null){
//            User_Profile.getInstance().updateHotCountn();
//            showNotification(remoteMessage.getNotification());
//        }
        showNotification(remoteMessage.getNotification());
    }

    private void showNotification(RemoteMessage.Notification notification) {
        Log.d("Notification_firebase: " ,notification+"");
        Intent intent =new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());

//        User_Profile.getInstance().RefreshHome();
        Home.getInstance().CheckOrder_On_List();
        Home.getInstance().updateHotCountn();

//        if (Objects.requireNonNull(notification.getTitle()).equalsIgnoreCase("Order delivered")){
//            Home.getInstance().Home_time_Hide();
//        }
//        else if (Objects.requireNonNull(notification.getTitle()).equalsIgnoreCase("Order delivered")){
//            Home.getInstance().Home_time_Show();
//        }
    }
}
