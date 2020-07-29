<?php

    require("db_connection.php");

    $username= $_POST['username'];
    $password= $_POST['password'];
    $enable= $_POST['enable'];
    $enable= (int)$enable;
    $query= mysqli_prepare($con,"SELECT temail_opt FROM tution_teacher WHERE tusername= ? AND tpassword= ?");
    mysqli_stmt_bind_param($query,"ss",$username,$password);
    mysqli_stmt_execute($query);
    $result= mysqli_stmt_get_result($query);
    $data=array();
    while($row=mysqli_fetch_assoc($result)){
        $data['enable']= $row['temail_opt'];
    }
    mysqli_stmt_close($query);
    

    $option=$data['enable'];

    if($option!=1 && $enable==1){
        //set 1
        $query=mysqli_prepare($con,"UPDATE tution_teacher SET temail_opt = ? WHERE tusername= ? AND tpassword= ?");
        mysqli_stmt_bind_param($query,"iss",$enable,$username,$password);
        mysqli_stmt_execute($query);
        // echo mysqli_stmt_affected_rows($query);
        if((int)mysqli_stmt_affected_rows($query)==1){
            // successful
            echo 'x';
        }
        mysqli_stmt_close($query);
        
    }else if($option!=0 && $enable==0){
        //set 0
        $query=mysqli_prepare($con,"UPDATE tution_teacher SET temail_opt = ? WHERE tusername= ? AND tpassword= ?");
        mysqli_stmt_bind_param($query,"iss",$enable,$username,$password);
        mysqli_stmt_execute($query);
        if((int)mysqli_stmt_affected_rows($query)==1){
            // successful
            echo 'y';
        }

        mysqli_stmt_close($query);
        
    }else if($option==1 && $enable==1){
        //already enabled
        echo 'a';
    }else if($option==0 && $enable==0){
        //already disabled
        echo 'b';
    }else{
        //error
        echo 'c';
    }



?>