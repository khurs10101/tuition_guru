<?php

    require("db_connection.php");

    $username= trim($_POST['username']);
    $password= trim($_POST['password']);
    $token= trim($_POST['Token']);

    // $is_user_exists_query= "SELECT * FROM tution_teacher WHERE tusername='$username' AND tpassword='$password'";

    //producion code
    $is_user_exists_query= mysqli_prepare($con,"SELECT * FROM tution_teacher WHERE tusername=? AND tpassword=?");
    mysqli_stmt_bind_param($is_user_exists_query,"ss",$username,$password);
    mysqli_stmt_execute($is_user_exists_query);
    $result= mysqli_stmt_get_result($is_user_exists_query);
    //ends


    // $result= mysqli_query($con,$is_user_exists_query);

    if(mysqli_num_rows($result)==1){
        //user exists
        //update the fcm registration token
        $token_update= mysqli_prepare($con,"UPDATE tution_teacher SET t_fcm_token = ? WHERE tusername=? AND tpassword=?");
        mysqli_stmt_bind_param($token_update,"sss",$token,$username,$password);
        mysqli_stmt_execute($token_update);

        // mysqli_query($con,"UPDATE tution_teacher SET t_fcm_token ='$token' WHERE tusername='$username' AND tpassword= '$password'");

        //return json 
        $user= array();
        while($row= mysqli_fetch_assoc($result)){
            $user[]=$row;
        }

        echo json_encode($user);

    }else{
        //user dont exists
        echo "c";
    }


    mysqli_close($con);


?>