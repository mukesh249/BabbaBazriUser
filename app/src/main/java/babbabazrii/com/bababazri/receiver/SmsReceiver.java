package babbabazrii.com.bababazri.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import babbabazrii.com.bababazri.Interface.SmsListener;

/**
 * Created by mukku on 2/27/2018.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");
        mListener.messageReceived();
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

}

