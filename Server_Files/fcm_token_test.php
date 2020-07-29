<?php

    require("db_connection.php");


    if(isset($_POST['fcm_token'])){
        $token= $_POST['fcm_token'];
    }

    $query= "";



?>