<?php

    require("db_connection.php");

    if(isset($_POST['Token'])){
        
        $token= $_POST['Token'];
        //insecure query
        $query="INSERT INTO tution_teacher(t_fcm_token) VALUES('$token') ON DUPLICATE KEY UPDATE t_fcm_token='$token'";
        mysqli_query($con,$query);
        mysqli_close($con);
    }

?>