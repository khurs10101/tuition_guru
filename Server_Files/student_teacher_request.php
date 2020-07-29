<?php

    require("db_connection.php");

    $name= $_POST['name'];
    $phone= $_POST['phone'];
    $email= $_POST['email'];
    $field= $_POST['field'];
    $subject= $_POST['subject'];
    $city= $_POST['city'];
    $state= $_POST['state'];

    $query= mysqli_prepare($con,"SELECT * FROM tution_request WHERE rname=? AND rphone=? AND remail=? AND rfield=? AND rsubject=?");
    mysqli_stmt_bind_param($query,"sssss",$name,$phone,$email,$field,$subject);
    mysqli_stmt_execute($query);
    $result= mysqli_stmt_get_result($query);

    if((int)mysqli_num_rows($result)>0){
        //query already submitted
        echo "b";
    }else{
        //prepare to insert the data into the table
        $query_to_insert= mysqli_prepare($con,"INSERT INTO tution_request (rname,rphone,remail,rfield,rsubject,rcity,rstate) VALUES (?,?,?,?,?,?,?)");
        mysqli_stmt_bind_param($query_to_insert,"sssssss",$name,$phone,$email,$field,$subject,$city,$state);
        mysqli_stmt_execute($query_to_insert);
        $result= mysqli_stmt_get_result($query_to_insert);

        if(mysqli_affected_rows($con)>0){
            //query successfull
            echo "a";
        }

    }

    mysqli_stmt_close($query);
    mysqli_stmt_close($query_to_insert);
    mysqli_close($con);

    

?>