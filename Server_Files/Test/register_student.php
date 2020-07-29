<?php

    //first make database connection
	require("db_connection.php");
	
	//get data from the app
	$name= $_POST['name'];
	$email= $_POST['email'];
	$phone= $_POST['phone'];
	// $username= $_POST['username'];
	$password= $_POST['password'];
	$city= $_POST['city'];
	$state= $_POST['state'];
	

	 //first check if the usser already exists
	// $user_exists_query="SELECT * FROM tution_student WHERE s_email='$email' OR s_mobile='$phone'";
	// //insert the data into SQL table and check if the insertion is successfull or not
	// $query="INSERT INTO tution_student (s_name, s_age, s_gender, s_mobile, s_email, s_username, s_password, address, city, state, pincode) VALUES ('$name', NULL, NULL, '$phone', '$email', '$username', '$password', NULL, '$city', '$state', NULL)";
	/*$result=mysqli_query($con,$query) or die("Query failed");*/
    
    //production query
    $user_exists_query= mysqli_prepare($con,"SELECT * FROM tution_student WHERE s_email=? OR s_mobile=?");
    $query_to_insert= mysqli_prepare($con,"INSERT INTO tution_student (s_name,s_mobile, s_email,s_password,city,state) VALUES (?,?,?,?,?,?)");
    mysqli_stmt_bind_param($user_exists_query,"ss",$email,$phone);
    mysqli_stmt_bind_param($query_to_insert,"ssssss",$name,$phone,$email,$password,$city,$state);
    mysqli_stmt_execute($user_exists_query);
    $result= mysqli_stmt_get_result($user_exists_query);

    
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
    
    
    
    //ends

    

    

	// $result= mysqli_query($con,$user_exists_query);
    
    // if(mysqli_num_rows($result)>0)
    // {
    //     //user already exists
    //     echo "a";
    // }
    // else
    // {
    //     if(mysqli_query($con,$query)){
    //         //query successfull
    //         echo "b";
    //     }
    //     else{
    //         //query unsucessfull
    //         echo "c";
    //     }
    // }

	
	
	mysqli_close($con);

?>