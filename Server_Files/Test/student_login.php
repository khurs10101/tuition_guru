<?php

    require("db_connection.php");

    $username= trim($_POST['username']);
    $password= trim($_POST['password']);

    // $is_user_exists_query= "SELECT * FROM tution_student WHERE s_username='$username' AND s_password='$password'";
    $is_user_exists_query= mysqli_prepare($con,"SELECT * FROM tution_student WHERE s_email=? AND s_password=?");
    mysqli_stmt_bind_param($is_user_exists_query,"ss",$username,$password);
    mysqli_stmt_execute($is_user_exists_query);
    $result= mysqli_stmt_get_result($is_user_exists_query);
   


    // $result= mysqli_query($con,$is_user_exists_query);

    if(mysqli_num_rows($result)==1){
        //user exists
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