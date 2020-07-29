<?php

    //including the database connection file
    require("db_connection.php");

    //getting data from post request

    $name= $_POST['name'];
    $email= $_POST['email'];
    $phone= $_POST['phone'];
    $age= $_POST['age'];
    $qualification= $_POST['qualification'];
    $field= $_POST['field'];
    $subject= $_POST['subject'];
    $specific_subject= $_POST['specificSubject'];
    $class= $_POST['class'];
    $username= $_POST['username'];
    $password= $_POST['password'];
    $city= $_POST['city'];
    $state= $_POST['state'];
    $fee= $_POST['fee'];
    $gender= $_POST['gender'];

    //query for test
    //insert the stuff into the table
    //first check if the usser already exists
    // // $user_exists_query="SELECT * FROM tution_teacher WHERE temail='$email' OR tphone='$phone'";
    // $query= "INSERT INTO tution_teacher (tname,temail,tphone,tage,tqualification,tfield,tsubject,tspecific_subject,tclass,tusername,tpassword,t_city,t_state)
    //  VALUES ('$name','$email','$phone','$age','$qualification','$field','$subject','$specific_subject','$class','$username','$password','$city','$state')";

    //query for final production
    $user_exists_query= mysqli_prepare($con,"SELECT * FROM tution_teacher WHERE temail=? OR tphone=?");
    mysqli_stmt_bind_param($user_exists_query,"ss",$email,$phone);
    mysqli_stmt_execute($user_exists_query);
    $result= mysqli_stmt_get_result($user_exists_query);
    

    $query_to_insert= mysqli_prepare($con, "INSERT INTO tution_teacher (tname,temail,tphone,tage,tqualification,tfield,tsubject,tspecific_subject,tclass,tusername,tpassword,t_city,t_state,tfee,tgender)
    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($query_to_insert,"sssssssssssssss",$name,$email,$phone,$age,$qualification,$field,$subject,$specific_subject,$class,$username,$password,$city,$state,$fee,$gender);

    //ends


    // $result= mysqli_query($con,$user_exists_query);
    
    //production query
    
    // $query_to_insert= mysqli_prepare($con,"INSERT INTO tution_student (s_name,s_mobile, s_email, s_username, s_password,city, state) VALUES (?,?,?,?,?,?,?)");
    // mysqli_stmt_bind_param($user_exists_query,"ss",$email,$phone);
    // mysqli_stmt_bind_param($query_to_insert,"sssssss",$name,$phone,$email,$username,$password,$city,$state);
    // mysqli_stmt_execute($user_exists_query);
    

    
    if(mysqli_num_rows($result)>0)
    {
        //user already exists
        echo "a";
    }
    else
    {
        if(mysqli_stmt_execute($query_to_insert)){
            //query successfull
            echo "b";
        }
        else{
            //query unsucessfull
            echo "c";
        }
    
    }


    mysqli_close($con);



?>