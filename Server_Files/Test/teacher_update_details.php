<?php

    require("db_connection.php");

    $age= $_POST['age'];
    $phone= $_POST['phone'];
    $city= $_POST['city'];
    $subject= $_POST['subject'];
    $branch= $_POST['branch'];
    $field= $_POST['field'];
    $fee= $_POST['fee'];
    $uptoClass= $_POST['uptoClass'];
    $username= $_POST['username'];
    $password= $_POST['password'];
    $state= $_POST['state'];

    $query=mysqli_prepare($con,"UPDATE tution_teacher SET tage=?,tphone=?,t_city=?,tspecific_subject=?,tfield=?,tfee=?,tclass=?,tsubject=?,t_state=? WHERE tusername=? AND tpassword=?");
    mysqli_stmt_bind_param($query,"sssssssssss",$age,$phone,$city,$subject,$field,$fee,$uptoClass,$branch,$state,$username,$password);
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