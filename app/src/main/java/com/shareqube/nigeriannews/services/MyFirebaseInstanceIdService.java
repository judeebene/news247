package com.shareqube.nigeriannews.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by judeebene on 25/3/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String LOG_TAG =  MyFirebaseInstanceIdService.class.getSimpleName();
    private static final String FCM_TESTING = "reminder";

    @Override
    public void onTokenRefresh() {
        // If you need to handle the generation of a token, initially or after a refresh this is
        // where you should do that.
        String token = FirebaseInstanceId.getInstance().getToken();


        // Once a token is generated, we subscribe to topic.
        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TESTING);
    }
}
