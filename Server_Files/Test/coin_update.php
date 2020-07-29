<?php

    require("db_connection.php");

    $username= $_POST['username'];
    $password= $_POST['password'];
    $coin= $_POST['coin'];
    $coin=(int)$coin;

    $query= mysqli_prepare($con,"UPDATE tution_teacher SET tcoins=? WHERE tusername= ? AND tpassword= ?");
    mysqli_stmt_bind_param($query,"iss",$coin,$username,$password);
    mysqli_stmt_execute($query);

    if((int)mysqli_stmt_affected_rows($query)==1){
        //update success
        echo "p";
    }else{
        //update failed

    }

    mysqli_stmt_close($query);
    mysqli_close($con);


?>