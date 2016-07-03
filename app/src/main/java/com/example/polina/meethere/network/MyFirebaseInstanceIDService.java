package com.example.polina.meethere.network;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by polina on 24.06.16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
        @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println(" TTTTTTTTTTTTTTTTTTTTTTTTTTTTTToken" + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
       // sendRegistrationToServer(refreshedToken);
    }
}
