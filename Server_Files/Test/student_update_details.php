<?php

    require("db_connection.php");

    $age= $_POST['age'];
    $phone= $_POST['phone'];
    $city= $_POST['city'];
    $username= $_POST['username'];
    $password= $_POST['password'];
    $state= $_POST['state'];
    $gender= $_POST['gender'];

    $query=mysqli_prepare($con,"UPDATE tution_student SET s_age=?,s_phone=?,city=?,state=?,s_gender=? WHERE s_email=? AND s_password=?");
    mysqli_stmt_bind_param($query,"sssssss",$age,$phone,$city,$state,$gender,$username,$password);
    mysqli_stmt_execute($query);


    if(mysqli_stmt_affected_rows($query)==1){
        //success
        echo "a";
    }else{
        //some error
        echo "c";
    }

    mysqli_close($con);


?>