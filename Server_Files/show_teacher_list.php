<?php

    require("db_connection.php");

    //display the list of teacher only when it is match by legitimate user
    $username= $_POST['username'];
    $password= $_POST['password'];


    // $authenticate_query="SELECT s_id FROM tution_student WHERE s_username='$username' AND s_password='$password'";
    // $authenticate_result= mysqli_query($con,$authenticate_query);

    $authenticate_query=mysqli_prepare($con,"SELECT s_id FROM tution_student WHERE s_email='$username' AND s_password='$password'");
    mysqli_stmt_bind_param($authenticate_query,"ss",$username,$password);
    mysqli_stmt_execute($authenticate_query);
    $authenticate_result= mysqli_stmt_get_result($authenticate_query);

    $teacher_list_query="SELECT * FROM tution_teacher";

    if(mysqli_num_rows($authenticate_result)==1){
        //the query is valid show
        $result= mysqli_query($con,$teacher_list_query);
        $teacher= array();

        while($row=mysqli_fetch_assoc($result)){
            $teacher[]= $row;
        }

        //spit out list as json
        echo json_encode($teacher);

    }else{
        echo "authentication failed";
    }




?>