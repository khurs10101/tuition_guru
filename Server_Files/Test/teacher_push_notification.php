<?php

    //functions to send the data to the fcm server
    function send_notification($token, $message){

        $url='https://fcm.googleapis.com/fcm/send';
        $field= array(
            'registration_ids'=> $token,
            'data'=> $message
        );

        $headers= array(
            'Authorization:key = AAAAgvXA5Zg:APA91bGFlHEjCekqoAZR8NjP0Fy6UL6Jo-TvCPSvNfNWcB2za-nP_E1o01C7FjFykurLSIt5-_XZeyAkSQOrbYG6Kxpd5wJHrwUBd3PsudIodLfwHe55BD19zXoaU6kDJbexOuw4xLAQ',
            'Content-Type: application/json'
        );

        //the curl code
        $ch= curl_init();
        curl_setopt($ch,CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSH_VERIFYHOST,0);
        curl_setopt($ch, CURLOPT_SSH_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($field));
        $result= curl_exec($ch);
        if($result === FALSE){
            die('Curl failed: '.curl_error($ch));
        }
        curl_close($ch);

    }


?>