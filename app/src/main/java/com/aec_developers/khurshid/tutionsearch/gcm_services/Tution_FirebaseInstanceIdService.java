package com.aec_developers.khurshid.tutionsearch.gcm_services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Khurshid on 3/17/2018.
 */

public class Tution_FirebaseInstanceIdService extends FirebaseInstanceIdService {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        //write the token data to the data base
        //store the token in the sharedpreference and use it from every where
        preferences = getApplicationContext().getSharedPreferences("fcm_token", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
//        Log.v("FCM_MAIN",token);


//        registerToken(token);
    }

//    private void registerToken(String token) {
//        OkHttpClient client = new OkHttpClient();
//        //for post request
//        RequestBody body = new FormEncodingBuilder()
//                .add("Token", token)
//                .build();
//        //for taking to server
//        Request request = new Request.Builder()
//                .url(SiteManager.FCM_TOKEN_REGISTRATION)
//                .post(body)
//                .build();
//
//        //executing the request
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
