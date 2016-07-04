package com.example.polina.meethere.network;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.polina.meethere.activities.EventActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by polina on 24.06.16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.err.println("!!!! RECEIVE:: " + remoteMessage);
        Intent i = new Intent(EventActivity.BC_FILTER + remoteMessage.getData().get("event_id"));
        for (Map.Entry<String, String> e : remoteMessage.getData().entrySet()) {
            i.putExtra(e.getKey(), e.getValue());
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

    }
}
