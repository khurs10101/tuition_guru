package com.aec_developers.khurshid.tutionsearch.gcm_services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Khurshid on 3/17/2018.
 */

public class Tution_FirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //message is recieved from here
        //add message dynamically to the  arrayadapter

    }
}
