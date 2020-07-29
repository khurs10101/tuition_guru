<?php

    require("db_connection.php");

    $username= $_POST['username'];
    $password= $_POST['password'];

    $query= mysqli_prepare($con,"SELECT tcoins FROM tution_teacher WHERE tusername=? AND tpassword= ?");
    mysqli_stmt_bind_param($query,"ss",$username,$password);
    mysqli_stmt_execute($query);
    $result= mysqli_stmt_get_result($query);

    if(mysqli_num_rows($result)==1){
        // $data[]=array();
        // while($row=mysqli_fetch_assoc($result)){
        //     $data['coin']=$row['tcoins'];
        // }
        $row=mysqli_fetch_assoc($result);
        $data['coin']=$row['tcoins'];
        echo (string)$data['coin'];
    }else{
        echo 'x';
    }


    mysqli_stmt_close($query);
    mysqli_close($con);

?>