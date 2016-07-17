package com.example.polina.meethere.network;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.polina.meethere.activities.EventActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by polina on 24.06.16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.err.println("!!!! RECEIVE:: " + remoteMessage);
        switch (remoteMessage.getData().get("type")) {
            case "comment":
                try {
                    JSONObject data = new JSONObject(remoteMessage.getData().get("data"));
                    Intent i = new Intent(EventActivity.BC_FILTER + data.get("event_id"));
                    for (Map.Entry<String, String> e : remoteMessage.getData().entrySet()) {
                        i.putExtra(e.getKey(), e.getValue());
                    }
                    LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "activity":
                try {
                    JSONObject data = new JSONObject(remoteMessage.getData().get("data"));
                    System.err.println("RECEIVE ACTIVITY: " + data);
                    // TODO: not implemented yet. should be something like:
//                    Intent i = new Intent(FeedActivity.BC_FILTER);
//                    for (Map.Entry<String, String> e : remoteMessage.getData().entrySet()) {
//                        i.putExtra(e.getKey(), e.getValue());
//                    }
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

    }
}
